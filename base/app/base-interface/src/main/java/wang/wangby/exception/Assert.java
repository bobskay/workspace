package wang.wangby.exception;

public class Assert{

	public static void error(String message){
		throw new RuntimeException(message);
	}
	
	public static void message(String message){
		throw new Message(message);
	}
}
