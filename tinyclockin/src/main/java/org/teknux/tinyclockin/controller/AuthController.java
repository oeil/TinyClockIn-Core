package org.teknux.tinyclockin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceManager;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.util.StopWatch;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;


/**
 * Authentication REST API Controller.
 *
 * @author Francois EYL
 */
@Path("/api/auth")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Inject
    private ServletContext servletContext;
    @Context
    private HttpServletRequest httpServletRequest;

    private IServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(servletContext));
    }

    /**
     * REST controller allowing to retrieve or create an {@link AuthToken} instance for the given user id (email).
     *
     * @param authToken
     * @return
     */
    @POST
    public Response getOrCreateToken(final AuthToken authToken) {
        final StopWatch stopWatch = StopWatch.get();

        if (authToken == null || authToken.getEmail() == null || authToken.getEmail().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input! User Id must be given.").type(MediaType.TEXT_PLAIN).build();
        }

        final AuthToken storedToken = getServiceManager().getService(IStoreService.class).getOrCreateToken(authToken.getEmail());
        logger.debug("POST /api/auth [{} | {}] [{} sec]", storedToken.getEmail(), storedToken.getToken(), stopWatch.stop().getSeconds());
        return Response.status(Response.Status.OK).entity(storedToken).build();
    }
}
