package wang.wangby.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.function.Function;

//只访问特定主机的http客户端
@Slf4j
public class SpecificRemoteHttpClient {
    String baseUrl;
    CloseableHttpClient httpClient;

    /**
     * @param  baseUrl 地址前缀,不包括/
     * @param  httpConfig http配置
     * */
    public SpecificRemoteHttpClient(String baseUrl,HttpConfig httpConfig){
        if(baseUrl.endsWith("/")){
            baseUrl=baseUrl.substring(0,baseUrl.length()-1);
        }
        this.baseUrl=baseUrl;
        this.httpClient=HttpUtil.createClient(httpConfig);
        log.debug("创建特定的主机的客户端,baseUrl="+baseUrl+", httpConfig="+httpConfig);
    }


    /**
     * 创建reequest
     * @param url 请求的url不包括前缀
     * @param requestMethod 请求的方法类型
     * @param body 请求体内容,可以为空
     * */
    public HttpUriRequest createRequest(String url, HttpRequestMethod requestMethod, String body){
        if(!url.startsWith("/")){
            url="/"+url;
        }
        url=baseUrl+url;
        return HttpUtil.createRequest(url,requestMethod,body,null);
    }

    //获得实际访问的url
    public String getUrl(String url){
        if(!url.startsWith("/")){
            url="/"+url;
        }
        url=baseUrl+url;
        return url;
    }

    public <T> T invoke(HttpUriRequest request, Function<CloseableHttpResponse, T> callback) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return callback.apply(response);
        }
    }

    public String getString(HttpUriRequest request) throws IOException {
        Function<CloseableHttpResponse, String> callback=HttpUtil::responseToString;
        return invoke(request,callback);
    }
}
