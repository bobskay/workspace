package wang.wangby.mserviceadmin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"wang.wangby.mserviceadmin","wang.wangby.mservice.config"})
@MapperScan({"wang.wangby.mserviceadmin","wang.wangby.mservice.config"})
public class MserviceAdminAutoConfiguration {


}
