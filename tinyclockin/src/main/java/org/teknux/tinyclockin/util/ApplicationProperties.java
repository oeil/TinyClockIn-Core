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

package org.teknux.tinyclockin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.exception.AppException;

public class ApplicationProperties {

    private static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    
    private static final String PROPERTIES_FILE = "/application.properties";
    
    public static final String APPLICATION_NAME_KEY = "application.name";
    public static final String APPLICATION_VERSION_KEY = "application.version";
    
    private static ApplicationProperties instance;
    
    private Properties properties; 
    
    //Singleton
    public ApplicationProperties(String propertiesFile) throws AppException {
        InputStream inputStream = ApplicationProperties.class.getResourceAsStream(propertiesFile);
        
        if (inputStream == null) {
            throw new AppException("Application properties file does not exists");
        }
        
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new AppException("Can not get properties", e);
        }
        
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.error("Can not close InputStream", e);
        }
    }
    
    public static ApplicationProperties getInstance() throws AppException {
        if (instance == null) {
            instance = new ApplicationProperties(PROPERTIES_FILE);
        }
        
        return instance;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
