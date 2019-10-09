package wang.wangby.service;

import wang.wangby.dao.BaseDao;
import wang.wangby.model.Setter;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.utils.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;


abstract  public class DefaultService<T extends BaseModel> extends BaseService{

    public int updateField(String fieldName,Object newValue,Object oldValue,long id){
        return updateField(fieldName,newValue,oldValue, CollectionUtil.singleList(id));
    }

    //更新单个字段
    public int updateField(String fieldName, Object newValue, Object oldValue, List<Long> ids){
        Map map=new HashMap<>();
        map.put("fieldName",fieldName);
        map.put("newValue",newValue);
        map.put("oldValues",CollectionUtil.singleList(oldValue));
        map.put("ids",ids);
        return this.defaultDao().updateField(map);
    }

    //setId,设置id的方法
    public  Integer insert(T t, Setter<T,Long> setId){
        setId.set(t,newId());
        return defaultDao().insert(t);
    }


    public T get(Long id) {
       return (T) defaultDao().get(id);
    }

    public List<T> get(List<Long> ids){
        return defaultDao().getById(ids);
    }
    public int delete(Long[] id){
        return defaultDao().deleteById(id);
    }

    //获取全部
    public List<T> getAll(){
        return list(newModel());
    }

    public List<T> list(Consumer<T> consumer){
        T t=newModel();
        return this.defaultDao().list(t,consumer);
    }

    public T unique(Consumer<T> consumer){
        T t=newModel();
        List<T> list= this.defaultDao().list(t,consumer);
        if(list.size()==0){
            return null;
        }
        return list.get(0);
    }

    public List<T> list(T t){
        return this.defaultDao().list(t,null);
    }

    public int update(T t){
        return defaultDao().updateById(t);
    }

    abstract public BaseDao defaultDao();

    abstract public T newModel();
}
