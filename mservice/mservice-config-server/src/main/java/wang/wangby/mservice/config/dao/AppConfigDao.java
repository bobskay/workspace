package wang.wangby.mservice.config.dao;

import org.apache.ibatis.annotations.Mapper;
import wang.wangby.dao.BaseDao;
import wang.wangby.mservice.config.model.AppConfig;

import java.util.List;

@Mapper
public interface AppConfigDao extends BaseDao<AppConfig> {

	//获取全局配置,未设置application的
	List<AppConfig> getGloablConfig();
}
