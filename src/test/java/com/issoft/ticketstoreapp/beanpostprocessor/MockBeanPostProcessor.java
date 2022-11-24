package com.issoft.ticketstoreapp.beanpostprocessor;

import com.issoft.ticketstoreapp.client.WeatherApiClient;
import org.mockito.Mockito;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class MockBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Object returnObject = bean;
        Class<?> beanClass = getClassForMocking(bean);
        if (beanClass.getAnnotation(Service.class) != null || beanClass.equals(WeatherApiClient.class)) {
            returnObject = Mockito.spy(bean);
        }
        return returnObject;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    private Class<?> getClassForMocking(final Object bean) {

        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
            return ((Advised) (bean)).getTargetClass();
        }
        return bean.getClass();
    }
}