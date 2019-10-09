package wang.wangby.zk.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.model.dao.BaseModel;

@Data
public class ZkInfo extends BaseModel {

    @Id
    private Long id;

    @Remark("地址")
    private String address;

    //名称
    private String name;
}
