package ca.concordia.inse6260.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class AdminSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		// FIXME - check ROLE_PROFESSOR
		boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
		
		if (isAdmin) {
			handle(request, response, authentication);
	        clearAuthenticationAttributes(request);
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// FIXME - check ROLE_PROFESSOR
		boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
		
		if (isAdmin) {
			return "/";
		} else {
			return super.determineTargetUrl(request, response);
		}
	}
	
	private boolean hasRole(final Authentication authentication, final String role) {
		boolean hasRole = false;
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			if (grantedAuthority.getAuthority().equals(role)) {
				hasRole = true;
				break;
			}
		}
		return hasRole;
	}
	
	

	
}
