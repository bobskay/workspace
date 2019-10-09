package wang.wangby.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import wang.wangby.task.GrafanaDashboardTask;
import wang.wangby.task.PushgatewayTask;
import wang.wangby.page.config.IndexPageProperties;

import java.util.HashSet;

@ComponentScan("wang.wangby.monitor")
@Slf4j
public class MyMonitorAutoConfiguration {


    @ConfigurationProperties(prefix = "my.monitor.pushgateway")
    @Bean
    public PushgatewayProperties pushgatewayProperties(){
        return new PushgatewayProperties();
    }

    @ConfigurationProperties(prefix = "my.monitor.grafana")
    @Bean
    public GrafanaProperties grafanaProperties(){
        return new GrafanaProperties();
    }



    @ConditionalOnMissingBean
    @Bean
    public IndexPageProperties indexPageProperties(){
        return new IndexPageProperties();
    }

    //获得系统配置的IndexPageProperties,并添加echars先关的js
    @ConditionalOnBean(IndexPageProperties.class)
    @Bean
    public IndexPageProperties echarJs(IndexPageProperties indexPageProperties){
        if(indexPageProperties.getJs()==null){
            indexPageProperties.setJs(new HashSet<>());
        }
        indexPageProperties.getJs().add("/resources/js/echarts/echarts.min.js");
        return indexPageProperties;
    }

    @ConditionalOnProperty(name ="my.monitor.pushgateway.enabled", matchIfMissing = false)
    @Bean
    public PushgatewayTask pushgatewayTask(PushgatewayProperties pushgatewayProperties){
        log.debug("开启pushgateway发送任务,pushgateway地址:"+pushgatewayProperties.getBaseUrl());
        return new PushgatewayTask();
    }

    @ConditionalOnProperty(name ="my.monitor.grafana.enabled", matchIfMissing = false)
    @Bean
    public GrafanaDashboardTask grafanaDashboardTask(GrafanaProperties grafanaProperties){
        log.debug("开启pushgateway发送任务,pushgateway地址:"+grafanaProperties.getBaseUrl());
        return new GrafanaDashboardTask();
    }
}
