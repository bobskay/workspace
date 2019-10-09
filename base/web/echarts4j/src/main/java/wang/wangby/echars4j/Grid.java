package wang.wangby.echars4j;

import java.util.ArrayList;
import java.util.List;

public class Grid{
	private String left;//3%
	private String right;
	private String buttom;
	private String height;
	private String top;
	private boolean containLabel;

	public String getLeft(){
		return left;
	}

	public void setLeft(String left){
		this.left=left;
	}

	public String getRight(){
		return right;
	}

	public void setRight(String right){
		this.right=right;
	}

	public String getButtom(){
		return buttom;
	}

	public void setButtom(String buttom){
		this.buttom=buttom;
	}

	public boolean isContainLabel(){
		return containLabel;
	}

	public void setContainLabel(boolean containLabel){
		this.containLabel=containLabel;
	}

	//创建两个grid
	//@param top 第一占多少百分比
	public static List<Grid> twoGrid(int top){
		Grid a=new Grid();
		a.setHeight(top+"%");

		Grid b=new Grid();
		b.setTop((top+20)+"%");
		b.setHeight(top+"%");

		List list=new ArrayList();
		list.add(a);
		list.add(b);
		return list;
	}

	public String getHeight(){
		return height;
	}

	public String getTop(){
		return top;
	}

	public void setHeight(String height){
		this.height=height;
	}

	public void setTop(String top){
		this.top=top;
	}

}
