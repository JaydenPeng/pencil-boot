package org.pencil.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP客户端工具类，提供简便的HTTP请求方法
 */
@Slf4j
public class HttpClientUtil {

    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;
    
    private static final int MAX_TOTAL_CONNECTIONS = 200;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 20;
    
    private static final PoolingHttpClientConnectionManager CONNECTION_MANAGER;
    private static final CloseableHttpClient DEFAULT_CLIENT;
    
    static {
        CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
        CONNECTION_MANAGER.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        CONNECTION_MANAGER.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
        CONNECTION_MANAGER.setValidateAfterInactivity(1000);
        
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .build();
                
        DEFAULT_CLIENT = HttpClients.custom()
                .setConnectionManager(CONNECTION_MANAGER)
                .setDefaultRequestConfig(defaultRequestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 创建带代理的HTTP客户端
     * 
     * @param proxyHost 代理主机
     * @param proxyPort 代理端口
     * @return HTTP客户端实例
     */
    public static CloseableHttpClient createHttpClient(String proxyHost, int proxyPort) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setProxy(new HttpHost(proxyHost, proxyPort))
                .build();
    }

    /**
     * 创建自定义配置的HTTP客户端
     * 
     * @param connectTimeout 连接超时时间(毫秒)
     * @param socketTimeout 套接字超时时间(毫秒)
     * @return HTTP客户端实例
     */
    public static CloseableHttpClient createHttpClient(int connectTimeout, int socketTimeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();
    }

    /**
     * 创建默认配置的HTTP客户端
     * 
     * @return HTTP客户端实例
     */
    public static CloseableHttpClient createHttpClient() {
        return DEFAULT_CLIENT;
    }

    /**
     * 执行GET请求
     * 
     * @param url 请求URL
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 执行带参数的GET请求
     * 
     * @param url 请求URL
     * @param params 请求参数
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult get(String url, Map<String, String> params) throws IOException {
        return get(url, params, null);
    }

    /**
     * 执行带参数和自定义HTTP客户端的GET请求
     * 
     * @param url 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return get(url, params, headers, null);
    }

    /**
     * 执行带参数、请求头和自定义HTTP客户端的GET请求
     * 
     * @param url 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @param httpClient 自定义HTTP客户端
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult get(String url, Map<String, String> params, Map<String, String> headers, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = DEFAULT_CLIENT;
        }

        String queryString = buildQueryString(params);
        HttpGet httpGet = new HttpGet(url + (queryString.isEmpty() ? "" : "?" + queryString));
        
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpGet::setHeader);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        }
    }

    /**
     * 执行表单POST请求
     * 
     * @param url 请求URL
     * @param params 表单参数
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null);
    }

    /**
     * 执行带自定义HTTP客户端的表单POST请求
     * 
     * @param url 请求URL
     * @param params 表单参数
     * @param httpClient 自定义HTTP客户端
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postForm(String url, Map<String, String> params, CloseableHttpClient httpClient) throws IOException {
        return postForm(url, params, null, httpClient);
    }

    /**
     * 执行带请求头和自定义HTTP客户端的表单POST请求
     * 
     * @param url 请求URL
     * @param params 表单参数
     * @param headers 请求头
     * @param httpClient 自定义HTTP客户端
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postForm(String url, Map<String, String> params, Map<String, String> headers, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = DEFAULT_CLIENT;
        }

        HttpPost httpPost = new HttpPost(url);
        
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::setHeader);
        }
        
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> formParams = new ArrayList<>();
            params.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    /**
     * 执行JSON POST请求
     * 
     * @param url 请求URL
     * @param json JSON字符串
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postJson(String url, String json) throws IOException {
        return postJson(url, json, null);
    }

    /**
     * 执行带自定义HTTP客户端的JSON POST请求
     * 
     * @param url 请求URL
     * @param json JSON字符串
     * @param httpClient 自定义HTTP客户端
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postJson(String url, String json, CloseableHttpClient httpClient) throws IOException {
        return postJson(url, json, null, httpClient);
    }

    /**
     * 执行带请求头和自定义HTTP客户端的JSON POST请求
     * 
     * @param url 请求URL
     * @param json JSON字符串
     * @param headers 请求头
     * @param httpClient 自定义HTTP客户端
     * @return HTTP响应结果
     * @throws IOException 如果请求过程中发生IO异常
     */
    public static HttpResult postJson(String url, String json, Map<String, String> headers, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = DEFAULT_CLIENT;
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::setHeader);
        }
        
        if (json != null) {
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    /**
     * 处理HTTP响应
     * 
     * @param response HTTP响应
     * @return 处理后的HTTP结果
     * @throws IOException 如果处理过程中发生IO异常
     */
    private static HttpResult handleResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
        return new HttpResult(
                response.getStatusLine().getStatusCode(),
                response.getAllHeaders(),
                body
        );
    }

    /**
     * 构建查询字符串
     * 
     * @param params 参数映射
     * @return 构建的查询字符串
     */
    private static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }
    
    /**
     * 关闭连接管理器
     * 应在应用程序关闭时调用此方法
     */
    public static void shutdown() {
        try {
            DEFAULT_CLIENT.close();
            CONNECTION_MANAGER.close();
        } catch (IOException e) {
            log.error("Error shutting down HttpClientUtil", e);
        }
    }
}
