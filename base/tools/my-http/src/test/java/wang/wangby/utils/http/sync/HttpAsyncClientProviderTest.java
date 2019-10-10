package wang.wangby.utils.http.sync;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.nio.reactor.IOReactorException;
import org.junit.Test;
import wang.wangby.test.beanfactory.TestBeanFactory;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.http.HttpRequestMethod;
import wang.wangby.utils.http.HttpUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpAsyncClientProviderTest {

    @Test
    public void test() throws IOReactorException, ExecutionException, InterruptedException {
        HttpAsyncClientProvider provider = TestBeanFactory.get(HttpAsyncClientProvider.class);
        HttpAsyncClient client = provider.newClient(new HttpConfig());
        HttpGet get = (HttpGet) HttpUtil.createRequest("https://github.com/", HttpRequestMethod.GET, null,null);
        CountDownLatch latch=new CountDownLatch(1);
        HttpResponseCallback callback=new HttpResponseCallback(response -> {
            String str=HttpUtil.responseToString(response);
            log.debug(str);
            latch.countDown();
            return null;
        });
        client.execute(get, callback);
        latch.await(1, TimeUnit.SECONDS);

    }
}