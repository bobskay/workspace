package wang.wangby.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Remark
public @interface Model{
	@AliasFor(annotation = Remark.class,attribute="value")
	String value();//显示用名称
	String tableName() default "";//对应数据库表名
}

