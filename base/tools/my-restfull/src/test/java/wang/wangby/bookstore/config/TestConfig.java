package wang.wangby.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wang.wangby.restfull.RestDaoFactory;
import wang.wangby.restfull.RestMethodInterceptor;
import wang.wangby.restfull.RestfullDaoDefinitionRegistrar;
import wang.wangby.springboot.autoconfigure.json.JsonAutoConfiguration;
import wang.wangby.springboot.autoconfigure.velocity.VelocityAutoConfiguration;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.http.SpecificRemoteHttpClient;
import wang.wangby.utils.template.TemplateUtil;

@Configuration
//RestfullDaoDefinitionRegistrar自动注册标记了RestDao的接口
@Import( {VelocityAutoConfiguration.class, JsonAutoConfiguration.class, RestfullDaoDefinitionRegistrar.class})
@ComponentScan("wang.wangby.bookstore")//扫描的包
public class TestConfig {

    public class BookstoreApiDaoFactory extends RestDaoFactory {
        public BookstoreApiDaoFactory(RestMethodInterceptor restMethodInterceptor) {
            super(restMethodInterceptor);
        }

    }

    //创建仓库dao
    @Bean
    public BookstoreApiDaoFactory registryDaoFactory(TemplateUtil templateUtil, JsonUtil jsonUtil) {
        SpecificRemoteHttpClient client=new SpecificRemoteHttpClient("http://myos:8080",new HttpConfig());
        RestMethodInterceptor interceptor=new RestMethodInterceptor(client,templateUtil,jsonUtil);
        return new BookstoreApiDaoFactory(interceptor);
    }
}
