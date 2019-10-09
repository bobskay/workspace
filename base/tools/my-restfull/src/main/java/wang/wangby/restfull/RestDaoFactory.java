package wang.wangby.restfull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import wang.wangby.restfull.annotation.RestDao;

@Slf4j
public class RestDaoFactory implements InstantiationAwareBeanPostProcessor {

    RestMethodInterceptor restMethodInterceptor;

    public RestDaoFactory(RestMethodInterceptor restMethodInterceptor) {
        this.restMethodInterceptor = restMethodInterceptor;
    }

    //代理所有标记了RestDao的接口
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        RestDao restDao = AnnotationUtils.getAnnotation(beanClass, RestDao.class);
        if (restDao==null) {
            return null;
        }
        Class daofactroy=restDao.daoFactory();
        if(!this.getClass().getName().equals(daofactroy.getName())){
            return  null;
        }
        log.debug("找到bean{}->{},restDao={}" , beanName ,beanClass.getName(),restDao);
        ProxyFactory pf = new ProxyFactory();
        pf.setInterfaces(beanClass);
        pf.addAdvice(restMethodInterceptor);
        return pf.getProxy();
    }

}
