package wang.wangby.echars4j.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wang.wangby.echars4j.Axis;
import wang.wangby.echars4j.ChartData;
import wang.wangby.echars4j.ChartOption;
import wang.wangby.echars4j.Series;

//简单的横纵坐标图
abstract public class CoordinateGreaph extends Graph{
	public ChartOption create(ChartData data){
		ChartOption o=new ChartOption();
		Map saveAsImage=new HashMap();
		saveAsImage.put("saveAsImage",new Object());
		o.setToolbox("feature",saveAsImage);
		o.setTooltip("axis");
		o.setTitle(data.getTitle());
		o.setXAxis(Axis.category(data.getXaxis()));
		o.setYAxis(Axis.value());

		List<Series> sers=new ArrayList();
		for(int i=0;i<data.getDatas().size();i++){
			Object oo=data.getDatas().get(i);
			Long[] d=data.getDatas().get(i);
			Series s=createSeries(data.getNames().get(i),d);
			sers.add(s);
		}
		o.setLegend(data.getNames().toArray(new  String[] {}));
		o.setSeries(sers);
		return o;
	}

	abstract public Series createSeries(String name,Long[] data);

}
