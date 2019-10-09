package wang.wangby.springboot.autoconfigure.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wang.wangby.SpringBeanFactory;
import wang.wangby.utils.BeanFactory;

@Configuration
public class SpringrAutoConfiguration {

    @Bean
    public SpringBeanFactory beanFactory(){
        SpringBeanFactory beanFactory=new SpringBeanFactory();
        BeanFactory.setFactory(beanFactory);
        return beanFactory;
    }
}
