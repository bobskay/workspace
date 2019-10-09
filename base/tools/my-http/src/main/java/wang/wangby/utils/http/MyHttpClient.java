package wang.wangby.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import wang.wangby.utils.StringUtil;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class MyHttpClient {
    CloseableHttpClient httpClient;

    /**
     * @param  httpConfig http配置
     * */
    public MyHttpClient(HttpConfig httpConfig){
        this.httpClient=HttpUtil.createClient(httpConfig);
    }


    public  HttpUriRequest createRequest(String url, HttpRequestMethod requestMethod, String body){
        return createRequest(url,requestMethod,body,null);
    }

    /**
     * 创建reequest
     * @param url 请求的url不包括前缀
     * @param requestMethod 请求的方法类型
     * @param body 请求体内容,可以为空
     * @param config 当前请求的配置信息
     * */
    public  HttpUriRequest createRequest(String url, HttpRequestMethod requestMethod, String body, RequestConfig config){
       return HttpUtil.createRequest(url,requestMethod,body,config);
    }

    public  HttpUriRequest createRequest(String url, HttpRequestMethod requestMethod) {
       return  HttpUtil.createRequest(url,requestMethod,null,null);
    }


    public <T> T invoke(HttpUriRequest request, Function<CloseableHttpResponse, T> callback) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return callback.apply(response);
        }
    }

    public void call(HttpUriRequest request, Consumer<CloseableHttpResponse> consumer) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            consumer.accept(response);
        }
    }

    public String getString(HttpUriRequest request) throws IOException {
        Function<CloseableHttpResponse, String> callback=HttpUtil::responseToString;
        return invoke(request,callback);
    }
}
