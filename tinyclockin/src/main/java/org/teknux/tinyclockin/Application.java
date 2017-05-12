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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.jettybootstrap.JettyBootstrap;
import org.teknux.jettybootstrap.JettyBootstrapException;
import org.teknux.jettybootstrap.configuration.JettyConfiguration;
import org.teknux.jettybootstrap.configuration.JettyConnector;
import org.teknux.tinyclockin.config.app.Configuration;
import org.teknux.tinyclockin.config.app.factory.ConfigurationException;
import org.teknux.tinyclockin.config.app.factory.ConfigurationFactory;


public class Application {

    private static final int EXIT_CODE_CONFIG_ERROR = 1;
    private static final int EXIT_CODE_JETTY_STARTUP_ERROR = 2;

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static volatile Configuration configuration = null;

    private JettyBootstrap jettyBootstrap;

    public Application() {
        this(null);
    }

    public Application(Configuration configuration) {
        Application.configuration = configuration;
    }

    public void start() {
        start(false);
    }

    public void start(boolean join) {
        if (jettyBootstrap == null) {
            //initialize jetty configuration for the app
            try {
                if (configuration == null) {
                    logger.debug("Loading application configuration...");
                    Application.configuration = ConfigurationFactory.getConfiguration(Configuration.class);
                } else {
                    logger.debug("Using provided application configuration...");
                }

                logger.debug("Init Server...");
                jettyBootstrap = initJetty(configuration);
            } catch (ConfigurationException | IllegalArgumentException | JettyBootstrapException e) {
                logger.error("Configuration file error", e);
                System.exit(EXIT_CODE_CONFIG_ERROR);
            }
        }

        try {
            //start the jetty server
            logger.debug("Starting Application...");
            jettyBootstrap.startServer(false);
            logger.info("TinyClockIn API Ready at http{}://localhost:{}/api", configuration.isSsl() ? "s" : "", configuration.getPort());
            if (join) {
                jettyBootstrap.joinServer();
            }
        } catch (JettyBootstrapException e) {
            logger.error("Internal Server Error", e);
            System.exit(EXIT_CODE_JETTY_STARTUP_ERROR);
        }
    }

    /**
     * Create a new Jetty Container for the given {@link Configuration}
     *
     * @param configuration
     * @return the newly created instance
     * @throws JettyBootstrapException
     */
    protected JettyBootstrap initJetty(Configuration configuration) throws JettyBootstrapException {
        JettyConfiguration jettyConfiguration = new JettyConfiguration();
        if (configuration.isSsl()) {
            jettyConfiguration.setJettyConnectors(JettyConnector.HTTPS);
            jettyConfiguration.setSslPort(configuration.getPort());
        } else {
            jettyConfiguration.setPort(configuration.getPort());
        }
        final JettyBootstrap jettyBootstrap = new JettyBootstrap(jettyConfiguration);
        jettyBootstrap.addSelf(configuration.getBasePath());
        return jettyBootstrap;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public void stop() {
        if (!isStarted()) {
            throw new IllegalArgumentException("Application is not started");
        }
        try {
            logger.debug("Stopping Application...");
            jettyBootstrap.stopServer();
            logger.info("Stopped");
        } catch (JettyBootstrapException e) {
            logger.error("Error while stopping application", e);
        }
    }

    public boolean isStarted() {
        return jettyBootstrap != null && jettyBootstrap.isServerStarted();
    }
}