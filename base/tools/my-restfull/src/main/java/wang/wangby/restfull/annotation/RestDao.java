package wang.wangby.restfull.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;
import wang.wangby.restfull.RestDaoFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repository
public @interface RestDao {
    //创建工程的类
    @AliasFor("daoFactory")
    Class value() default RestDaoFactory.class;

    @AliasFor("value")
    Class daoFactory() default RestDaoFactory.class;
}
