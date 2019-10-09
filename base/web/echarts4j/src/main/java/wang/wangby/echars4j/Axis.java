package wang.wangby.echars4j;

public class Axis{
	public enum AxisType{
		category,value
	}

	private AxisType type;
	private boolean boundaryGap;
	private String[] data;
	private Integer gridIndex;
	private String position;
	private Boolean inverse;

	public static Axis category(String[] data){
		Axis a=new Axis();
		a.data=data;
		a.type=AxisType.category;
		a.boundaryGap=false;
		return a;
	}

	public String getType(){
		if(type==null){
			return null;
		}
		return type+"";
	}

	public void setType(AxisType type){
		this.type=type;
	}

	public boolean isBoundaryGap(){
		return boundaryGap;
	}

	public void setBoundaryGap(boolean boundaryGap){
		this.boundaryGap=boundaryGap;
	}

	public String[] getData(){
		return data;
	}

	public void setData(String[] data){
		this.data=data;
	}

	public static Axis value(){
		Axis a=new Axis();
		a.type=Axis.AxisType.value;
		return a;
	}

	public Integer getGridIndex(){
		return gridIndex;
	}

	public void setGridIndex(Integer gridIndex){
		this.gridIndex=gridIndex;
	}

	public String getPosition(){
		return position;
	}

	public void setPosition(String position){
		this.position=position;
	}

	public Boolean getInverse(){
		return inverse;
	}

	public void setInverse(Boolean inverse){
		this.inverse=inverse;
	}

}
