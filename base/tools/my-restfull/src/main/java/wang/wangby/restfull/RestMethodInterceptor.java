package wang.wangby.restfull;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.http.HttpRequestMethod;
import wang.wangby.utils.http.SpecificRemoteHttpClient;
import wang.wangby.utils.template.TemplateUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RestMethodInterceptor implements MethodInterceptor {
    SpecificRemoteHttpClient httpClient;
    TemplateUtil templateUtil;
    JsonUtil jsonUtil;
    //缓存方法的rest配置信息
    private Map<Method,RestMethodInfo> restMethodInfo=new ConcurrentHashMap<>();

    public RestMethodInterceptor(SpecificRemoteHttpClient httpClient, TemplateUtil templateUtil,JsonUtil jsonUtil) {
        this.httpClient = httpClient;
        this.templateUtil=templateUtil;
        this.jsonUtil=jsonUtil;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        RestMethodInfo info = getInfo(method);
        //跳过未配置requestMapping的
        if (info.getMethod()==null) {
            return invocation.proceed();
        }

        String result=this.invokeRemote(info,invocation.getArguments());
        Type returnType=method.getGenericReturnType();
        return JSON.parseObject(result, returnType);
    }

    //生成boday,只有最后一个参数不是String的时候才生成
    private String createBody(Object[] args){
        if(args.length==0){
            return null;
        }
        Object last=args[args.length-1];
        if(last instanceof String){
            return null;
        }
        return jsonUtil.toString(last);
    }


    RestMethodInfo getInfo(Method method) {
        RestMethodInfo info=restMethodInfo.get(method);
        if(info!=null){
            return info;
        }
        synchronized (method){
            info=restMethodInfo.get(method);
            if(info!=null){
                return info;
            }
        }

        RestMethodInfo methodInfo=createMethodInfo(method);
        restMethodInfo.put(method,methodInfo);
        return methodInfo;
    }

    private RestMethodInfo createMethodInfo(Method method) {
        RestMethodInfo  info=new RestMethodInfo();
        info.setName(method.getName());
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method,RequestMapping.class);
        if (requestMapping == null) {
            return info;
        }
        //paramName
        info.setParameterNames(getParamName(method));
        //method
        HttpRequestMethod httpRequestMethod=toHttpRequestMethod(requestMapping.method());
        info.setMethod(httpRequestMethod);
        //url
        String[] value= requestMapping.value();
        if(value.length!=1){
            log.error("requestMapping配置的地址不是一个,requestMapping={},value={}",requestMapping, StringUtil.toString(value));
            return info;
        }
        info.setUrl(value[0]);
        if(info.getUrl().indexOf("$")!=-1){
            info.setUrlContentVariable(true);
        }

        //是否包含请求体
        info.setHasBody(hasBoday(method,httpRequestMethod));

        log.debug("开始代理rest方法,restMethodInfo={}",info);
        return info;
    }

    /**
     * 是否包含请求体,规则
     * 1. 方法是post或者put
     * 2. 最后1个参数类型不是string或者原生类型
     *
     * */
    private boolean hasBoday(Method method, HttpRequestMethod httpRequestMethod) {
        if(httpRequestMethod==HttpRequestMethod.POST||httpRequestMethod==HttpRequestMethod.PUT){
            Parameter[] ps=method.getParameters();
            if(ps.length!=0){
                Parameter last=ps[ps.length-1];
                if(last.getType().isAssignableFrom(String.class)){
                   return false;
                }
                if(ClassUtil.isNumber(last.getType())){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    //TODO 获取方法参数名称,还没搞定,目前用args0,args1
    private List<String> getParamName(Method method) {
        Parameter[]  parameters= method.getParameters();
        List list=new ArrayList(parameters.length);
        for(Parameter s:parameters){
            list.add(s.getName());
        }
        return list;
    }

    private HttpRequestMethod toHttpRequestMethod(RequestMethod[] methods){
        //默认get
        if(methods==null||methods.length==0){
            return HttpRequestMethod.GET;
        }
        //如果有就用post
        for(RequestMethod method:methods){
            if(method==RequestMethod.POST){
                return HttpRequestMethod.POST;
            }
        }
        //用配置的第一个
        return HttpRequestMethod.valueOf(methods[0].toString());
    }

    //调用远程
    String invokeRemote(RestMethodInfo info,Object[] args) throws IOException {
        String url=info.getUrl();
        if(info.isUrlContentVariable()){
            Map paramMap=new HashMap();
            for(int i=0;i<args.length;i++){
                paramMap.put(info.getParameterNames().get(i),args[i]);
            }
            url=templateUtil.parseText(url,paramMap);
        }
        String body=null;
        if(info.isHasBody()){
            body=jsonUtil.toString(args[args.length-1]);
        }

        HttpUriRequest request = httpClient.createRequest(url,info.getMethod(),body);
        String response =httpClient.getString(request);
        if(log.isDebugEnabled()){
            String resp=StringUtil.subString(response,1000);
            log.debug("请结果,url={} | response={}",httpClient.getUrl(url),resp);
        }
        return response;
    }


}
