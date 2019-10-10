package wang.wangby.mservice.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.dao.BaseDao;
import wang.wangby.mservice.config.dao.AppConfigDao;
import wang.wangby.mservice.config.model.AppConfig;
import wang.wangby.service.DefaultService;

import java.util.List;

@Service
public class AppConfigService extends DefaultService<AppConfig> {
	@Autowired
    AppConfigDao appConfigDao;
	
	
	public List<AppConfig> findConfigByApplication(String application){
		List global=this.getGloablConfig();
		log.debug("全局配置个数:"+global.size());

		AppConfig config=new AppConfig();
		config.setApplication(application);;
		
		List apps= appConfigDao.select(config);
		global.addAll(apps);
		return global;
	}


	public List<AppConfig> getGloablConfig(){
		return appConfigDao.getGloablConfig();
	}


	@Override
	public BaseDao defaultDao() {
		return appConfigDao;
	}

	@Override
	public AppConfig newModel() {
		return new AppConfig();
	}
}
