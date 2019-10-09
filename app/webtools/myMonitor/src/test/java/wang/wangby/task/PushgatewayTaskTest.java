package wang.wangby.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.monitor.config.PushgatewayProperties;
import wang.wangby.test.TestBase;
import wang.wangby.utils.monitor.MonitorFacade;
import wang.wangby.utils.monitor.TimeMonitor;

@Slf4j
public class PushgatewayTaskTest extends TestBase {

    @Test
    public void getMetrics() {
        TimeMonitor monitor = MonitorFacade.timeMonitor("httpRequest", 5, 10, 20, 100);
        monitor.addRequest("/test1", 4);
        monitor.addRequest("/test1", 5);
        monitor.addRequest("/test1", 6);
        monitor.addRequest("/test1", 200);
        monitor.addRequest("/test2", 1);

        PushgatewayTask task = new PushgatewayTask();
        task.applicationName = "testApp";
        String metric = task.getMetrics();
        log.debug("最终发送数据\n" + metric);
    }


    @Test
    public void sendMetrics() throws Exception {
        PushgatewayProperties pushgatewayProperties=new PushgatewayProperties();
        pushgatewayProperties.setSendIntervalSecond(1L);
        pushgatewayProperties.setBaseUrl("http://myos:9091/");
        PushgatewayTask task = new PushgatewayTask(){
            public String getMetrics(){
                return "webapp_test 1\n";
            }
        };
        task.applicationName = "testApp";
        task.instanceId="testApp-1";
        task.pushgatewayProperties=pushgatewayProperties;
        task.afterPropertiesSet();
        log.debug("最终发送的url:"+task.sendUrl);
        Thread.sleep(5000L);
    }
}