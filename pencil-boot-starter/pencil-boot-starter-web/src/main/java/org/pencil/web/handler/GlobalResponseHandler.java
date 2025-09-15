package org.pencil.web.handler;

import org.pencil.common.beans.resp.Result;
import org.pencil.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author pencil
 * @date 24/10/11 21:46
 */
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Value("${pencil.response.transfer.switch:true}")
    private boolean transferSwitch;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return returnType.getContainingClass().isAnnotationPresent(RestController.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (!transferSwitch) {
            return body;
        }

        if (StringHttpMessageConverter.class.getName().equalsIgnoreCase(selectedConverterType.getName())) {
            HttpHeaders headers = response.getHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return JsonUtil.toJsonStr(body);
        }

        if (body instanceof Result<?>) {
            return body;
        }
        return Result.ok(body);
    }
}
