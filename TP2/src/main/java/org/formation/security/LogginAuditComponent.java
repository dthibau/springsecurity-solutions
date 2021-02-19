package org.formation.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LogginAuditComponent {

	Logger logger = LoggerFactory.getLogger(LogginAuditComponent.class);

	@EventListener
	public void auditEventHappened(
			AuditApplicationEvent auditApplicationEvent) {
		
		AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		
		WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");

		if ( details != null ) {
			logger.info("Remote IP address: " + details.getRemoteAddress());
			logger.info(" Session Id: " + details.getSessionId());
		}
	}
	
}
