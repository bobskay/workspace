package wang.wangby.web.webfilter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import wang.wangby.utils.monitor.MonitorUtil;
import wang.wangby.utils.monitor.vo.InvokeThread;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class MyFilter implements Filter {
	private static final org.slf4j.Logger monitorLog = org.slf4j.LoggerFactory.getLogger("monitorAgent");
	private List<WebFilter> webFilters;

	private Function<String, Boolean> filter;

	/**
	 * @param webFilters 自定义的过滤器链
	 * @param filter       判断一个uri是否可以访问,返回值如果是null说明全部允许 返回值如果有内容,就直接返回给页面,下面的过滤器不执行了
	 */
	public MyFilter(List<WebFilter> webFilters, Function<String, Boolean> filter) {
		this.webFilters = webFilters;
		this.filter = filter;
		if (filter == null) {
			filter = uri -> true;
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("初始化" + this.getClass().getName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestId=IdWorker.nextString();
		MDC.put("requestId", requestId);
		MonitorUtil.init("MyFilter.doFilter");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = httpRequest.getRequestURI();
		if (!filter.apply(uri)) {
			// 不符合的请求直接跳过
			try {
				chain.doFilter(request, response);
			} catch (Exception ex) {
				log.error("chain.doFilter:" + ex.getMessage(), ex);
			}
			return;
		}

		List results = new ArrayList();

		// 自定义过滤器开始
		for (WebFilter f : webFilters) {
			try {
				Object result = f.begin(httpRequest, httpResponse);
				if (result == null) {
					return;
				}
				results.add(result);
			} catch (Exception ex) {
				log.error("执行过滤器begin出错:" + f + ":" + ex.getMessage(), ex);
				results.add(null);
			}
		}

		// 系统的过滤器链
		try {
			chain.doFilter(request, response);
		} catch (Exception ex) {
			log.error("chain.doFilter:" + ex.getMessage(), ex);
		}

		// 结束,按自定链顺序反着来
		for (int i = webFilters.size() - 1; i >= 0; i--) {
			try {
				webFilters.get(i).end(results.get(i), httpRequest);
			} catch (Exception ex) {
				log.error("执行过滤器end出错:" + webFilters.get(i) + ":" + ex.getMessage(), ex);
				results.add(null);
			}

		}
		InvokeThread thread=MonitorUtil.finish();
		StringBuilder sb=new StringBuilder();
		thread.interator(m->{
			String pix=StringUtil.createString(m.getLevel(),"--");
			//忽略0.1ms一下的方法
			double time=(m.getEnd()-m.getBegin());
			if(time<1000*100){
				return;
			}
			time=time/1000/1000;
			sb.append(pix+m.getName()+","+time);
			sb.append("\n");
		});
		monitorLog.debug("\n"+sb.toString());
		MDC.remove("requestId");
	}

	@Override
	public void destroy() {
		log.info("销毁" + this.getClass().getName());
	}

}
