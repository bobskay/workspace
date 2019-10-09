package wang.wangby.springboot.autoconfigure.mvc;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wang.wangby.springboot.autoconfigure.json.JsonAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import(JsonAutoConfiguration.class)
@Slf4j
public class MvcAutoconfguration implements WebMvcConfigurer {

	@Bean
	public StringToDateConverter stringToDateConverter() {
		return new StringToDateConverter();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

		fastJsonConfig.setSerializeConfig(SerializeConfig.getGlobalInstance());
		fastJsonConfig.setParserConfig(ParserConfig.getGlobalInstance());

		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		converters.add(0,fastConverter);
	}

	@Bean
	@ConditionalOnClass(ProceedingJoinPoint.class)
	public RestControllerExceptionHandler errorHandler() {
		return new RestControllerExceptionHandler();
	}

}
