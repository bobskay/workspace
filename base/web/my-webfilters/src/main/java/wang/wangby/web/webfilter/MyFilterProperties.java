package wang.wangby.web.webfilter;

import lombok.Data;
import wang.wangby.annotation.Remark;

import java.util.HashMap;
import java.util.Map;

@Data
public class MyFilterProperties {
	
	@Remark("是否开启过滤器")
	private boolean enable;

	@Remark("要忽略的访问前缀,以逗号隔开")
	public String exclusions="webjars,resources,favicon.ico";

	@Remark("是否统计访问耗时")
	public boolean statis = true;
	@Remark("要忽略日志访问前缀,以逗号隔开")
	public String ignoreLog="/app/monitor,/test";

	@Remark("是否开启排队规则")
	public boolean queue = true;

	@Remark("查看监控结果的页面")
	private String monitorPage="/monitor";

	@Remark("请求允许的最大并发量")
	public Map<String, Integer> limit;

	// 格式化配置并发数的url,将-替换成/
	public Map<String, Integer> getLimitMap() {
		Map map = new HashMap();
		limit.forEach((url, count) -> {
			url = formatUrl(url);
			map.put(url, count);
		});
		return map;
	}

	// 格式化配置的url,因为配置文件不允许用/,所以配置的时候用-,还有路径必须以/开头
	public static String formatUrl(String url) {
		url = url.trim();
		if (!url.startsWith("/")) {
			url = "/" + url;
		}
		return url.replace("-", "/");
	}

}
