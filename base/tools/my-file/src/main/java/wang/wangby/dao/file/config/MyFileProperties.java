package wang.wangby.dao.file.config;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
public class MyFileProperties {
    @Remark("数据保存的根目录")
    private String dataDir="/opt/data/myfile";
    @Remark("是否异步保存数据")
    private boolean async=false;
    @Remark("最多允许缓存多少条指令")
    private int maxQueueSize=10000;
    @Remark("刷盘间隔(s)")
    private  int periodSecond=60;
}
