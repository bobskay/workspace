package wang.wangby.echars4j;

public class Series{
	public enum SeriesType{
		line;
	}

	private String name;
	private SeriesType type;
	private String stack;
	private Style areaStyle;
	private Long[] data;
	private Boolean showSymbol;
	private Boolean hoverAnimation;
	private Integer symbolSize;
	private Integer XAxisIndex;
	private Integer YAxisIndex;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getType(){
		if(type==null){
			return null;
		}
		return type+"";
	}

	public void setType(SeriesType type){
		this.type=type;
	}

	public String getStack(){
		return stack;
	}

	public void setStack(String stack){
		this.stack=stack;
	}

	public Style getAreaStyle(){
		return areaStyle;
	}

	public void setAreaStyle(Style areaStyle){
		this.areaStyle=areaStyle;
	}

	public Long[] getData(){
		return data;
	}

	public void setData(Long[] data){
		this.data=data;
	}

	public Boolean getShowSymbol(){
		return showSymbol;
	}

	public Boolean getHoverAnimation(){
		return hoverAnimation;
	}

	public void setShowSymbol(Boolean showSymbol){
		this.showSymbol=showSymbol;
	}

	public void setHoverAnimation(Boolean hoverAnimation){
		this.hoverAnimation=hoverAnimation;
	}

	public Integer getSymbolSize(){
		return symbolSize;
	}

	public void setSymbolSize(Integer symbolSize){
		this.symbolSize=symbolSize;
	}

	public Integer getXAxisIndex(){
		return XAxisIndex;
	}

	public void setXAxisIndex(Integer xAxisIndex){
		XAxisIndex=xAxisIndex;
	}

	public Integer getYAxisIndex(){
		return YAxisIndex;
	}

	public void setYAxisIndex(Integer yAxisIndex){
		YAxisIndex=yAxisIndex;
	}

}
