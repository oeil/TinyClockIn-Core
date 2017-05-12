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

package org.teknux.tinyclockin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.AppServlet;
import org.teknux.tinyclockin.service.configuration.ConfigurationServiceImpl;
import org.teknux.tinyclockin.service.configuration.IConfigurationService;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.service.store.StoreServiceImpl;

import javax.servlet.ServletContext;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Central access point for services
 */
public class ServiceManager implements IServiceManager {

    private static Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    /**
     * Running Service List
     */
    private final Map<Class<? extends IService>, IService> services = new LinkedHashMap<>();

    private final ServletContext servletContext;

    public ServiceManager(ServletContext context) {
        this.servletContext = context;

        services.put(IConfigurationService.class, new ConfigurationServiceImpl());
        services.put(IStoreService.class, new StoreServiceImpl());
    }

    public static IServiceManager get(ServletContext context) {
        Objects.requireNonNull(context);
        return (IServiceManager) context.getAttribute(AppServlet.CONTEXT_ATTRIBUTE_SERVICE_MANAGER);
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void start() throws ServiceException {
        synchronized (services) {
            logger.trace("Starting {} Services...", services.size());
            for (final IService service : services.values()) {
                logger.trace("Starting Service [{}]...", service.getClass().getSimpleName());
                service.start(this);
                logger.trace("Service [{}] started", service.getClass().getSimpleName());
            }
            logger.trace("{} Services started", services.size());
        }
    }

    public void stop() throws ServiceException {
        synchronized (services) {
            logger.trace("Stopping {} Services...", services.size());
            for (final IService service : services.values()) {
                logger.trace("Stopping Service [{}]...", service.getClass().getSimpleName());
                service.stop();
                logger.trace("Service [{}] stopped", service.getClass().getSimpleName());
            }
            logger.trace("{} Services stopped", services.size());
        }
    }

    @Override
    public <T extends IService> T getService(final Class<T> serviceClass) {
        synchronized (services) {
            final IService wantedService = services.get(serviceClass);
            if (wantedService == null || !serviceClass.isAssignableFrom(wantedService.getClass())) {
                throw new IllegalArgumentException("Service Unavailable");
            }
            final T castedService = serviceClass.cast(wantedService);
            if (castedService == null) {
                throw new IllegalArgumentException("Error accessing service");
            }
            return castedService;
        }
    }
}
