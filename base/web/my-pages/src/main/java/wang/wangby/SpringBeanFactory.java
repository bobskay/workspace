package wang.wangby;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wang.wangby.utils.BeanFactory;

import java.util.Collection;
import java.util.Map;

public class SpringBeanFactory extends BeanFactory implements  ApplicationContextAware {
    private ApplicationContext applicationContext;

    public <T> T getBeanInternal(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public <T> Collection<T> getBeansOfTypeInternal(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        return beans.values();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
