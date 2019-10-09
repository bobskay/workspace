package wang.wangby.monitor.service;


import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import wang.wangby.monitor.model.CpuDetail;
import wang.wangby.utils.FixSizeList;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CpuService implements InitializingBean {

    FixSizeList<CpuDetail> fixSizeList;
    JobInfo cupChartJob;


    //获取cup信息和使用率
    public CpuDetail getCpuInfo() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = sigar.getCpuPercList();

        CpuDetail cpuDetail=new CpuDetail();
        cpuDetail.setCpuInfoList(Arrays.asList(infos));
        cpuDetail.setPercList(Arrays.asList(cpuList));
        cpuDetail.setSize(infos.length);
        cpuDetail.setCpuTotal(sigar.getCpuPerc());
        return cpuDetail;
    }

    public FixSizeList<CpuDetail> getChartData(){
        return fixSizeList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fixSizeList=new FixSizeList<>(60);
        cupChartJob=ScheduledFactory.newSchedule(()->{
            try {
                fixSizeList.add(getCpuInfo());
            } catch (SigarException e) {
                log.error("获取cpu信息出错:",e.getMessage(),e);
            }
        },"定时获取cpu信息",5,5, TimeUnit.SECONDS);
    }
}
