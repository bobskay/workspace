package wang.wangby.monitor.config;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
public class GrafanaProperties {
    @Remark("任务间隔")
    private long sendIntervalSecond=300;
    @Remark("grafana地址")
    private String baseUrl;
    @Remark("是否自动创建视图")
    private boolean enabled;
    @Remark("请求的token")
    private String token="eyJrIjoiUE40clNUTjdyQ3JLb1FzazV1c0ZFaVdodUlyOE50U1QiLCJuIjoibWluZyIsImlkIjoxfQ==";
}
