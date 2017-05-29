package org.teknux.tinyclockin.controller;

import org.teknux.tinyclockin.exception.AppException;
import org.teknux.tinyclockin.model.Version;
import org.teknux.tinyclockin.util.ApplicationProperties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * @author Francois EYL
 */
@Path("/api/version")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class VersionController {

    @GET
    public Version getVersion() throws AppException {
        Version version = new Version();
        version.setVersion(ApplicationProperties.getInstance().getProperty(ApplicationProperties.APPLICATION_VERSION_KEY));
        return version;
    }
}
