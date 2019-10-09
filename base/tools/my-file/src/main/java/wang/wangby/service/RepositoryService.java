package wang.wangby.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.dao.MyRepository;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.model.dao.Pagination;
import wang.wangby.utils.IdWorker;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
/**
 * 主要处理某个model类的service,包含基础的增删改查功能
 * */
public class RepositoryService<T extends BaseModel> {

    @Autowired
    MyRepository myRepository;

    private Class<T> modelClass;
    private Function<T, Object> setPk;

    public RepositoryService() {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        if (type instanceof Class) {
            this.modelClass = (Class) type;
            for (Field field : modelClass.getDeclaredFields()) {
                Id idAnn = field.getAnnotation(Id.class);
                if (idAnn != null) {
                    initSetter(field);
                    log.debug("新增service:" + this.getClass().getName() + ",对应的model主键:" + ((Class) type).getName() + "." + field.getName());
                    return;
                }
            }
        }
        if (this.modelClass == null) {
            throw new RuntimeException("当前类未指定泛型:" + this.getClass());
        }
        if (this.setPk == null) {
            throw new RuntimeException("model类未设置主键:" + modelClass.getName());
        }

    }

    //初始化设置主键的方法
    private void initSetter(Field field) {
        field.setAccessible(true);
        if (field.getType() == String.class) {
            setPk = o -> {
                try {
                    Object pk = field.get(o);
                    if (pk == null) {
                        pk = IdWorker.nextString();
                        field.set(o, pk);
                    }
                    return pk;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("设置主键出错:" + o, e);
                }
            };
        } else {
            setPk = o -> {
                try {
                    Object pk = field.get(o);
                    if (pk == null) {
                        pk = IdWorker.nextLong();
                        field.set(o, pk);
                    }
                    return pk;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("设置主键出错:" + o, e);
                }
            };
        }

    }

    public List<T> getAll() throws Exception {
        return myRepository.getAll(modelClass);
    }

    //新增数据,如果主键为null,就自动创建
    public T insert(T t) throws Exception {
        Object id = setPk.apply(t);
        myRepository.insert(t);
        return t;
    }

    public T get(Long id) throws Exception {
        return (T)myRepository.get(modelClass,id);
    }

    public void update(T t) throws Exception {
        myRepository.update(t);
    }

    public int delete(Long id) throws Exception {
       return myRepository.delete(modelClass,id);
    }
}
