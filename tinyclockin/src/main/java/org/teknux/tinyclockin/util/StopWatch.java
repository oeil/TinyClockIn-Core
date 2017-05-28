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

import java.time.Duration;
import java.time.LocalDateTime;


/**
 * Simple stopwatch object
 *
 * @author Francois EYL
 */
public class StopWatch {

    private LocalDateTime start;
    private LocalDateTime stop;

    public LocalDateTime start() {
        start = LocalDateTime.now();
        stop = null;
        return start;
    }

    public Duration stop() {
        stop = LocalDateTime.now();
        return getDuration();
    }

    public Duration getDuration() {
        LocalDateTime endTime = stop;
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        return Duration.between(start, endTime);
    }

    public static StopWatch get() {
        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }
}
