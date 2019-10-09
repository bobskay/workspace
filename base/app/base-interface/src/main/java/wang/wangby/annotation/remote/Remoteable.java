package wang.wangby.annotation.remote;

import wang.wangby.annotation.Remark;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//标识一个方法是否可以被远程调用
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public  @interface  Remoteable{

}
