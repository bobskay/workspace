package wang.wangby.dao.file;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.dao.MyRepository;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

@Slf4j
public class FileDao<T extends BaseModel> implements MyRepository<T> {

    DataSerializer dataSerializer;
    String root;//根目录
    public FileDao(DataSerializer dataSerializer,String root) throws IOException {
        this.dataSerializer=dataSerializer;
        if(!root.endsWith("/")){
            root=root+"/";
        }
        this.root =root;
        Path dir=Paths.get(root);
        if(!dir.toFile().exists()){
            Files.createDirectory(dir);
        }
    }

    @Override
    public int insert(T model) throws IOException {
        Long id=model.id();
        if(id==null){
            throw new RuntimeException("主键不能为空:"+model);
        }

        Path dir=Paths.get(root+model.getClass().getName());
        if(!dir.toFile().exists()){
            Files.createDirectory(dir);
        }

        byte[] bs=dataSerializer.toByte(model);
        Path path=getPath(model.getClass(),model.id());
        Files.createFile(path);
        Files.write(path,bs);
        return 1;
    }

    private Path getPath(Class clazz, Long id){
        String fileName= root +clazz.getName()+"/"+id;
        return Paths.get(fileName);
    }

    @Override
    public int delete(Class<T> clazz, Long id) throws IOException {
        Path path=getPath(clazz,id);
        if(Files.deleteIfExists(path)){
            return 1;
        }
        return 0;
    }

    @Override
    public int update(T model) throws IOException {
        Path path=getPath(model.getClass(),model.id());
        Files.delete(path);
        byte[] bs=dataSerializer.toByte(model);
        Files.write(path,bs);
        return 1;
    }

    @Override
    public void iterator(Function<T, Boolean> visitor) throws IOException {
        File file=new File(root);
        for(String className:file.list()){
           try{
               boolean isContinue=iterator(className,visitor);
               if(!isContinue){
                   return;
               }
           }catch (Exception ex){
               log.warn("遍历文件出错,className={}",className,ex);
           }
        }
    }

    //遍历某个类
    public boolean iterator(String clsName,Function<T, Boolean> visitor) throws Exception{
        Class clazz=null;
        try{
            clazz=Class.forName(clsName);
        }catch (ClassNotFoundException ex){
            log.warn("类不存在,跳过目录:"+root+clsName);
            return true;
        }

        File file=new File(root+clsName);
        if(!file.isDirectory()){
            return true;
        }
        for(String id:file.list()){
            if(StringUtil.isEmpty(id)){
                continue;
            }
            Path path=Paths.get(root+clsName+"/"+id);
            byte[] bs = Files.readAllBytes(path);
            T model=(T)dataSerializer.toBean(bs,clazz);
            if(model==null){
                log.warn("数据不正确:path="+path);
                continue;
            }
            if(!visitor.apply(model)){
                return false;
            }
        }
        return true;
    }

    @Override
    public T get(Class<T> clazz, Long id) throws IOException {
        Path path=getPath(clazz,id);
        if(!path.toFile().exists()){
            return null;
        }
        byte[] bs = Files.readAllBytes(path);
        Object o=dataSerializer.toBean(bs,clazz);
        return (T) o;
    }
}
