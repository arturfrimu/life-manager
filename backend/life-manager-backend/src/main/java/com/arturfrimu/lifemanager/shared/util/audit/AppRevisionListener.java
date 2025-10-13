package com.arturfrimu.lifemanager.shared.util.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class AppRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        if (revisionEntity instanceof RevInfoEntity rev) {
            setUserFromSecurityContext(rev);
            setSourceIpFromRequestContextHolder(rev);
        } else {
            log.error("CRITICAL! No revision entity of type {} is not supported", revisionEntity.getClass());
            throw new IllegalArgumentException("No revision entity of type %s".formatted(revisionEntity.getClass()));
        }
    }

    private static void setSourceIpFromRequestContextHolder(RevInfoEntity rev) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes attrs) {
            HttpServletRequest request = attrs.getRequest();
            rev.setSourceIp(request.getRemoteAddr());
        }
    }

    private static void setUserFromSecurityContext(RevInfoEntity rev) {
        rev.setUsername("arturfrimu"); // todo add user from security context here
    }
}

