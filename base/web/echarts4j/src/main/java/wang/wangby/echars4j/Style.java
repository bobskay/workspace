package wang.wangby.echars4j;

public class Style {
	private StyleContent normal;

	public static Style normal(){
		Style s=new Style();
		s.setNormal(new StyleContent());
		return s;
	}
	
	public StyleContent getNormal() {
		return normal;
	}

	public void setNormal(StyleContent normal) {
		this.normal = normal;
	}

	
	
	
}
