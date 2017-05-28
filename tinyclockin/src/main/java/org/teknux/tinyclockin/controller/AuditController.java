package org.teknux.tinyclockin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.controller.security.Secured;
import org.teknux.tinyclockin.model.Audit;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceManager;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.util.StopWatch;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Objects;


/**
 * @author Francois EYL
 */
@Path("/api/audits")
@Secured
@RolesAllowed({"ADMIN"})
@Produces({ MediaType.APPLICATION_JSON })
public class AuditController {

    private static Logger logger = LoggerFactory.getLogger(AuditController.class);

    @Inject
    private ServletContext servletContext;

    @Context
    private SecurityContext securityContext;

    private IServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(servletContext));
    }

    @GET
    public Response audits(@Context HttpServletRequest requestContext) {
        final StopWatch stopWatch = StopWatch.get();

        final String email = securityContext.getUserPrincipal().getName();

        final Audit audit = Audit.create(email, Audit.Type.HTTP_GET, "api/audits", requestContext.getRemoteAddr());
        getServiceManager().getService(IStoreService.class).audit(audit);

        final List<Audit> auditsToReturn = getServiceManager().getService(IStoreService.class).getAudits();
        logger.debug("GET /api/audits [{} items] [{} sec]", auditsToReturn.size(), stopWatch.stop().getSeconds());

        return Response.status(Response.Status.OK).entity(auditsToReturn).build();
    }
}
