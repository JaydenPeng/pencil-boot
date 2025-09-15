package org.pencil.common.http;

import lombok.Data;
import org.apache.http.Header;
import org.pencil.common.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求结果封装类
 * 
 * @author pencil
 * @date 25/04/28 10:19
 */
@Data
public class HttpResult {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    /**
     * 构造HTTP结果对象
     * 
     * @param statusCode HTTP状态码
     * @param headers HTTP响应头
     * @param body 响应体
     */
    public HttpResult(int statusCode, Header[] headers, String body) {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
        for (Header header : headers) {
            this.headers.put(header.getName(), header.getValue());
        }
        this.body = body;
    }
    
    /**
     * 检查HTTP状态码是否表示成功(2xx)
     * 
     * @return 如果状态码在200-299范围内则返回true
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * 获取指定响应头的值
     * 
     * @param name 响应头名称
     * @return 响应头值，如果不存在则返回null
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
    
    /**
     * 将响应体解析为指定类型的对象
     * 
     * @param clazz 目标类型
     * @param <T> 目标类型参数
     * @return 解析后的对象，解析失败时返回null
     */
    public <T> T parseBody(Class<T> clazz) {
        if (body == null || body.isEmpty()) {
            return null;
        }
        return JsonUtil.toObject(body, clazz);
    }
}
