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

import java.util.Objects;

import javax.servlet.ServletContext;

public class UrlUtil {
    private static final String URL_SEPARATOR = "/";

    public static String getAbsoluteUrl(ServletContext servletContext, String path) {
        String contextPath = Objects.requireNonNull(servletContext, "ServletContext can not be null").getContextPath();

        if (contextPath.isEmpty()) {
            if ((path == null || path.isEmpty() || path.equals(URL_SEPARATOR))) {
                contextPath = URL_SEPARATOR;
            }
        } else if (contextPath.equals(URL_SEPARATOR)) {
            if (path != null && !path.isEmpty() && !path.equals(URL_SEPARATOR)) {
                contextPath = "";
            }
        } else {
            // ContextPath should starts with /
            if (!contextPath.startsWith(URL_SEPARATOR)) {
                contextPath = URL_SEPARATOR + contextPath;
            }
            // ContextPath Should not ends with /
            if (contextPath.endsWith(URL_SEPARATOR)) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
        }

        if (path == null || path.isEmpty() || path.equals(URL_SEPARATOR)) {
            return contextPath;
        } else {
            // Path Should starts with /
            if (!path.startsWith(URL_SEPARATOR)) {
                path = URL_SEPARATOR + path;
            }
            // Path Should not ends with /
            if (path.endsWith(URL_SEPARATOR)) {
                path = path.substring(0, path.length() - 1);
            }

            return contextPath + path;
        }
    }
}
