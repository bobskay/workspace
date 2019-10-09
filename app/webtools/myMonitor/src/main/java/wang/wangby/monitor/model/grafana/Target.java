package wang.wangby.monitor.model.grafana;
import lombok.Data;
@Data
public class Target{
    private Boolean hide;
    private String format;
    private String legendFormat;
    private String expr;
    private Integer step;
    private String refId;
    private Integer intervalFactor;
}