package test;

import main.java.com.company.analysis.IResponseMessageAnalysis;
import main.java.com.company.analysis.ISentMessageAnalytics;
import main.java.com.company.analysis.ResponseMessageAnalytics;
import main.java.com.company.analysis.SentMessageAnalytics;
import main.java.com.company.messages.Message;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static junit.framework.TestCase.assertEquals;

/**
 * SentMessageAnalytics and ResponseMessageAnalysis classes - testing. Three messages are created and added to network statistics objects so calculation could be performed.
 *
 * @author Ivana Salmanić
 */

public class AnalysisTest {
    private ISentMessageAnalytics sentMessageAnalytics = new SentMessageAnalytics();
    private IResponseMessageAnalysis responseMessageAnalysis = new ResponseMessageAnalytics(sentMessageAnalytics);

    @Test
    public void givenMessages_whenCalculate_shouldReturnNetworkStats() {
        createMessagesObjectsForAnalysisTests();

        assertEquals(sentMessageAnalytics.getSentMessagesCount(), 3);
        assertEquals(responseMessageAnalysis.getMessagesReceived(), 2);
        assertEquals(responseMessageAnalysis.getAverageTimeAB(), (float) 10.5);
        assertEquals(responseMessageAnalysis.getAverageTimeBA(), (float) 1.0);
        assertEquals(responseMessageAnalysis.getAverageTimeABA(), (float) 11.5);
        assertEquals(responseMessageAnalysis.getMaxTimeABA(), 12);
    }

    private void createMessagesObjectsForAnalysisTests() {
        ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));

        Message message1 =  new Message(startTime.toInstant().toEpochMilli(), 1);
        message1.setReceivedOnB(startTime.plusNanos(10000000).toInstant().toEpochMilli());
        message1.setSendToA(startTime.plusNanos(11000000).toInstant().toEpochMilli());
        message1.setReceivedOnA(startTime.plusNanos(12000000).toInstant().toEpochMilli());
        sentMessageAnalytics.newMessageSent(message1.getOrderNum());
        responseMessageAnalysis.newMessageReceived(message1);

        Message message2 =  new Message(startTime.toInstant().toEpochMilli(), 2);
        message2.setReceivedOnB(startTime.plusNanos(11000000).toInstant().toEpochMilli());
        message2.setSendToA(startTime.plusNanos(12000000).toInstant().toEpochMilli());
        message2.setReceivedOnA(startTime.plusNanos(13000000).toInstant().toEpochMilli());
        sentMessageAnalytics.newMessageSent(message2.getOrderNum());
        responseMessageAnalysis.newMessageReceived(message2);

        Message message3 =  new Message(startTime.toInstant().toEpochMilli(), 3);
        sentMessageAnalytics.newMessageSent(message3.getOrderNum());
    }

}
