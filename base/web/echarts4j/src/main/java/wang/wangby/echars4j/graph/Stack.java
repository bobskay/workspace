package wang.wangby.echars4j.graph;

import wang.wangby.echars4j.Series;
import wang.wangby.echars4j.Series.SeriesType;
import wang.wangby.echars4j.Style;

public class Stack extends CoordinateGreaph{

	@Override
	public Series createSeries(String name,Long[] data){
		Series s=new Series();
		s.setAreaStyle(Style.normal());
		s.setType(SeriesType.line);
		s.setName(name);
		s.setData(data);
		s.setStack("总量");
		return s;
	}

}
