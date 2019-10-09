package wang.wangby.autoconfigure.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import wang.wangby.codecretor.CodeCreator;
import wang.wangby.codecretor.CreatedCode;
import wang.wangby.dao.BaseDao;
import wang.wangby.dao.DbUtils;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.template.TemplateUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties({ DaoProperties.class })
@Slf4j
public class DaoAutoConfiguration {

	DaoProperties daoProperties;

	public DaoAutoConfiguration(DaoProperties daoProperties) {
		this.daoProperties = daoProperties;
	}

	@Bean
	public IdWorker idWorker() {
		return new IdWorker(daoProperties.getMachineNo());
	}

	@Bean
	public DbUtils dbUtils() {
		return new DbUtils();
	}

	@Bean
	public CodeCreator codeCreator(TemplateUtil templateUtil) {
		return new CodeCreator(templateUtil, daoProperties);
	}

	@Bean
	public CreatedCode list(CodeCreator codeCreator) throws Exception {
		List autoMapper = new ArrayList();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String base = codeCreator.getBasePackage().replace(".", "/");
		Resource[] allClass = resolver.getResources("classpath*:" + base + "/**/*.class");
		log.debug("开始扫描所有dao:" + base);
		for (int i = 0; i < allClass.length; i++) {
			Resource res = allClass[i];
			try {
				String xml = createMapperXml(res, codeCreator);
				if (xml == null) {
					continue;
				}
				ByteArrayResource bs = new ByteArrayResource(xml.getBytes(), "UTF-8") {
					public String toString() {
						return res + "";
					}
				};
				autoMapper.add(bs);
			} catch (Exception ex) {
				log.warn("自动生成" + res + "的映射出错:" + ex.getMessage(), ex);
			}
		}
		log.debug("自动生成映射文件:" + autoMapper.size());
		CreatedCode code = new CreatedCode();
		code.setMyBatisMapping(autoMapper);
		return code;

	}

	private String createMapperXml(Resource res, CodeCreator codeCreator) throws Exception {
		Class daoClass = getDaoClass(res, codeCreator);
		if (daoClass == null) {
			return null;
		}
		return codeCreator.getMapperXml(daoClass);
	}

	/**
	 * 获取和数据库对应的dao类 1.查找所有prefix包下的类 2.选出继承BaseDao的 3.配置了Mapper的
	 */
	private Class getDaoClass(Resource res, CodeCreator codeCreator) throws IOException, ClassNotFoundException {
		String prefix = codeCreator.getBasePackage().replace(".", "/") + "/";
		String url = res.getURL().getPath();
		int beginIndex = url.lastIndexOf(prefix);
		if (beginIndex == -1) {
			return null;
		}
		int end = url.indexOf(".", beginIndex);
		String className = url.substring(beginIndex, end).replace("/", ".");
		Class daoClass = Class.forName(className);
		if (daoClass == BaseDao.class) {
			return null;
		}

		if (!ClassUtil.isInstance(daoClass, BaseDao.class)) {
			return null;
		}
		Mapper mapper = AnnotationUtils.getAnnotation(daoClass, Mapper.class);
		if (mapper == null) {
			return null;
		}

		return daoClass;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(MybatisProperties properties, DataSource dataSource, CreatedCode code)
			throws Exception {
		log.debug("使用自动配置的SqlSessionFactory");
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		Resource[] resource = properties.resolveMapperLocations();
		for(Resource r: resource) {
			log.debug("找到自定义mapper文件:"+r);
		}
		List list = new ArrayList();
		list.addAll(Arrays.asList(resource));
		list.addAll(code.getMyBatisMapping());

		factory.setMapperLocations((Resource[]) list.toArray(new Resource[] {}));
		
		return factory.getObject();
	}

}
