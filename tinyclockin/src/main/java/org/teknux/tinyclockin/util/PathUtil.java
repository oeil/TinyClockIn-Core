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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathUtil {
    private static final String CHARSET_UTF8 = "UTF-8";
    
    /**
     * Get Jar location
     * 
     * @return Jar directory
     */
    public static String getJarDir() {
        return getJarDir(PathUtil.class);
    }
    
    /**
     * Get Jar location
     * 
     * @param clazz
     * @return
     */
    public static String getJarDir(Class<?> clazz) {
        return decodeUrl(new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent());
    }
    
    /**
     * Decode Url
     * 
     * @param url
     * @return
     */
    private static String decodeUrl(String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLDecoder.decode(url, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
