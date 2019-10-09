package wang.wangby.monitor.model.grafana;
import lombok.Data;
@Data
public class Legend{
    private Boolean current;
    private Boolean total;
    private Boolean alignAsTable;
    private Boolean avg;
    private Boolean min;
    private Boolean max;
    private Boolean values;
    private Boolean show;
    private Boolean sortDesc;
    private Boolean rightSide;
}