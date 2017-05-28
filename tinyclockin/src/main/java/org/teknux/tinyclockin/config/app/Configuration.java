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

package org.teknux.tinyclockin.config.app;

import org.skife.config.Config;
import org.skife.config.Default;


public interface Configuration {

    @Config("basePath")
    @Default("/")
    String getBasePath();

    @Config("ssl")
    @Default("false")
    boolean isSsl();

    @Config("port")
    @Default("8080")
    int getPort();

    @Config("database.file.path")
    @Default("~/database")
    String getDatabaseFilePath();

    @Config("admin.key")
    @Default("admin")
    String getAdminKey();
}
