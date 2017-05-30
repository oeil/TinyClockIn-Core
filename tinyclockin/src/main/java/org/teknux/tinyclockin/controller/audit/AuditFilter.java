package org.teknux.tinyclockin.controller.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.model.Audit;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceManager;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.util.StopWatch;
import org.teknux.tinyclockin.util.StopWatchServletContextUtil;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;


/**
 * Implementation of {@link Logged} to Audit Http Resquest/Response.
 *
 * @author Francois EYL
 */
@Logged
@Provider
public class AuditFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static Logger logger = LoggerFactory.getLogger(AuditFilter.class);

    @Inject
    private ServletContext servletContext;

    @Context
    private HttpServletRequest servletRequest;

    private IServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(servletContext));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final Principal principal = requestContext.getSecurityContext().getUserPrincipal();

        final String email = principal == null ? null : principal.getName();
        final String method = requestContext.getMethod();
        final long runtime = StopWatchServletContextUtil.read(servletContext).getDuration().getSeconds();
        final String url = servletRequest.getServletPath();
        final String ip = servletRequest.getRemoteHost();
        final int status = responseContext.getStatus();

        final Audit audit = Audit.create(email, method, url, ip, runtime, status);
        logger.debug(String.format("##AUDIT### email=[%s] method=[%s] url=[%s] ip=[%s] runtimeInSec=[%s] http-rsp-status[%s]", email, method, url, ip, runtime, status));

        getServiceManager().getService(IStoreService.class).storeAudit(audit);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        StopWatchServletContextUtil.store(servletContext, StopWatch.get());
    }
}
