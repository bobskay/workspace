package wang.wangby.springboot.autoconfigure.velocity;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.util.HtmlUtils;
import wang.wangby.springboot.autoconfigure.json.JsonAutoConfiguration;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.template.SimpleObject;
import wang.wangby.utils.template.SimpleObjectConvertor;
import wang.wangby.utils.template.TemplateUtil;
import wang.wangby.utils.template.TrimAllDirective;

import java.util.Properties;

@Configuration
@ConditionalOnClass(TemplateUtil.class)
@Import(JsonAutoConfiguration.class)
@Slf4j
public class VelocityAutoConfiguration {

	@Autowired
	JsonUtil jsonUtil;

	@ConfigurationProperties(prefix = "my.velocity")
	@Bean
	public VelocityProperties velocityProperties(){
		return new VelocityProperties();
	}

	@Bean
	public TemplateUtil templateUtil(VelocityProperties velocityProperties) {
		initSimpleObject();

		VelocityEngine velocityEngine = new VelocityEngine();
		Properties p = new Properties();
		p.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		p.setProperty("userdirective", TrimAllDirective.class.getName());
		p.setProperty("input.encoding", velocityProperties.getEncoding());
		p.setProperty("output.encoding", velocityProperties.getEncoding());
		p.setProperty("runtime.log", velocityProperties.getLog());
		velocityEngine.init(p);
		TemplateUtil util=new TemplateUtil(velocityEngine, velocityProperties.getRoot());
		log.info("veclocity自动配置完成,配置信息:{}",velocityProperties);
		return util;
	}

	private void initSimpleObject() {
		SimpleObjectConvertor convertor = new SimpleObjectConvertor() {
			//将对象转为json
			public String toJson(Object target) {
				return jsonUtil.toString(target);
			}

			//过滤html字符
			public String htmlEscape(String str) {
				return HtmlUtils.htmlEscape(str);
			}
		};
		SimpleObject.init(convertor);
	}


}
