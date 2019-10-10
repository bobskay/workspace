package wang.wangby.mserviceadmin.model.vo;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;
import wang.wangby.annotation.Remark;

@Data
public class EurekaClientInfo {
    public enum Status{
        UP,DOWN
    }

    @Remark("服务实例信息")
    private  ServiceInstance serviceInstance;
    @Remark("状态up/down")
    private String status;
    @Remark("实例id")
    private String instanceId;
    @Remark("服务名称")
    private String serviceName;
}
