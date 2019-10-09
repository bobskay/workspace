package wang.wangby.springboot.autoconfigure.velocity;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
public class VelocityProperties {
	private String encoding="UTF-8";
	private String root="/templates";//模板跟路径,相对于classpath
	private String log="/opt/logs/velocity/velocity.log";//velocity日志地址
}
