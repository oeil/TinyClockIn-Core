/*
 * Copyright (C) 2016 TekNux.org
 *
 * This file is part of the TinyClockIn GPL Source Code.
 *
 * TinyClockIn Source Code is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * TinyClockIn Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dropbitz Community Source Code.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.tinyclockin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.controller.security.Secured;
import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceManager;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.util.StopWatch;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Francois EYL
 */
@Path("/api/actions")
@Secured
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ActionsController {

    @Inject
    private ServletContext servletContext;

    @Context
    private SecurityContext securityContext;

    private static Logger logger = LoggerFactory.getLogger(ActionsController.class);

    private IServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(servletContext));
    }

    @GET
    public List<ClockAction> actions(@QueryParam("latestOnly") Boolean latestOnly) {
        final StopWatch stopWatch = StopWatch.get();

        final String email = securityContext.getUserPrincipal().getName();

        List<ClockAction> actionsToReturn = null;
        if (Boolean.TRUE.equals(latestOnly)) {
            final ClockAction lastAction = getServiceManager().getService(IStoreService.class).getLastAction(email);
            actionsToReturn = new ArrayList<>();
            if (lastAction != null) {
                actionsToReturn.add(lastAction);
            }
        } else {
            actionsToReturn = getServiceManager().getService(IStoreService.class).getActions(email);
        }
        logger.debug("GET /api/actions [{} items] [{} sec]", actionsToReturn.size(), stopWatch.stop().getSeconds());
        return actionsToReturn;
    }

    @POST
    public Response doAction(ClockAction action) {
        final StopWatch stopWatch = StopWatch.get();

        final String email = securityContext.getUserPrincipal().getName();

        Response errRsp = validateAction(action, email);
        if (errRsp != null) {
            return errRsp;
        }

        getServiceManager().getService(IStoreService.class).storeAction(email, action);
        logger.debug("POST /api/actions [{} | {}] [{} sec]", action.getType(), action.getWorkstation(), stopWatch.stop().getSeconds());
        return Response.status(Response.Status.OK).entity(action).build();
    }

    private Response validateAction(final ClockAction action, final String userId) {
        if (action == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An action resource is expected!").type(MediaType.TEXT_PLAIN).build();
        }

        final ClockAction lastAction = getServiceManager().getService(IStoreService.class).getLastAction(userId);
        if (lastAction == null && action.getType() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot Check-out, Check-in is required first!").type(MediaType.TEXT_PLAIN).build();
        } else if (lastAction != null && lastAction.getType() == action.getType()) {
            if (action.getType() == 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Alreay Checked-out!").type(MediaType.TEXT_PLAIN).build();
            }
            if (action.getType() == 1) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Alreay Checked-in!").type(MediaType.TEXT_PLAIN).build();
            }
        }

        if (action.getDescription() != null && action.getDescription().length() > 200) {
            return Response.status(Response.Status.BAD_REQUEST).entity("description limit is 200 characters!").type(MediaType.TEXT_PLAIN).build();
        }
        if (action.getWorkstation() < 10 || action.getWorkstation() > 1000) {
            return Response.status(Response.Status.BAD_REQUEST).entity("workstation must be a number between 10-1000!").type(MediaType.TEXT_PLAIN).build();
        }

        return null;
    }
}
