package wang.wangby.monitor.model.grafana;

import lombok.Data;

@Data
public class DashboardCreater {
    Dashboard dashboard;
    Boolean overwrite;
}
