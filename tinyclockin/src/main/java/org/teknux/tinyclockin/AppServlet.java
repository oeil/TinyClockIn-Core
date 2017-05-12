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

package org.teknux.tinyclockin;

import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.service.ServiceException;
import org.teknux.tinyclockin.service.ServiceManager;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.util.Objects;


public class AppServlet extends ServletContainer {

    public static final String CONTEXT_ATTRIBUTE_SERVICE_MANAGER = "ServiceManager";

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(AppServlet.class);

    private ServiceManager serviceManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            logger.trace("Starting Service Manager...");
            serviceManager = new ServiceManager(filterConfig.getServletContext());
            serviceManager.start();
            filterConfig.getServletContext().setAttribute(CONTEXT_ATTRIBUTE_SERVICE_MANAGER, serviceManager);
            logger.trace("Service Manager started");
        } catch (Exception e) { // we need to catch any exceptions
            logger.error("Error while starting application services", e);
            throw new UnavailableException("Error while initializing application services");
        }

        super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Objects.requireNonNull(serviceManager);
        try {
            serviceManager.stop(); // stop the service
        } catch (ServiceException e) {
            logger.error("Error while stopping application services", e);
        }
        getServletContext().removeAttribute(CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
        logger.trace("Service Manager stopped.");

        super.destroy();
    }
}
