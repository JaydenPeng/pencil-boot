package org.pencil.web.anno;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author pencil
 * @date 24/10/11 21:10
 */
@Slf4j
@Aspect
@Component
public class ReqStatisticsAspect {

    @Resource
    private Counter requestCounter;

    @Resource
    private Histogram requestHistogram;

    public Object reqRecord(ProceedingJoinPoint joinPoint, ReqStatistics reqStatistics) throws Throwable {
        long startTime = System.currentTimeMillis();

        String value = reqStatistics.value();
        StringBuilder builder = new StringBuilder("http req log").append(System.lineSeparator());

        builder.append("tag=").append(value).append(System.lineSeparator());

        if (reqStatistics.reqLog()) {
            Object[] args = joinPoint.getArgs();
            builder.append("req=").append(Arrays.toString(args)).append(System.lineSeparator());
        }

        boolean flag = true;
        try {
            Object proceed = joinPoint.proceed();
            if (reqStatistics.respLog()) {
                builder.append("resp=").append(proceed).append(System.lineSeparator());
            }
            return proceed;
        } catch (Throwable throwable) {
            flag = false;
            builder.append("status=").append(throwable.getMessage()).append(System.lineSeparator());
            throw throwable;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;

            if (reqStatistics.costTime()) {
                builder.append("costTime=").append(costTime).append(System.lineSeparator());
            }
            log.info(builder.toString());

            if (reqStatistics.metric()) {
                String status = flag ? "success" : "fail";
                requestCounter.labels(value, status).inc();
                requestHistogram.labels(value, status).observe(costTime);
            }
        }
    }


}
