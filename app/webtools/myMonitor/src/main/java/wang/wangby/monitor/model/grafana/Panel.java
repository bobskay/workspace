package wang.wangby.monitor.model.grafana;

import lombok.Data;

import java.util.List;

@Data
public class Panel {
    private String renderer;
    private Boolean stack;
    private Legend legend;
    private Boolean error;
    private String title;
    private String type;
    private List seriesOverrides;
    private List<Target> targets;
    private Boolean points;
    private Boolean percentage;
    private Integer dashLength;
    private List links;
    private Integer id;
    private Boolean lines;
    private Integer spaceLength;
    private Boolean editable;
    private String nullPointMode;
    private Boolean steppedLine;
    private Integer fill;
    private Integer linewidth;
    private Boolean bars;
    private String timeFrom;
    private Boolean dashes;
    private String timeShift;
    private String datasource;
    private String aliasColors;
    private Integer pointradius;
    private GridPos gridPos;
}