package wang.wangby.mservice.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.mservice.config.dao.AppConfigDao;
import wang.wangby.mservice.config.model.AppConfig;
import wang.wangby.service.BaseService;

import java.util.List;

@Service
public class AppConfigService extends BaseService {
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
	
	public void insert(AppConfig appConfig) {
		appConfig.setAppConfigId(newId());
		appConfigDao.insert(appConfig);
	}
}
