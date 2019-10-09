package wang.wangby.annotation.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记在controller方法上,表明这个方法要在菜单里显示
 *
 * **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {

    //显示的菜单名称,如果为空就取remark注释里的值
    String value() default "";

    //菜单显示位置,如位置一样按名称排序
    int index() default 0;

}
