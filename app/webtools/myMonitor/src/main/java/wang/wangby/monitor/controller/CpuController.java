package wang.wangby.monitor.controller;

import org.hyperic.sigar.CpuPerc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.echars4j.ChartData;
import wang.wangby.echars4j.ChartOption;
import wang.wangby.echars4j.graph.GraphType;
import wang.wangby.model.request.Response;
import wang.wangby.monitor.model.CpuDetail;
import wang.wangby.monitor.service.CpuService;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.FixSizeList;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cpu")
public class CpuController extends BaseController {

    @Autowired
    CpuService cpuService;

    @Menu("cpu信息")
    @RequestMapping("index")
    public String index(){
        return $("index");
    }

    @RequestMapping("get")
    public Response<List<ChartOption>> get(){
        FixSizeList<CpuDetail> info=cpuService.getChartData();
        List<CpuDetail> data=info.getData();
        List<ChartOption> result=new ArrayList<>();
        if(data.size()==0){
            return respone(result);
        }
        for(int i=0;i<data.get(0).getSize();i++){
            addData(result,data,i);
        }
        return respone(result);
    }

    private void addData(List<ChartOption> chartOptions, List<CpuDetail> data,int index){
        ChartData chartData=new ChartData();
        List<Long> user=new ArrayList();
        List<Long> sys=new ArrayList();
        List<Long> idle=new ArrayList();
        for(int i=0;i<data.size();i++){
            CpuDetail detail=data.get(i);
            CpuPerc perc=detail.getPercList().get(index);
            user.add(0,(long)(perc.getUser()*100));
            sys.add(0,(long)(perc.getSys()*100));
            idle.add(0,(long)(perc.getIdle()*100));
        }
        chartData.addData("user", user.toArray(new Long[] {}));
        chartData.addData("sys", sys.toArray(new Long[] {}));
        chartData.addData("idle", idle.toArray(new Long[] {}));
        chartData.setXaxis(new String[]{});//x轴
        chartData.setTitle("第"+index+"颗cpu");
        ChartOption op=ChartOption.create(GraphType.stack, chartData);
        chartOptions.add(op);
    }
}
