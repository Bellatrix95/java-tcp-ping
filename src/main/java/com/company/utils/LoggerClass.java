package main.java.com.company.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Logger configuration class
 *
 * @author Ivana Salmanić
 */
public final class LoggerClass {
    public final static Logger log;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s %5$s%6$s%n");
        log = Logger.getLogger("LoggerClass");
    }

    /* change somehow */
    /**
     * @param statsForLastTimeFrame statistics object that needs to be logged
     * @return String value containing statistics for past time-frame
     */
    public static String parseStatisticsObject(Map<String, Object> statsForLastTimeFrame) {
        ZonedDateTime startTime = (ZonedDateTime) statsForLastTimeFrame.get("startTime");
        StringBuilder formatForLog = new StringBuilder(startTime.format(formatter)).append(" : ");

        statsForLastTimeFrame.remove("startTime");

        for (String key : statsForLastTimeFrame.keySet()) {
            formatForLog.append(key).append("=").append(statsForLastTimeFrame.get(key)).append("  ");
        }

        formatForLog.append("\n");
        return formatForLog.toString();
    }
}
