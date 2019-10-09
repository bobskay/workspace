package wang.wangby.echars4j;

import java.util.*;

import wang.wangby.echars4j.graph.GraphType;

/**
 * 返回页面的对象,java程序要做的就是构造这个对象
 *
 */
public class ChartOption{

	private Text title;//标题
	private Data legend;//图例
	private List<Axis> xAxis;//x轴
	private List<Axis> yAxis;//y轴
	private List<Series> series;
	private Map tooltip=new HashMap();
	private Map toolbox=new HashMap();
	private List<String> color;
	
	private List<Grid> grid;

	public void setTitle(String title){
		this.title=new Text(title);
	}
	
	public void setLegend(String[] data){
		Data d=new Data(data);
		this.legend=d;
	}

	public Data getLegend(){
		return legend;
	}

	public Text getTitle(){
		return title;
	}

	public List getXAxis(){
		return xAxis;
	}

	public void setXAxis(Axis xAxis){
		List list=new ArrayList();
		list.add(xAxis);
		this.xAxis=list;
	}

	public void setXAxis(List<Axis> xAxis){
		this.xAxis=xAxis;
	}

	public List<Axis> getYAxis(){
		return yAxis;
	}

	public void setYAxis(Axis yAxis){
		List list=new ArrayList();
		list.add(yAxis);
		this.yAxis=list;
	}

	public void setYAxis(List<Axis> yAxis){
		this.yAxis=yAxis;
	}

	public List<Series> getSeries(){
		return series;
	}

	public void setSeries(List<Series> series){
		this.series=series;
	}

	public Map getTooltip(){
		return tooltip;
	}

	public void setTooltip(String tooltip){
		this.tooltip.put("trigger",tooltip);
	}

	public Map getToolbox(){
		return toolbox;
	}

	public void setToolbox(String key,Object value){
		toolbox.put(key,value);
	}

	public static ChartOption create(GraphType graphType,ChartData data){
		ChartOption op= graphType.graph.create(data);
		String[] color= new String[]{"#00008B", "#00BFFF", "#61a0a8", "#d48265", "#91c7ae", "#749f83", "#ca8622", "#bda29a", "#6e7074", "#546570", "#c4ccd3"};
		op.color= Arrays.asList(color);
		return op;
	}

	public List<Grid> getGrid(){
		return grid;
	}

	public void setGrid(List<Grid> grid){
		this.grid=grid;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color = color;
	}

	
}
