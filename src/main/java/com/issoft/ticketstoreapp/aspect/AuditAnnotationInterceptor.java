package com.issoft.ticketstoreapp.aspect;

import com.issoft.ticketstoreapp.model.audit.Argument;
import com.issoft.ticketstoreapp.model.audit.Audit;
import com.issoft.ticketstoreapp.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAnnotationInterceptor {

    private final AuditRepository auditRepository;

    @Pointcut("(within(com..service..*)) && execution(public * *(..))")
    public void withinServices() {
    }

    @Pointcut("@annotation( com.issoft.ticketstoreapp.annotation.Audit)")
    public void methodsWithAudit() {
    }

    @Pointcut("withinServices() && methodsWithAudit()")
    public void serviceAudit() {
    }

    @Around("serviceAudit()")
    public Object doServiceMethodsAudit(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        Optional<Object> returnValue = Optional.ofNullable(pjp.proceed());

        Audit audit = Audit.builder()
                .methodSignature(signature.toLongString())
                .returnValue(String.valueOf(returnValue.orElse(null)))
                .executionDatetime(LocalDateTime.now())
                .build();
        audit.setMethodArgs(getMethodArgsForAudit(pjp, audit));
        this.auditRepository.save(audit);

        return returnValue.orElse(null);
    }

    @AfterThrowing(pointcut = "serviceAudit()", throwing = "exception")
    public void doServiceExceptionAudit(JoinPoint jp, Throwable exception) {
        String exType = exception.getClass().getTypeName();
        String exMessage = exception.getMessage();
        Audit exceptionAudit = Audit.builder()
                .methodSignature(jp.getSignature().toLongString())
                .exceptionType(exType)
                .exceptionMessage(exMessage)
                .executionDatetime(LocalDateTime.now())
                .build();
        this.auditRepository.save(exceptionAudit);
    }

    private List<Argument> getMethodArgsForAudit(ProceedingJoinPoint pjp, Audit audit) {
        List<Argument> arguments = new ArrayList<>();

        for (Object arg : pjp.getArgs()) {
            Argument argument = new Argument();
            argument.setArgType(arg.getClass().getTypeName());
            argument.setArgValue(arg.toString());
            audit.addToMethodArgs(argument);
            arguments.add(argument);
        }
        return arguments;
    }
}