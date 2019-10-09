package wang.wangby.model.dao;

import wang.wangby.annotation.persistence.Id;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BaseModel implements Serializable,Cloneable {
	private transient Map<String,Object> ext=new HashMap();

	public Map getExt() {
		return ext;
	}

	public Long id(){
		return GetMethodHolder.get(this.getClass()).apply(this);
	}

	public static class GetMethodHolder{
		private static Map<Class,Function<BaseModel,Long>> functionMap=new ConcurrentHashMap<>();

		public static Function<BaseModel,Long> get(Class<? extends BaseModel> clazz){
			Function<BaseModel,Long> function=functionMap.get(clazz);
			if(function!=null){
				return function;
			}
			synchronized (clazz){
				if(function!=null){
					return function;
				}
			}

			for(Field f:clazz.getDeclaredFields()){
				Id id=f.getAnnotation(Id.class);
				if(id!=null){
					f.setAccessible(true);
					function= model->getId(f,model);
					functionMap.put(clazz,function);
					return function;
				}
			}
			throw new RuntimeException(clazz.getName()+"未设置主键");
		}

		public static Long getId(Field field,Object model){
			try {
				return (Long) field.get(model);
			} catch (IllegalAccessException e) {
				return null;
			}
		}
	}
}
