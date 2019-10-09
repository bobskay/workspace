package wang.wangby.monitor.service;

import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.junit.Test;
import wang.wangby.monitor.model.CpuDetail;
import wang.wangby.utils.FixSizeList;

@Slf4j
public class CpuServiceTest {

    @Test
    public void getCpuInfo() throws SigarException {
        CpuService cupService=new CpuService();
        CpuDetail detail=cupService.getCpuInfo();

        log.debug("总user:"+detail.getCpuTotal().getUser()+"");
        log.debug("总idle:"+detail.getCpuTotal().getIdle()+"");
        log.debug("总sys:"+detail.getCpuTotal().getSys()+"");
        log.debug(detail.getCpuTotal().getUser()+detail.getCpuTotal().getIdle()+detail.getCpuTotal().getSys()+"");

        log.debug("===============================");
        for (int i = 0; i < detail.getSize(); i++) {// 不管是单块CPU还是多CPU都适用
            CpuInfo info = detail.getCpuInfoList().get(i);
            log.debug("第" + (i + 1) + "块CPU信息");
            log.debug("CPU的总量MHz:	" + info.getMhz());// CPU的总量MHz
            log.debug("CPU生产商:	" + info.getVendor());// 获得CPU的卖主，如：Intel
            log.debug("CPU类别:	" + info.getModel());// 获得CPU的类别，如：Celeron
            log.debug("CPU缓存数量:	" + info.getCacheSize());// 缓冲存储器数量
            //当前CPU的用户使用率、系统使用率、当前等待率、当前空闲率、总的使用率

            CpuPerc cpu=detail.getPercList().get(i);
            log.debug("CPU用户使用率:	" + CpuPerc.format(cpu.getUser()));// 用户使用率
            log.debug("CPU系统使用率:	" + CpuPerc.format(cpu.getSys()));// 系统使用率
            log.debug("CPU当前等待率:	" + CpuPerc.format(cpu.getWait()));// 当前等待率
            log.debug("CPU当前错误率:	" + CpuPerc.format(cpu.getNice()));//当前错误率
            log.debug("CPU当前空闲率:	" + CpuPerc.format(cpu.getIdle()));// 当前空闲率
            log.debug("CPU总的使用率:	" + CpuPerc.format(cpu.getCombined()));// 总的使用率
            log.debug("---------------------------------");
        }
    }

    @Test
    public void getChartData() throws Exception {
        CpuService cupService=new CpuService();
        cupService.afterPropertiesSet();
        Thread.sleep(1000*3);
        FixSizeList<CpuDetail> list= cupService.getChartData();
        log.debug(list.getData()+"");
    }

}