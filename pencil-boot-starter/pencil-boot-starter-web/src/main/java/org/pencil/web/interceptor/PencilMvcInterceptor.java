package org.pencil.web.interceptor;

import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.lang3.StringUtils;
import org.pencil.common.constant.Constants;
import org.pencil.common.contexts.GlobalContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author pencil
 * @date 24/10/11 21:25
 */
public class PencilMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();

        if (ArrayUtil.isNotEmpty(cookies)) {
            String userId = Arrays.stream(cookies).filter(cookie -> Constants.USER_ID.equals(cookie.getName())).findFirst()
                    .map(Cookie::getValue).orElse(Constants.NULL);
            if (StringUtils.isNoneBlank(userId)) {
                GlobalContext.put(Constants.USER_ID, userId);
                MDC.put(Constants.USER_ID, Constants.convertMdc(userId));
            }
        }

        String traceId = Optional.ofNullable(request.getHeader(Constants.TRACE_ID)).orElse("web-" + UUID.randomUUID());
        GlobalContext.generateTraceId(traceId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
        GlobalContext.clearAll();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
