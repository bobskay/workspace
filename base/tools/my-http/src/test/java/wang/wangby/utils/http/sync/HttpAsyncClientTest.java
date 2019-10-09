package wang.wangby.utils.http.sync;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.nio.reactor.IOReactorException;
import org.junit.Test;
import wang.wangby.test.beanfactory.TestBeanFactory;
import wang.wangby.utils.http.HttpConfig;

import java.util.concurrent.ExecutionException;

@Slf4j
public class HttpAsyncClientTest {

    @Test
    public void doGet() throws IOReactorException, ExecutionException, InterruptedException {
        HttpAsyncClientProvider provider = TestBeanFactory.get(HttpAsyncClientProvider.class);
        HttpAsyncClient client = provider.newClient(new HttpConfig());
        HttpResponse response = client.doGet("https://github.com/");
        log.debug(response+"");
    }
}