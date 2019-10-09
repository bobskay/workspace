package wang.wangby.restfull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import wang.wangby.restfull.annotation.RestDao;
import wang.wangby.utils.StringUtil;

import java.io.IOException;

@Slf4j
//自动扫描带有RestDao的类并注册
public class RestfullDaoDefinitionRegistrar implements ImportBeanDefinitionRegistrar{
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    //TODO 要扫描的包,默认是当前类所在的包上级,静态静态变量,如果要修改在启动类里直接设置
    //eg: RestFullDaoFactory.SACN_PACKAGE="wang.wangby"
    public static String SACN_PACKAGE = RestfullDaoDefinitionRegistrar.class.getPackage().getName();
    //标记了这个annotion的类或接口自动扫描
    public  static String ANNOTATION_NAME= RestDao.class.getName();

    @Override
    /**
     * 扫描所有带RestDao标签的类,并注册,
     * 扫描代码抄的org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.scanCandidateComponents
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        String scanPkg=SACN_PACKAGE;
        //如果报名和当前包一样,就取上一级
        if(scanPkg.equals( RestfullDaoDefinitionRegistrar.class.getPackage().getName())){
            scanPkg= StringUtil.getLastBefore(scanPkg,".");
        }
        log.debug("准备扫描:"+scanPkg);
        for (Resource resource : getResources(scanPkg)) {
           try{
               register(registry, metadataReaderFactory, resource);
           }catch (Exception ex){
               log.warn("自动注册失败忽略resource="+resource,ex);
            }
        }
    }

    //注册某个类
    private void register(BeanDefinitionRegistry registry, CachingMetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
        sbd.setResource(resource);
        sbd.setSource(resource);
        if (sbd.getMetadata().hasAnnotation(ANNOTATION_NAME)) {
            RootBeanDefinition definition = new RootBeanDefinition(sbd.getBeanClassName());
            registry.registerBeanDefinition(sbd.getBeanClassName(), definition);
            log.debug("自动注册bean:" + sbd.getBeanClassName());
        }
    }

    //获取所有class
    private Resource[] getResources(String scanPkg){
       try{
           String pkg=ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(scanPkg));
           String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                   pkg + '/' + DEFAULT_RESOURCE_PATTERN;
           Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
           return resources;
       }catch (Exception ex){
           log.error("查找class文件出错,sacnPackage="+scanPkg,ex);
           return new Resource[0];
       }
    }
}
