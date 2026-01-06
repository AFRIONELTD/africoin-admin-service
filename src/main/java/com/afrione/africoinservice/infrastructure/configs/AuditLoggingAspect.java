package com.afrione.africoinservice.infrastructure.configs;

import com.afrione.africoinservice.domain.services.AuditService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "(" +
                    "execution(* com.afrione.africoinservice.infrastructure.web.controllers..*.*(..)) && " +
                    "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                    "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                    "@annotation(org.springframework.web.bind.annotation.DeleteMapping))" +
                    ") && " +
                    "!within(com.afrione.africoinservice.infrastructure.web.controllers.AuthController)",
            returning = "result"
    )
    public void logControllerOperation(JoinPoint joinPoint, Object result) {
        try {
            AuthenticatedUser authenticatedUser = getAuthenticatedUser();
            if (authenticatedUser != null) {
                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String actionDescription = String.format("%s.%s executed successfully with params: %s",
                        className, methodName, Arrays.toString(joinPoint.getArgs()));

                // Determine action type from method name and annotations
                AuditService.ActionType actionType = determineActionType(joinPoint);

                // Create a generic audit entry
                log.info("Audit Log - User: {}, Action: {}, Description: {}",
                        authenticatedUser.getUsername(), actionType, actionDescription);

                // Store audit trail in database
                auditService.createAudit(authenticatedUser, null, actionType, actionDescription);
            }
        } catch (Exception e) {
            log.error("Error logging audit trail", e);
        }
    }

    @AfterThrowing(
            pointcut = "(" +
                    "execution(* com.afrione.africoinservice.infrastructure.web.controllers..*.*(..)) && " +
                    "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                    "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                    "@annotation(org.springframework.web.bind.annotation.DeleteMapping))" +
                    ") && " +
                    "!within(com.afrione.africoinservice.infrastructure.web.controllers.AuthController)",
            throwing = "exception"
    )
    public void logControllerException(JoinPoint joinPoint, Exception exception) {
        try {
            AuthenticatedUser authenticatedUser = getAuthenticatedUser();
            if (authenticatedUser != null) {
                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String actionDescription = String.format("%s.%s failed with error: %s",
                        className, methodName, exception.getMessage());

                AuditService.ActionType actionType = determineActionType(joinPoint);

                log.warn("Audit Log - User: {}, Action: {} FAILED, Description: {}",
                        authenticatedUser.getUsername(), actionType, actionDescription);

                auditService.createAudit(authenticatedUser, null, actionType, actionDescription);
            }
        } catch (Exception e) {
            log.error("Error logging audit trail for exception", e);
        }
    }

    private AuthenticatedUser getAuthenticatedUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof AuthenticatedUser) {
                return (AuthenticatedUser) principal;
            }
        } catch (Exception e) {
            log.debug("Could not retrieve authenticated user from security context", e);
        }
        return null;
    }

    private AuditService.ActionType determineActionType(JoinPoint joinPoint) {
        try {
            Method method = getTargetMethod(joinPoint);
            if (method == null) {
                return AuditService.ActionType.UPDATE;
            }

            if (method.isAnnotationPresent(PostMapping.class)) {
                String methodName = method.getName().toLowerCase();
                if (methodName.contains("create") || methodName.contains("setup") || methodName.contains("initiate")) {
                    return AuditService.ActionType.CREATE;
                }
                return AuditService.ActionType.UPDATE;
            } else if (method.isAnnotationPresent(PutMapping.class)) {
                return AuditService.ActionType.UPDATE;
            } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                return AuditService.ActionType.DELETE;
            }
        } catch (Exception e) {
            log.debug("Error determining action type", e);
        }
        return AuditService.ActionType.UPDATE;
    }

    private Method getTargetMethod(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] paramTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterTypes();
            return joinPoint.getTarget().getClass().getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            log.debug("Could not find target method", e);
        }
        return null;
    }
}
