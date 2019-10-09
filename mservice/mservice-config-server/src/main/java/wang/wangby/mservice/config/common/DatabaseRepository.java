package wang.wangby.mservice.config.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;
import wang.wangby.mservice.config.model.AppConfig;
import wang.wangby.mservice.config.service.AppConfigService;
import wang.wangby.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseRepository implements EnvironmentRepository {
	public static final Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

	@Autowired
    AppConfigService appConfigService;

	@Override
	public Environment findOne(String application, String profile, String label) {
		log.info("开始查找配置:" + application + "." + profile + "." + label);

		List<AppConfig> configs = appConfigService.findConfigByApplication(application);

		Environment env = new Environment(application, profile);
		Map map = new HashMap();
		for (AppConfig config : configs) {
			if (isMatch(config, profile, label)) {
				if (StringUtil.isEmpty(config.getVarKey())) {
					log.warn(
							"错误配置项:" + config.getAppConfigId() + ":" + config.getVarKey() + "=" + config.getVarValue());
					continue;
				}
				map.put(config.getVarKey(), config.getVarValue());
			}
		}
		map.put("spring.application.name", application);
		env.add(new PropertySource(profile, map));

		return env;
	}

	// 判断属性是否符合,当判断的其中一方为空就认为相等
	public boolean isMatch(AppConfig config, String profile, String label) {
		if (!StringUtil.isMatchOrEmpty(profile,config.getProfile())) {
			return false;
		}
		return StringUtil.isMatchOrEmpty(label,config.getVarLabel());
	}
}
