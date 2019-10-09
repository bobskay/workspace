package wang.wangby.exception;

//网络调用超时
public class TimeoutException extends Message{

    private String remote;//远程地址

    public TimeoutException(String remote,String message ) {
        super(message);
        this.remote=remote;
    }

    public String getMessage(){
        return "调用"+remote+"超时 "+super.getMessage();
    }
}
