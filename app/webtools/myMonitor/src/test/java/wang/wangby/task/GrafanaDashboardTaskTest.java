package wang.wangby.task;

import org.junit.Before;
import org.junit.Test;
import wang.wangby.monitor.config.GrafanaProperties;
import wang.wangby.test.TestBase;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.http.HttpUtil;
import wang.wangby.utils.monitor.TimeStatist;

import static org.junit.Assert.*;

public class GrafanaDashboardTaskTest extends TestBase {

    GrafanaDashboardTask task;

    @Before
    public void init() throws Exception {
        GrafanaProperties p=new GrafanaProperties();
        p.setBaseUrl("http://myos:3000");
        p.setSendIntervalSecond(30);

        task=new GrafanaDashboardTask();
        task.grafanaProperties=p;
        task.jsonUtil=jsonUtil;

    }

    @Test
    public void doCreateDashboard() throws Exception {
        task.afterPropertiesSet();
        //已存在的跳过
        TimeStatist statist=new TimeStatist("httpRequest.totalconsume",new long[]{1});
        task.doCreateDashboard(statist);

        //不存在的创建
        TimeStatist indexStatist=new TimeStatist("/treeView",new long[]{1});
        task.doCreateDashboard(indexStatist);
    }

    @Test
    public void getRequestTemplate() throws Exception {
        GrafanaDashboardTask task=new GrafanaDashboardTask();
        task.grafanaProperties=new GrafanaProperties();
        task.baseUrl="http://myos:3000/";
        task.grafanaClient = HttpUtil.createClient(new HttpConfig());
        task.jsonUtil=jsonUtil;
        String text=task.getRequestTemplate();
        assertNotEmpty(text);
    }

}