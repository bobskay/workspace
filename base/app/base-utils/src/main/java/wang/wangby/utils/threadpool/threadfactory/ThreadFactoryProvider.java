package wang.wangby.utils.threadpool.threadfactory;

import org.springframework.stereotype.Component;
import wang.wangby.utils.cache.Multiton;

import java.util.concurrent.ThreadFactory;

@Component
public class ThreadFactoryProvider extends Multiton<ThreadFactory> {

    @Override
    protected ThreadFactory getInner(String key) {
        return new CustomerNameThreadFactory(key);
    }
}
