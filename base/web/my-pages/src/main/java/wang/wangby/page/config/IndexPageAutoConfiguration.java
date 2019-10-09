package wang.wangby.page.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wang.wangby.page.controller.IndexController;

@Configuration
@Import(IndexController.class)
public class IndexPageAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "my.index")
    public IndexPageProperties indexPageProperties() {
        return new IndexPageProperties();
    }
}
