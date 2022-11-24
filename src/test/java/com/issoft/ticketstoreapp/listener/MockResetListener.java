package com.issoft.ticketstoreapp.listener;

import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import static java.util.Arrays.stream;

@Component
public class MockResetListener implements TestExecutionListener, Ordered {

    @Override
    public void afterTestExecution(TestContext testContext) {

        stream(testContext
                .getApplicationContext()
                .getBeanDefinitionNames())
                .map(beanName -> testContext.getApplicationContext().getBean(beanName))
                .filter(MockUtil::isMock)
                .forEach(Mockito::reset);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}