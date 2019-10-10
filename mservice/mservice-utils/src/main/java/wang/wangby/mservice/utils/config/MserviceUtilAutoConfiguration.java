package wang.wangby.mservice.utils.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import wang.wangby.mservice.utils.service.MyRule;

//mservice用到的工具类
@ComponentScan("wang.wangby.mservice.utils")
public class MserviceUtilAutoConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new MyRule();
    }

}
