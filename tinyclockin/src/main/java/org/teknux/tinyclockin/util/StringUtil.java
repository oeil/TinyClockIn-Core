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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Francois EYL
 */
public class StringUtil {

    /**
     * Returns the hexadecimal representation of the given byte array as a String.
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0'); // $NON-NLS-1$
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Calculate the MD5 hash for the given String and returns and byte array
     * for the given {@link Charset}.
     *
     * @param content
     *            to calculate MD5 hash
     * @return
     */
    public static byte[] md5(final String content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            return md5.digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            // should never happen since MD5 is a supported algo in every JRE
            return null;
        }
    }
}
