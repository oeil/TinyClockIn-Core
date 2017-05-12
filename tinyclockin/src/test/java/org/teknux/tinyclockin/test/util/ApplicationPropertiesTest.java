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

package org.teknux.tinyclockin.test.util;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.tinyclockin.exception.AppException;
import org.teknux.tinyclockin.util.ApplicationProperties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationPropertiesTest {
    @Test(expected = AppException.class)
    public void test01badPropertiesFile() throws AppException {
        new ApplicationProperties("/bad-application.properties");
    }

    @Test
    public void test02propertiesFile() throws AppException {
        ApplicationProperties applicationProperties = new ApplicationProperties("/application.properties");
        Assert.assertEquals("nameValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_NAME_KEY));
        Assert.assertEquals("versionValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_VERSION_KEY));
    }

    @Test
    public void test03propertiesFileStatic() throws AppException {
        ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
        Assert.assertEquals("nameValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_NAME_KEY));
        Assert.assertEquals("versionValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_VERSION_KEY));
    }
}
