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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 5000;

    public static CloseableHttpClient createHttpClient(String proxyHost, int proxyPort) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setProxy(new HttpHost(proxyHost, proxyPort))
                .build();
    }

    public static CloseableHttpClient createHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();
    }

    public static HttpResult get(String url) throws IOException {
        return get(url, null);
    }

    public static HttpResult get(String url, Map<String, String> params) throws IOException {
        return get(url, params, null);
    }

    public static HttpResult get(String url, Map<String, String> params, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }

        String queryString = buildQueryString(params);
        HttpGet httpGet = new HttpGet(url + (queryString.isEmpty() ? "" : "?" + queryString));

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        }
    }

    public static HttpResult postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null);
    }

    public static HttpResult postForm(String url, Map<String, String> params, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formParams = new ArrayList<>();
        params.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
        httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    public static HttpResult postJson(String url, String json) throws IOException {
        return postJson(url, json, null);
    }

    public static HttpResult postJson(String url, String json, CloseableHttpClient httpClient) throws IOException {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    private static HttpResult handleResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String body = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
        return new HttpResult(
                response.getStatusLine().getStatusCode(),
                response.getAllHeaders(),
                body
        );
    }

    private static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }
}
