package wang.wangby.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpMessage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import wang.wangby.monitor.config.GrafanaProperties;
import wang.wangby.monitor.model.grafana.DashboardCreater;
import wang.wangby.monitor.model.grafana.Panel;
import wang.wangby.monitor.model.grafana.Target;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.http.HttpRequestMethod;
import wang.wangby.utils.http.HttpUtil;
import wang.wangby.utils.monitor.Monitor;
import wang.wangby.utils.monitor.MonitorFacade;
import wang.wangby.utils.monitor.TimeMonitor;
import wang.wangby.utils.monitor.TimeStatist;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.util.concurrent.TimeUnit;

@Slf4j
public class GrafanaDashboardTask implements InitializingBean {

    @Autowired
    GrafanaProperties grafanaProperties;
    @Autowired
    JsonUtil jsonUtil;

    JobInfo sendJob;
    CloseableHttpClient grafanaClient;
    //grafana服务器地址
    String baseUrl;
    //创建dashboard的模板
    String dashboardCreateTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        baseUrl = grafanaProperties.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        HttpConfig httpConfig = new HttpConfig();
        grafanaClient = HttpUtil.createClient(httpConfig);

        dashboardCreateTemplate = getRequestTemplate();
        if(StringUtil.isEmpty(dashboardCreateTemplate)){
            throw new RuntimeException("无法获取创建请求先关的dashboard模,请求先在grafana里配置title=httpRequest.totalconsume,flag=request的dashboard");
        }

        long interval = grafanaProperties.getSendIntervalSecond();
        sendJob = ScheduledFactory.newSchedule(this::createDashboard, "grafanaDashboardTask", interval, interval, TimeUnit.SECONDS);
    }

    //获得创建request的模板
    String getRequestTemplate() throws Exception {
        String url = baseUrl + "api/search?query=httpRequest.totalconsume&flag=request";
        HttpUriRequest get = HttpUtil.createRequest(url, HttpRequestMethod.GET, null, null);
        addAuthorization(get);
        String uid = "";
        //获取UID
        try (CloseableHttpResponse response = grafanaClient.execute(get)) {
            String js = HttpUtil.responseToString(response);
            if(!js.trim().startsWith("[")){
                log.warn("通过{}获取到数据格式不正确:{}",url,js);
                return "";
            }
            JSONArray array = JSON.parseArray(js);
            if(array.size()==0){
                log.warn("通过{}获取到数据格式不正确:{}",url,js);
                return "";
            }
            JSONObject obj = array.getJSONObject(0);
            uid = obj.getString("uid");
        }

        if (StringUtil.isEmpty(uid)) {
            log.warn("通过{}获取到数据为空",url);
            return "";
        }

        //获取dashboard内容
        String dashboardUrl = baseUrl + "api/dashboards/uid/" + uid;
        HttpUriRequest dashboardGet = HttpUtil.createRequest(dashboardUrl, HttpRequestMethod.GET, null, null);
        addAuthorization(dashboardGet);
        try (CloseableHttpResponse response = grafanaClient.execute(dashboardGet)) {
            String js = HttpUtil.responseToString(response);
            DashboardCreater dc=jsonUtil.toBean(js,DashboardCreater.class);
            if(dc.getDashboard()==null||dc.getDashboard().getId()==null){
                log.warn("通过{}获取到数据格式不正确:{}",dashboardUrl,js);
                return "";
            }
            return js;
        }
    }

    public void createDashboard() {
        MonitorFacade.getMonitors().forEach((String monitorName, Monitor monitor) -> {
            //展示只自动添加timeMonitor
            if (!(monitor instanceof TimeMonitor)) {
                return;
            }
            TimeMonitor tm = (TimeMonitor) monitor;
            tm.getMap().values().iterator().forEachRemaining((TimeStatist ts) -> {
                try {
                    doCreateDashboard(ts);
                } catch (Exception ex) {
                    log.error("获取dashboard出错:{},跳过", ex.getMessage(), ts.getName(), ex);
                }
            });
        });
    }

    //判断视图是否存,如果不存在就自动创建
    void doCreateDashboard(TimeStatist ts) throws Exception {
        //获得名称查找监控图
        //http://myos:3000/api/search?query={name}&flag=request
        String url = baseUrl + "api/search?query=" + ts.getName() + "&flag=request";
        HttpUriRequest get = HttpUtil.createRequest(url, HttpRequestMethod.GET, null, null);
        addAuthorization(get);
        try (CloseableHttpResponse response = grafanaClient.execute(get)) {
            String js = HttpUtil.responseToString(response);
            JSONArray array = JSON.parseArray(js);
            if (array.size() > 0) {
                log.debug("dashboard已经存在跳过:{}", ts.getName());
                return;
            }
        }

        String createUrl=baseUrl + "api/dashboards/db";
        String createContent = getCreateContent(ts);
        log.debug("准备创建dashboard:{}",createContent);
        HttpUriRequest post=HttpUtil.createRequest(createUrl, HttpRequestMethod.POST,createContent,null);
        addAuthorization(post);
        try (CloseableHttpResponse response = grafanaClient.execute(post)) {
            String result=HttpUtil.responseToString(response);
            log.info("grafana创建结果:{}",result);
        } catch (Exception e) {
            log.error("grafana创建出错:{}",e.getMessage(),e);
        }
    }

    //生成创建dashboard的内容
    private String getCreateContent(TimeStatist ts) {
        DashboardCreater creater=jsonUtil.toBean(dashboardCreateTemplate,DashboardCreater.class);
        creater.getDashboard().setId(null);
        creater.getDashboard().setTitle(ts.getName());
        creater.setOverwrite(false);
        creater.getDashboard().setUid(null);
        for(Panel panel:creater.getDashboard().getPanels()){
            String oldName=panel.getTitle();
            panel.setTitle(ts.getName());
            for(Target target:panel.getTargets()){
                String expr=target.getExpr();
                expr=expr.replace(oldName, ts.getName());
                target.setExpr(expr);
            }
        }
        return jsonUtil.toString(creater);
    }

    private void addAuthorization(HttpMessage httpMessage){
        httpMessage.addHeader("Authorization","Bearer "+grafanaProperties.getToken());
    }
}
