package wang.wangby.zk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.dao.file.FileDao;
import wang.wangby.exception.Message;
import wang.wangby.utils.IdWorker;
import wang.wangby.zk.model.ZkInfo;

import java.io.IOException;
import java.util.List;

@Service
public class ZkInfoService {

    @Autowired
    FileDao fileDao;

    public void insert(ZkInfo zkInfo) throws Exception {
        for(ZkInfo info:this.getAll()){
            if(info.getAddress().equalsIgnoreCase(zkInfo.getAddress())){
                throw  new Message("配置已经存在:"+zkInfo.getAddress());
            }
        }
        zkInfo.setId(IdWorker.nextLong());
        fileDao.insert(zkInfo);
    }

    public List<ZkInfo> getAll() throws Exception {
       return fileDao.getAll(ZkInfo.class);
    }

    public ZkInfo get(Long id) throws IOException {
        return (ZkInfo) fileDao.get(ZkInfo.class,id);
    }

    public void delete(long id) throws IOException {
        fileDao.delete(ZkInfo.class,id);
    }
}
