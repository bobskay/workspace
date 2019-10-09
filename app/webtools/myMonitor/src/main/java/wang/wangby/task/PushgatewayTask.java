package wang.wangby.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import wang.wangby.monitor.config.PushgatewayProperties;
import wang.wangby.utils.StrBuilder;
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
public class PushgatewayTask implements InitializingBean {

    @Autowired
    PushgatewayProperties pushgatewayProperties;
    @Value("${spring.application.name}")
    String applicationName;
    @Value("${eureka.instance.instance-id}")
    String instanceId;

    //在afterPropertiesSet里创建
    JobInfo sendJob;//发送任务
    CloseableHttpClient pushgatewayClient;//发送请求的httpclient
    String sendUrl;//数据最终发往的地址


    public String getMetrics(){
        StrBuilder sb=new StrBuilder();
        MonitorFacade.getMonitors().forEach((String monitorName, Monitor monitor)->{
            addMonitor(sb,monitorName,monitor);
        });
        return sb.toString();
    }

    public void addMonitor(StrBuilder sb,String monitorName, Monitor monitor){
        if(monitor instanceof TimeMonitor){
            appendTimeMontior(sb,monitorName,(TimeMonitor)monitor);
            return ;
        }
        monitor.getReslut().forEach((String name,Object result)->{
            String value=result+"";
            if(value.matches(StringUtil.REG_NUMBER)){
                appendTimeMontor(sb,monitorName,name,"totalCount",value+"");
            }else{
                log.warn("未知类型,无法放入Pushgateway:"+result);
            }
        });
    }

    private void appendTimeMontior(StrBuilder sb,String monitorName, TimeMonitor monitor) {
        monitor.getMap().forEach((String name, TimeStatist timeStatist)->{
            //总耗时
            appendTimeMontor(sb,monitorName,timeStatist.getName(),"totalConsume",timeStatist.getTotalConsume()+"");
            //总次数
            appendTimeMontor(sb,monitorName,timeStatist.getName(),"totalCount",timeStatist.getCount()+"");
            //每个记录,根据统计周期划分,5,10,20...,gt20
            for(int i=0;i<timeStatist.getAccuracyCount().length;i++){
                String label="gt"+timeStatist.getAccuracy()[timeStatist.getAccuracy().length-1];
                if(i<timeStatist.getAccuracy().length){
                    label=timeStatist.getAccuracy()[i]+"";
                }
                appendTimeMontor(sb,monitorName,timeStatist.getName(),label,timeStatist.getAccuracyCount()[i]+"");
            }
        });
    }

    //添加当监控项,最终格式为web_monitor{name="/index",label="5"}
    private void appendTimeMontor(StrBuilder sb,String category, String name, String label, String value){
        sb.append("web_monitor{");
        sb.append("category=\""+category+"\",");
        sb.append("name=\""+name+"\",");
        sb.append("label=\""+label+"\",");
        sb.append("app=\""+applicationName+"\"");
        sb.append("} "+value);
        sb.append("\n");
    }

    @Override
    //bean初始化完毕后创建发送的任务
    public void afterPropertiesSet() throws Exception {
        String base=pushgatewayProperties.getBaseUrl();
        if(!base.endsWith("/")){
            base+="/";
        }
        sendUrl=base+"metrics/job/javaWeb/instance/"+instanceId;
        log.debug("pushgateway最终发送地址:"+sendUrl);
        HttpConfig httpConfig=new HttpConfig();
        pushgatewayClient=HttpUtil.createClient(httpConfig);
        long interval=pushgatewayProperties.getSendIntervalSecond();
        sendJob= ScheduledFactory.newSchedule(this::sendMetrics,"sendToPushgatewayTask",interval,interval, TimeUnit.SECONDS);
    }

    public void sendMetrics() {
        String metrics=getMetrics();
        log.debug("准备发送数据:{}",metrics);
        HttpUriRequest post=HttpUtil.createRequest(sendUrl, HttpRequestMethod.POST,metrics,null);
        try (CloseableHttpResponse response = pushgatewayClient.execute(post)) {
            String result=HttpUtil.responseToString(response);
            if(StringUtil.isEmpty(result)){
                log.debug("pushgateway发送成功,返回空");
            }else{
                log.info("pushgateway发送返回:{}",result);
            }
        } catch (Exception e) {
            log.error("往pushgateway发送数据出错:{}",e.getMessage(),e);
        }
    }
}
