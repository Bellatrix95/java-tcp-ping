package test;

import static junit.framework.TestCase.assertEquals;

public class checkServer {

//    @Test
//    public void givenClient1_whenServerResponds_thenCorrect() {
//        Pitcher client1 = new Pitcher();
//        try {
//            client1.startConnection("127.0.0.1", 6000);
//            Message message = new Message(1, ZonedDateTime.now().toInstant().toEpochMilli());
//            ByteArray byteArray = new ByteArray(50);
//            byte[] codedMessage = byteArray.fillByteArray(message);
//            byte[] msg = client1.sendMessage(codedMessage);
//            assertEquals(msg, msg); //ispravi jer ne znam sta bi trebalo vratiti jos
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void givenClient2_whenServerResponds_thenCorrect() {
//        try {
//            Pitcher client2 = new Pitcher();
//            client2.startConnection("127.0.0.1", 6000);
//            Message message = new Message(2, ZonedDateTime.now().toInstant().toEpochMilli());
//            ByteArray byteArray = new ByteArray(50);
//            byte[] codedMessage = byteArray.fillByteArray(message);
//            byte[] msg = client2.sendMessage(codedMessage);
//
//            assertEquals(msg, msg); //ispravi jer ne znam sta bi trebalo vratiti jos
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }
/*
    @Test
    public void givenLimitedResource_whenTryAcquire_shouldNotBlockIndefinitely() {
        // given
        RateLimiter rateLimiter = RateLimiter.create(1);

        // when
        rateLimiter.acquire();
        boolean result = rateLimiter.tryAcquire(2, 10, TimeUnit.MILLISECONDS);

        // then
        assertThat(result).isFalse();
    }
    */
}
