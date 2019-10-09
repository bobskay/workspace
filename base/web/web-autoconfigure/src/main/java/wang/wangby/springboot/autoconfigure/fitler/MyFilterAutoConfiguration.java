package wang.wangby.springboot.autoconfigure.fitler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.StringUtil;
import wang.wangby.web.webfilter.MyFilter;
import wang.wangby.web.webfilter.MyFilterProperties;
import wang.wangby.web.webfilter.QueueFilter;
import wang.wangby.web.webfilter.StatistFilter;

@Configuration
@ConditionalOnClass(MyFilter.class)
@ConditionalOnProperty(name ="my.webfilter.enable", matchIfMissing = false)
@Slf4j
public class MyFilterAutoConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "my.webfilter")
	public MyFilterProperties myFilterProperties() {
		return new MyFilterProperties();
	}
	
 
	@Bean
	@ConditionalOnMissingBean
	public MyFilter myFilter(MyFilterProperties properties) {
		List<String> exclusions=getFormatUrl(properties.getExclusions());
		List webFilters = new ArrayList();
		if (properties.statis) {
			List<String> igs=getFormatUrl(properties.ignoreLog);
			webFilters.add(new StatistFilter(properties.getMonitorPage(),igs));
		}
		if (properties.queue && properties.limit != null && properties.limit.size() > 0) {
			QueueFilter f = new QueueFilter(properties.getLimitMap());
			webFilters.add(f);
		}
		log.debug("创建自定义过滤器:"+properties);
		
		// 是否过滤某个链接
		MyFilter filter = new MyFilter(webFilters, url-> {
			for (String s : exclusions) {
				if (url.startsWith(s)) {
					return false;
				}
			}
			return true;
		});
		return filter;
	}




	private List<String> getFormatUrl(String url){
		List list=new ArrayList();
		url=url.trim();
		if(StringUtil.isEmpty(url)) {
			return list;
		}
		for(String s:url.split(",")) {
			String x=s.trim();
			if(StringUtil.isNotEmpty(x)) {
				if(!x.startsWith("/")) {
					x="/"+x;
				}
				list.add(x);
			}
		}
		return list;
	}
}
