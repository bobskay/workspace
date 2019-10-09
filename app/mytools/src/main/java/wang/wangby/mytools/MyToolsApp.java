package wang.wangby.mytools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.monitor.config.MyMonitorAutoConfiguration;
import wang.wangby.page.config.IndexPageAutoConfiguration;
import wang.wangby.testcase.config.MyTestCaseAutoConfiguration;
import wang.wangby.mytools.config.WebSocketConfig;

@SpringBootApplication
//使用默认首页
@Import({IndexPageAutoConfiguration.class,
        MyMonitorAutoConfiguration.class,
        MyTestCaseAutoConfiguration.class,
        WebSocketConfig.class
})
@Slf4j
public class MyToolsApp {

    public static void main(String args[]){
        ConfigurableApplicationContext context= SpringApplication.run(MyToolsApp.class, args);
        log.debug("加载类个数:"+context.getBeanDefinitionNames().length);
    }

}