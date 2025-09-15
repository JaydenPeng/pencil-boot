package org.pencil.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author pencil
 * @date 24/10/11 21:52
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }


}
