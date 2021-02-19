package org.formation.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.security.AbstractAuthorizationAuditListener;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

@Component
public class CustomAuditListener extends AbstractAuthorizationAuditListener {
	public static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

	@Override
	public void onApplicationEvent(AbstractAuthorizationEvent event) {
		if (event instanceof AuthorizationFailureEvent) {
			onAuthorizationFailureEvent((AuthorizationFailureEvent) event);
		}
	}

	private void onAuthorizationFailureEvent(AuthorizationFailureEvent event) {
		Map<String, Object> data = new HashMap<>();
		data.put("type", event.getAccessDeniedException().getClass().getName());
		data.put("message", event.getAccessDeniedException().getMessage());
		if ( event.getSource() instanceof FilterInvocation )
			data.put("requestUrl", ((FilterInvocation) event.getSource()).getRequestUrl());
		else if ( event.getSource() instanceof MethodInterceptor )
			data.put("method", ((MethodInterceptor)event.getSource()).toString());
		data.put("authorities",event.getAuthentication().getAuthorities());

		if (event.getAuthentication().getDetails() != null) {
			data.put("details", event.getAuthentication().getDetails());
		}

		publish(new AuditEvent(event.getAuthentication().getName(), AUTHORIZATION_FAILURE, data));
	}
}
