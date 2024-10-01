package org.pencil.feign.config;

import cn.hutool.core.text.CharSequenceUtil;
import feign.Logger;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author pencil
 * @date 24/10/01 19:00
 */
@Slf4j
public class CustomFeignLogger extends Logger {


    private final Counter feignCounter;

    private final Histogram feignHistogram;

    public CustomFeignLogger(Counter feignCounter, Histogram feignHistogram) {
        this.feignCounter = feignCounter;
        this.feignHistogram = feignHistogram;
    }

    private final ThreadLocal<Map<String, Object>> logInfoHolder = ThreadLocal.withInitial(HashMap::new);

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        logInfoHolder.get().put("request", request.requestTemplate());
    }

    /**
     * 日志返回信息
     * @param configKey 配置key
     * @param logLevel 日志级别
     * @param response response对象
     * @param elapsedTime 请求耗时
     * @return 返回对象
     * @throws IOException
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        Map<String, Object> logInfo = this.logInfoHolder.get();
        RequestTemplate requestTemplate = (RequestTemplate)logInfo.get("request");
        StringBuilder builder = new StringBuilder("Feign请求信息\n");
        builder.append("请求url=").append(requestTemplate.path()).append("\n")
                .append("请求耗时＝").append(elapsedTime).append("ms").append("\n")
                .append("请求状态＝").append(response.status()).append("\n")
                .append("请求参数=").append(requestTemplate.queries()).append("\n")
                .append("请求body=").append(Optional.ofNullable(requestTemplate.body())
                        .map(body -> new String(body, StandardCharsets.UTF_8)).orElse(null)).append("\n")
                .append("请求header=").append(requestTemplate.headers()).append("\n");
        if (response.body() != null) {
            byte[] bodyData = response.body().asInputStream().readAllBytes();
            String respStr = new String(bodyData, StandardCharsets.UTF_8);
            if (response.status() == 200) {
                respStr = CharSequenceUtil.sub(respStr, 0, 1024);
            }
            builder.append("请求响应=").append(respStr);
            response = response.toBuilder().body(bodyData).build();
        } else {
            builder.append("请求响应=").append("null");
        }

        feignCounter.labels(requestTemplate.path(), String.valueOf(response.status())).inc();
        feignHistogram.labels(requestTemplate.path()).observe(elapsedTime / 1000.0);

        if (response.status() != 200) {
            log.error(builder.toString());
        } else {
            log.info(builder.toString());
        }
        this.logInfoHolder.remove();
        return response;
    }
}
