package wang.wangby.echars4j.graph;

public enum GraphType{
	stack(new Stack()),//堆叠图
	line(new Line()),//折线图
	twoCoordinate(new TwoCoordinate());//两个坐标的图

	public Graph graph;

	GraphType(Graph graph){
		this.graph=graph;
	}
}
