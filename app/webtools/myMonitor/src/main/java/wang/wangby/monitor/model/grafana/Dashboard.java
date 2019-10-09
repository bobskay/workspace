package wang.wangby.monitor.model.grafana;
import lombok.Data;

import java.util.List;
@Data
public class Dashboard{
    private Boolean hideControls;
    private Integer graphTooltip;
    private Integer schemaVersion;
    private Boolean editable;
    private List<Panel> panels;
    private String timezone;
    private Boolean refresh;
    private String title;
    private Integer version;
    private List<String> tags;
    private String gnetId;
    private String uid;
    private List links;
    private String style;
    private Integer id;
}