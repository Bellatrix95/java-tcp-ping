package main.java.com.company.utils;

import main.java.com.company.analysis.IResponseMessageAnalysis;
import main.java.com.company.analysis.ISentMessageAnalytics;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LoggerClass configuration class
 *
 * @author Ivana Salmanić
 */
public final class LoggerClass {
    public final static java.util.logging.Logger log;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s %5$s%6$s%n");
        log = java.util.logging.Logger.getLogger("LoggerClass");
    }

    /**
     * @param sentMessageAnalytics statistics object for sent messages
     * @param responseMessageAnalysis statistics object for received messages
     * @return String value containing statistics for past time-frame
     */
    public static String formatNetworkStatisticsForLog(ISentMessageAnalytics sentMessageAnalytics, IResponseMessageAnalysis responseMessageAnalysis) {

        if(sentMessageAnalytics.getSentMessagesCount() == 0) return "Message producing has started!";

        ZonedDateTime startTime = sentMessageAnalytics.getStartTime();
        StringBuilder formatForLog = new StringBuilder(startTime.format(formatter)).append(" : ");

        formatForLog.append("numMessagesSent").append("=").append(sentMessageAnalytics.getSentMessagesCount()).append("  ");
        formatForLog.append("numMessagesLost").append("=").append(sentMessageAnalytics.getSentMessagesCount() - responseMessageAnalysis.getMessagesReceived()).append("  ");
        formatForLog.append("averageTimeAB").append("=").append(responseMessageAnalysis.getAverageTimeAB()).append("  ");
        formatForLog.append("averageTimeBA").append("=").append(responseMessageAnalysis.getAverageTimeBA()).append("  ");
        formatForLog.append("averageTimeABA").append("=").append(responseMessageAnalysis.getAverageTimeABA()).append("  ");
        formatForLog.append("maxTimeABA").append("=").append(responseMessageAnalysis.getMaxTimeABA()).append("  ");

        formatForLog.append("\n");
        return formatForLog.toString();
    }
}
