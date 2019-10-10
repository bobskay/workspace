package wang.wangby.mservice.config.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.model.dao.BaseModel;

@Data
@Table("m_appconfig")
public class AppConfig extends BaseModel {
	@Id
	@Remark
	private Long appConfigId;
	@Remark("项目名称")
	private String application;
	@Remark("配置分类")
	private String profile;
	@Remark("标签")
	private String varLabel;
	@Remark("配置项目的key")
	private String varKey;
	@Remark("配置值")
	private String varValue;
	
	
}
