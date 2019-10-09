package wang.wangby.springboot.autoconfigure.json;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import wang.wangby.utils.JsonUtil;

public class JacksonImpl extends JsonUtil {

	private ObjectMapper objectMapper;

	public JacksonImpl() {
		objectMapper = new ObjectMapper();
		// 不显示为null的字段
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// 忽略未知属性
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);// 忽略Object

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(simpleModule);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public String toString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T toBean(String str, Class<T> t) {
		try {
			return objectMapper.readValue(str, t);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String toFormatString(Object obj) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	public ObjectMapper objectMapper() {
		return objectMapper;
	}
}
