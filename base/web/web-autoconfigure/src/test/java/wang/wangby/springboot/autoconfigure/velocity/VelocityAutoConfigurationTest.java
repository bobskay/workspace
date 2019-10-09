package wang.wangby.springboot.autoconfigure.velocity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

@Slf4j
public class VelocityAutoConfigurationTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(VelocityAutoConfiguration.class);
        for (String name : context.getBeanDefinitionNames()) {
            log.debug(name);
        }


    }
}