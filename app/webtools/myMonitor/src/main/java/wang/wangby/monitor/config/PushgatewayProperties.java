package wang.wangby.monitor.config;

import lombok.Data;

@Data
public class PushgatewayProperties {
    private long sendIntervalSecond=15;
    private String baseUrl;
    private boolean enabled;
}
