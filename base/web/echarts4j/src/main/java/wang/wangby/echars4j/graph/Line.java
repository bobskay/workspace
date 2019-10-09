package wang.wangby.echars4j.graph;

import wang.wangby.echars4j.Series;
import wang.wangby.echars4j.Series.SeriesType;

public class Line extends CoordinateGreaph{

	@Override
	public Series createSeries(String name,Long[] data){
		Series s=new Series();
		s.setType(SeriesType.line);
		s.setName(name);
		s.setData(data);
		s.setShowSymbol(false);
		s.setHoverAnimation(false);
		return s;
	}

}
