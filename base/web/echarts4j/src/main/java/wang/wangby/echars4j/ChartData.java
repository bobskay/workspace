package wang.wangby.echars4j;

import java.util.ArrayList;
import java.util.List;

/** 生成视图所需的数据 */
public class ChartData{

	//x轴现实的内容,监控图里通常是时间
	private String[] xaxis;
	//数据名称
	private List names=new ArrayList();
	//数据值
	private List<Long[]> datas=new ArrayList();
	//数据标题
	private String title;

	public String[] getXaxis(){
		return xaxis;
	}

	public void setXaxis(String[] xaxis){
		this.xaxis=xaxis;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}


	public List<String> getNames() {
		return names;
	}

	public void setNames(List names) {
		this.names = names;
	}

	public List<Long[]> getDatas(){
		return datas;
	}

	public void setDatas(List<Long[]> datas){
		this.datas=datas;
	}

	/**
	 * 设置数据
	 * @param name 统计的数据项名称
	 * @param data 数据值,个数要和横坐标一样xaxis
	 * 
	 * */
	public void addData(String name,Long[] data) {
		this.names.add(name);
		this.datas.add(data);
	}
}
