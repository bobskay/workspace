package wang.wangby.utils.http;

import lombok.SneakyThrows;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;
import wang.wangby.utils.FileUtil;
import wang.wangby.utils.StringUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HttpUtil {
    //默认的http连接客户端
    private static CloseableHttpClient DEFAULT_CLIENT = createClient(new HttpConfig());


    public static CloseableHttpClient createClient(HttpConfig httpConfig) {
        // 设置协议http和https对应的处理socket链接工厂的对象
        RegistryBuilder bulder = RegistryBuilder.<ConnectionSocketFactory>create();
        bulder.register("http", PlainConnectionSocketFactory.INSTANCE);
        if (httpConfig.getSslKeyStorePath()!=null) {
            SSLContext sslcontext =SSLContextFactory.create(httpConfig.getSslKeyStorePath(),httpConfig.getSsKeyStorepass());
            bulder.register("https", new SSLConnectionSocketFactory(sslcontext));
        }else{
            bulder .register("https", SSLConnectionSocketFactory.getSocketFactory());
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = bulder.build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(httpConfig.getMaxTotal());
        connManager.setDefaultMaxPerRoute(httpConfig.getDefaultMaxPerRoute());
        if (httpConfig.getMaxPerRoute() != null) {
            httpConfig.getMaxPerRoute().forEach((route, count) -> {
                connManager.setMaxPerRoute(route, count);
            });
        }

        if (httpConfig.getSocketConfig() != null) {
            httpConfig.getSocketConfig().forEach((host, config) -> {
                connManager.setSocketConfig(host, config);
            });
        }

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(httpConfig.getConnectTimeout())         //连接超时时间
                .setSocketTimeout(httpConfig.getSocketTimeout())          //读超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(httpConfig.getConnectionRequestTimeout())    //从池中获取连接超时时间
                .build();

        HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(httpConfig.getRetryCount(), httpConfig.isRetryEnabled());
        return HttpClients.custom()
                .setConnectionManager(connManager)             //连接管理器
                //.setProxy(new HttpHost("myproxy", 8080))       //设置代理
                .setDefaultRequestConfig(defaultRequestConfig) //默认请求配置
                .setRetryHandler(requestRetryHandler)               //重试策略
                .build();
    }

    /**
     * 通过get方式获取内容
     *
     * @param url           请求地址
     * @param callback      将CloseableHttpResponse转为返回内容
     * @param httpClient    所使用的httpclinet客户端
     * @param requestConfig 请求配置,如果为null,就用httpClient里配的
     */
    public static <T> T get(String url, Function<CloseableHttpResponse, T> callback, CloseableHttpClient httpClient, RequestConfig requestConfig) throws IOException {
        HttpUriRequest httpget=createRequest(url,HttpRequestMethod.GET,"",requestConfig);
        try (CloseableHttpResponse response = httpClient.execute(httpget)) {
            return callback.apply(response);
        }
    }

    public static String get(String url) throws IOException {
        return get(url, HttpUtil::responseToString, DEFAULT_CLIENT, null);
    }

    //将返回内容转为string
    public static String responseToString(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        try {
            return EntityUtils.toString(entity, Consts.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("将response转为String出错", e);
        }
    }

    public static <T> T postBody(String url, String body, Function<CloseableHttpResponse, T> callback, CloseableHttpClient httpClient) throws IOException {
        HttpUriRequest post=createRequest(url,HttpRequestMethod.POST,body,null);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return callback.apply(response);
        }
    }

    public static <T> T put(String url, String body, Function<CloseableHttpResponse, T> callback, CloseableHttpClient httpClient) throws IOException {
        HttpUriRequest put=createRequest(url,HttpRequestMethod.PUT,body,null);
        try (CloseableHttpResponse response = httpClient.execute(put)) {
            return callback.apply(response);
        }
    }

    public static <T> T delete(String url, Function<CloseableHttpResponse, T> callback, CloseableHttpClient httpClient) throws IOException {
        HttpUriRequest delete=createRequest(url,HttpRequestMethod.PUT.DELETE,null,null);
        try (CloseableHttpResponse response = httpClient.execute(delete)) {
            return callback.apply(response);
        }
    }

    private static HttpUriRequest emptyRequest(String url, HttpRequestMethod requestMethod) {
        switch (requestMethod){
            case GET:
                return  new HttpGet(url);
            case PUT:
                return new HttpPut(url);
            case HEAD:
                return new HttpHead(url);
            case POST:
                return new HttpPost(url);
            case PATCH:
                return new HttpPatch(url);
            case TRACE:
                return new HttpTrace(url);
            case DELETE:
                return new HttpDelete(url);
            case OPTIONS:
                return new HttpOptions(url);
        }
        throw new RuntimeException("未实现的requestMethod:"+requestMethod);
    }


    /**
     * 创建reequest
     * @param url 请求的url不包括前缀
     * @param requestMethod 请求的方法类型
     * @param body 请求体内容,可以为空
     * @param config 当前请求的配置信息
     * */
    public static HttpUriRequest createRequest(String url, HttpRequestMethod requestMethod, String body, RequestConfig config){
        HttpUriRequest request = emptyRequest(url, requestMethod);
        if(StringUtil.isEmpty(body)){
            return request;
        }

        if(request instanceof HttpRequestBase&&config!=null){
            HttpRequestBase baseRequest= (HttpRequestBase) request;
            baseRequest.setConfig(config);
        }

        if(!(request instanceof HttpEntityEnclosingRequestBase)){
            throw new RuntimeException("当前方法不能设置请求的body,requestMethod="+requestMethod);
        }
        HttpEntityEnclosingRequestBase entityEnclosingRequest=(HttpEntityEnclosingRequestBase)request;
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
        entityEnclosingRequest.setEntity(entity);
        return request;
    }
}
