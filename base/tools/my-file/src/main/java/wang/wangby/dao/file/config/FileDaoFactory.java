package wang.wangby.dao.file.config;

import wang.wangby.dao.MyRepository;
import wang.wangby.dao.file.AsynRepository;
import wang.wangby.dao.file.DataSerializer;
import wang.wangby.dao.file.FileDao;

public class FileDaoFactory {

    public static MyRepository fileDao(MyFileProperties myFileProperties) throws Exception {
        DataSerializer dataSerializer=new DataSerializer();
        FileDao fileDao=new FileDao(dataSerializer,myFileProperties.getDataDir());
        if(!myFileProperties.isAsync()){
            return fileDao;
        }
        AsynRepository repository=new AsynRepository();
        repository.init(fileDao,myFileProperties.getMaxQueueSize(),myFileProperties.getPeriodSecond());
        return repository;
    }
}
