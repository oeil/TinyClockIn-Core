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

package org.teknux.tinyclockin.config.app.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.skife.config.ConfigurationObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.tinyclockin.util.PathUtil;

public class ConfigurationFactory<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ConfigurationFactory.class);

	private static final String RESOURCE_SEPARATOR = "/";
		
	private static final String DEFAULT_CONFIG_FILE_BASEPATH = PathUtil.getJarDir();
	private static final String DEFAULT_CONFIG_RESOURCE_BASEPATH = RESOURCE_SEPARATOR;
	private static final String DEFAULT_CONFIG_FILENAME = "config.properties";
	private static final String DEFAULT_CONFIG_DIST_FILENAME = "config.properties-dist";
	
	private Class<T> clazz;
	
	private String configFileBasePath;
	private String configResourceBasePath;
	private String configFileName;
	private String configDistFileName;


	public String getConfigFileBasePath() {
        return configFileBasePath;
    }

    public void setConfigFileBasePath(String configFileBasePath) {
        this.configFileBasePath = configFileBasePath;
    }

    public String getConfigResourceBasePath() {
        return configResourceBasePath;
    }

    public void setConfigResourceBasePath(String configResourceBasePath) {
        this.configResourceBasePath = configResourceBasePath;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public String getConfigDistFileName() {
        return configDistFileName;
    }

    public void setConfigDistFileName(String configDistFileName) {
        this.configDistFileName = configDistFileName;
    }

    private String getConfigFilePath() {
        return (configFileBasePath.equals(File.separator)?configFileBasePath:configFileBasePath + File.separator) + configFileName;
    }
    
    private String getConfigResourcePath() {
        return (configResourceBasePath.equals(RESOURCE_SEPARATOR)?configResourceBasePath:configResourceBasePath + RESOURCE_SEPARATOR) + configFileName;
    }
    
    private String getConfigDistResourcePath() {
        return (configResourceBasePath.equals(RESOURCE_SEPARATOR)?configResourceBasePath:configResourceBasePath + RESOURCE_SEPARATOR) + configDistFileName;
    }
       
    /**
     * Get configuration from jar directory, from resource or create new configuration file
     * 
     * @param clazz Configuration Class
     * @param <T> Configuration Type
     * @return Configuration
     * @throws ConfigurationException On failed
     */
    public static <T> T getConfiguration(Class<T> clazz) throws ConfigurationException {
        return new ConfigurationFactory<T>(clazz).buildConfiguration();
    }
    
    public ConfigurationFactory(Class<T> clazz) {
        this.clazz = clazz;
        
        configFileBasePath = DEFAULT_CONFIG_FILE_BASEPATH;
        configResourceBasePath = DEFAULT_CONFIG_RESOURCE_BASEPATH;
        configFileName = DEFAULT_CONFIG_FILENAME;
        configDistFileName = DEFAULT_CONFIG_DIST_FILENAME;
	}

	public T buildConfiguration() throws ConfigurationException {
		logger.trace("Get configuration...");

		InputStream inputStream = null;
		
		File file = new File(getConfigFilePath());

		logger.trace("Check if config file exists in jar directory [{}]...", file.getPath());
		if (file.exists()) { //Check if file exists in jar directory
			inputStream = getStreamFromFile(file);
			logger.debug("Use config file in jar directory");
		} else { //Check if config file exits in resource
			logger.trace("Check if config file exists in resource [{}]...", getConfigResourcePath());
				
			inputStream = getStreamFromResource(getConfigResourcePath());
			if (inputStream != null) {
				logger.warn("Use resource config");				
			} else { //If not, Create file in jar directory
				logger.warn("File does not exits. Create config file exists in jar directory [{}]...", file.getPath());
				try {
                    inputStream = createConfigurationFile(getConfigDistResourcePath(), file);
                } catch (IOException e) {
                    throw new ConfigurationException(MessageFormat.format("Can not create config file in jar directory [{0}]", file.getPath()), e);
                }
			}
		}
		
		//Build configuration
		try {
            return buildConfigurationFromStream(inputStream);
        } catch (IOException e) {
            throw new ConfigurationException(MessageFormat.format("Bad config file [{0}]", file.getPath()), e);
        }
	}
		
	/**
	 * Get Stream from file
	 * 
	 * @param file
	 * @return InputStream
	 * @throws ConfigurationException
	 */
	private InputStream getStreamFromFile(File file) throws ConfigurationException {
        if (! file.canRead()) { //Check if file is readable in jar directory
            throw new ConfigurationException(MessageFormat.format("Config file is not readable in jar directory [{0}]", file.getPath()));
        } else { //Open config file in jar directory
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new ConfigurationException(MessageFormat.format("Can not read config file [{0}]", file.getPath()));
            }
        }
	}
	
	/**
	 * Get Stream from resource
	 * 
	 * @param resource
	 * @return InputStream
	 */
	private InputStream getStreamFromResource(String resource) {
	    return ConfigurationFactory.class.getResourceAsStream(resource);
	}

	/**
	 * Create Configuration File from resource
	 * 
	 * @param file
	 * @param distResource
	 * @return InputStream
	 * @throws IOException 
	 */
	private InputStream createConfigurationFile(String distResource, File file) throws IOException {
	    InputStream inputStream = null;
        FileUtils.copyURLToFile(ConfigurationFactory.class.getResource(distResource), file);
        inputStream = new FileInputStream(file);
        
	    return inputStream;
	}
	
	/**
	 * Build Configuration from Stream
	 * 
	 * @param inputStream
	 * @return Configuration
	 * @throws IOException
	 */
    private T buildConfigurationFromStream(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } finally {
            if (inputStream != null) {
              try {
                  inputStream.close();
              } catch (IOException e) {
                  logger.error("Can't close input stream", e);
              }
          }
        }
        
        ConfigurationObjectFactory factory = new ConfigurationObjectFactory(properties);
        return factory.build(clazz);
    }
}
