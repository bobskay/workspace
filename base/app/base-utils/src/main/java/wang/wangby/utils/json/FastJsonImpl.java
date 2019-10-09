package wang.wangby.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.util.IdentityHashMap;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.JsonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FastJsonImpl extends JsonUtil {
    @Override
    public String toString(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T toBean(String str, Class<T> t) {
        return JSON.parseObject(str, t);
    }

    @Override
    public String toFormatString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
    }

    /**
     * 设置全局的js配置
     *
     * @param serializeConfig 序列化配置
     * @param parseConfig     反序列化配置
     * @param features        其它配置
     */
    synchronized public static void initGlobalConfig(Map<Class, ObjectSerializer> serializeConfig,
                                                     Map<Class, ObjectDeserializer> parseConfig,//
                                                     SerializerFeature... features
    ) {
        if (serializeConfig == null) {
            serializeConfig = defaultSerializeConfig();
        }
        if (parseConfig == null) {
            parseConfig = new HashMap<>();
        }

        for (Map.Entry<Class, ObjectSerializer> en : serializeConfig.entrySet()) {
            SerializeConfig.globalInstance.put(en.getKey(), en.getValue());
        }
        for (Map.Entry<Class, ObjectDeserializer> en : parseConfig.entrySet()) {
            ParserConfig.getGlobalInstance().putDeserializer(en.getKey(), en.getValue());
        }

        for (SerializerFeature f : features) {
            JSON.DEFAULT_GENERATE_FEATURE = JSON.DEFAULT_GENERATE_FEATURE | f.mask;
        }

        log.debug("初始化jsonUtil:serializeConfig=" + serializeConfig.size() + ",parseConfig=" + parseConfig.size());
        debugConfig(SerializeConfig.globalInstance);
        FastJsonImpl fast = new FastJsonImpl();
        JsonUtil.init(fast);
    }

    private static void debugConfig(SerializeConfig globalInstance) {
        if (!log.isTraceEnabled()) {
            return;
        }
        try {
            Field field = SerializeConfig.class.getDeclaredField("serializers");
            field.setAccessible(true);

            IdentityHashMap<Type, ObjectSerializer> map = (IdentityHashMap<Type, ObjectSerializer>) field.get(globalInstance);
            Field buckets = IdentityHashMap.class.getDeclaredField("buckets");
            buckets.setAccessible(true);
            Object[] entry = (Object[]) buckets.get(map);

            log.trace("json解析使用fastJson,默认配置信息:");
            for (Object o : Arrays.asList(entry)) {
                if (o != null) {
                    Field k = o.getClass().getDeclaredField("key");
                    Field v = o.getClass().getDeclaredField("value");
                    k.setAccessible(true);
                    v.setAccessible(true);
                    log.trace(k.get(o) + "=" + v.get(o));
                }
            }
        } catch (Exception ex) {
            AppendableSerializer x;
            log.info("打印配置json出错:" + ex.getMessage());
        }
    }


    //默认配置,将long输出为字符串,解决js精度丢失问题
    public static Map defaultSerializeConfig() {
        Map map = new HashMap();
        map.put(BigInteger.class, ToStringSerializer.instance);
        map.put(Long.class, ToStringSerializer.instance);
        map.put(Long.TYPE, ToStringSerializer.instance);
        return map;
    }
}
