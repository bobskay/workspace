package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class BeanFactory {
    private static BeanFactory INSTANCE;

    public static <T> T getBean(Class<T> clazz) {
        return INSTANCE.getBeanInternal(clazz);
    }

    public static void setFactory(BeanFactory beanFactory) {
        log.debug("初始化beanfactory:" + beanFactory);
        INSTANCE = beanFactory;
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        return INSTANCE.getBeansOfTypeInternal(clazz);
    }

    protected <T> Collection<T> getBeansOfTypeInternal(Class<T> clazz) {
        throw new RuntimeException("unimpl");
    }

    protected <T> T getBeanInternal(Class<T> clazz) {
        return ClassUtil.newInstance(clazz);
    }
}
