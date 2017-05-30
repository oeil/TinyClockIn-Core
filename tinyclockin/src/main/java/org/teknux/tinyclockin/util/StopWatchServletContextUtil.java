package org.teknux.tinyclockin.util;

import javax.servlet.ServletContext;


/**
 * @author Francois EYL
 */
public class StopWatchServletContextUtil {

    private static String STOPWATCH_HEADER_KEY = "InternalStopWatch";

    private StopWatchServletContextUtil() {}

    public static void store(ServletContext context, StopWatch stopWatch) {
        context.setAttribute(STOPWATCH_HEADER_KEY, stopWatch);
    }

    public static StopWatch read(ServletContext context) {
        return (StopWatch) context.getAttribute(STOPWATCH_HEADER_KEY);
    }
}