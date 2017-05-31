package org.teknux.tinyclockin.util;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Francois EYL
 */
public class StopWatchServletRequesttUtil {

    private static String STOPWATCH_HEADER_KEY = "InternalStopWatch";

    private StopWatchServletRequesttUtil() {}

    public static void store(HttpServletRequest context, StopWatch stopWatch) {
        context.setAttribute(STOPWATCH_HEADER_KEY, stopWatch);
    }

    public static StopWatch read(HttpServletRequest context) {
        return (StopWatch) context.getAttribute(STOPWATCH_HEADER_KEY);
    }
}