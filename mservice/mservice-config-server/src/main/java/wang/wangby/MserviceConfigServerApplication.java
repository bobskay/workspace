package wang.wangby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.autoconfigure.dao.DaoAutoConfiguration;

@SpringBootApplication
@Import(DaoAutoConfiguration.class)
@EnableConfigServer
public class MserviceConfigServerApplication {
	public static void main(String[] args) {
		ApplicationContext applicationContext= SpringApplication.run(MserviceConfigServerApplication.class, args);


	}
	
}
