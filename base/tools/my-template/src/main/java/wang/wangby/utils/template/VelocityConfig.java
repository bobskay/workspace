package wang.wangby.utils.template;

import lombok.Data;

@Data
public class VelocityConfig {
	private String encoding="UTF-8";
	private String root="/templates";
	private String log="velocity.log";
}
