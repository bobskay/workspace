package wang.wangby.testcase.service;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.Integers;
import wang.wangby.utils.threadpool.ThreadPool;

import static org.junit.Assert.*;

@Slf4j
public class CpuTestServiceTest {

    public static void main(String[] args) throws InterruptedException {
        CpuTestService service = new CpuTestService();
        service.start(4);
        Integers i = new Integers(0);
        new Thread(() -> {
            while (true) {
                try {
                    ThreadPool.PoolInfo currentInfo = service.getInfo().getPoolInfo();
                    log.debug("第{}秒,活动线程数{},poolSize{},queue{}", i.incrementAndGet(), currentInfo.getActiveCount(),
                            currentInfo.getPoolSize(), currentInfo.getQueueSize());
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100000);
    }

}