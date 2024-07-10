package com.harrishjoshi.springaop.audit.trails.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class GlobalConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+05:45"));
    }
}
