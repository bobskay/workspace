package wang.wangby.echars4j.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wang.wangby.echars4j.Axis;
import wang.wangby.echars4j.ChartData;
import wang.wangby.echars4j.ChartOption;
import wang.wangby.echars4j.Grid;
import wang.wangby.echars4j.Series;
import wang.wangby.echars4j.Series.SeriesType;

//连个坐标系的图
public class TwoCoordinate extends Graph{

	@Override
	public ChartOption create(ChartData data){
		ChartOption o=new ChartOption();
		Map saveAsImage=new HashMap();
		saveAsImage.put("saveAsImage",new Object());
		o.setToolbox("feature",saveAsImage);
		o.setTooltip("axis");
		o.setTitle(data.getTitle());
		o.setXAxis(createXAxis(data.getXaxis()));
		o.setYAxis(createYAxis());

		List<Series> sers=new ArrayList();
		Long[] d0=data.getDatas().get(0);
		Series s0=createSeries(data.getNames().get(0),d0,null);
		sers.add(s0);

		Long[] d1=data.getDatas().get(1);
		Series s1=createSeries(data.getNames().get(1),d1,1);
		sers.add(s1);

		o.setGrid(Grid.twoGrid(35));

		o.setLegend(data.getNames().toArray(new String[] {}));
		o.setSeries(sers);
		return o;
	}

	private Series createSeries(String name,Long[] data,Integer index){
		Series s=new Series();
		s.setType(SeriesType.line);
		s.setName(name);
		s.setData(data);
		s.setSymbolSize(8);
		s.setHoverAnimation(false);
		s.setXAxisIndex(index);
		s.setYAxisIndex(index);
		return s;
	}

	private List<Axis> createXAxis(String[] data){
		Axis first=Axis.category(data);
		Axis second=Axis.category(data);
		second.setGridIndex(1);
		second.setPosition("top");
		List list=new ArrayList();
		list.add(first);
		list.add(second);
		return list;
	}

	private List<Axis> createYAxis(){
		Axis first=Axis.value();
		Axis second=Axis.value();
		second.setGridIndex(1);
		second.setInverse(true);
		List list=new ArrayList();
		list.add(first);
		list.add(second);
		return list;
	}

}
