package wang.wangby.springboot.autoconfigure.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.json.FastJsonImpl;

@Configuration
@Slf4j
public class JsonAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JsonUtil fastJson() {
		FastJsonImpl.initGlobalConfig(null,null);
		return new FastJsonImpl();
	}


}
