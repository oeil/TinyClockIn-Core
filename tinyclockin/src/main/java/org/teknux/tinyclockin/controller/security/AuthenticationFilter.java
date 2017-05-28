package org.teknux.tinyclockin.controller.security;

import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceManager;
import org.teknux.tinyclockin.service.configuration.IConfigurationService;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.util.MD5Util;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;


/**
 * @author Francois EYL
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String HEADER_AUTH_TYPE_KEYWORD = "Bearer";
    public static final String HEADER_ADMIN_TYPE_KEYWORD = "Admin";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_ROLE = "ADMIN";

    @Inject
    private ServletContext servletContext;

    private IServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(servletContext));
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || (!authorizationHeader.startsWith(HEADER_AUTH_TYPE_KEYWORD) && !authorizationHeader.startsWith(HEADER_ADMIN_TYPE_KEYWORD))) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        if (authorizationHeader != null && authorizationHeader.startsWith(HEADER_AUTH_TYPE_KEYWORD)) {
            // regular user
            final String token = authorizationHeader.substring(HEADER_AUTH_TYPE_KEYWORD.length()).trim();
            if (!getServiceManager().getService(IStoreService.class).isTokenExist(token)) {
                throw new NotAuthorizedException("Invalid Authorization Token");
            } else {
                AuthToken authToken = getServiceManager().getService(IStoreService.class).findToken(token);
                requestContext.setSecurityContext(buildSecurityContext(currentSecurityContext, authToken.getEmail(), HEADER_AUTH_TYPE_KEYWORD));
            }
        } else if (authorizationHeader != null && authorizationHeader.startsWith(HEADER_ADMIN_TYPE_KEYWORD)) {
            // admin user
            final String adminKeyTry = authorizationHeader.substring(HEADER_ADMIN_TYPE_KEYWORD.length()).trim();
            final String adminKey = MD5Util.toHexString(MD5Util.md5(getServiceManager().getService(IConfigurationService.class).getConfiguration().getAdminKey()));
            if (!Objects.equals(adminKey, adminKeyTry)) {
                throw new NotAuthorizedException("Authorization header must be provided");
            } else {
                requestContext.setSecurityContext(buildSecurityContext(currentSecurityContext, ADMIN_USERNAME, HEADER_ADMIN_TYPE_KEYWORD));
            }
        } else {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
    }

    private SecurityContext buildSecurityContext(final SecurityContext currentSecurityContext, final String user, final String scheme) {
        return new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return user;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return ADMIN_ROLE.equals(role) && HEADER_ADMIN_TYPE_KEYWORD.equals(scheme);
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return scheme;
            }
        };
    }
}
