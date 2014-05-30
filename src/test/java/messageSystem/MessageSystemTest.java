package messageSystem;

/**
 * Created by Andrey
 * 10.04.14.
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

public class MessageSystemTest {
    private MessageSystem messageSystem = new MessageSystem();

    private static Subscriber SUBSCRIBER_SEND = mock(Subscriber.class);
    private static Subscriber SUBSCRIBER_GET = mock(Subscriber.class);

    private void prepareSubscriber(Subscriber s)
    {
        when(s.getAddress()).thenReturn(new Address());
        when(s.getMessageSystem()).thenReturn(messageSystem);
        messageSystem.registerService(s);
    }

    @Before
    public void setUp() throws Exception
    {
        prepareSubscriber(SUBSCRIBER_SEND);
        prepareSubscriber(SUBSCRIBER_GET);
    }

    @Test
    public void testExecForSubscriber() throws Exception
    {
        Message msg = mock(Message.class);
        Address address = SUBSCRIBER_SEND.getAddress();
        when(msg.getTo()).thenReturn(address);

        messageSystem.sendMessage(msg);

        messageSystem.execForSubscriber(SUBSCRIBER_SEND);
        verify(msg, atLeastOnce()).exec(SUBSCRIBER_SEND);
    }

    @Test
    public void testSendMessage() throws Exception
    {
        Message msg = mock(Message.class);
        Address sendAddress = SUBSCRIBER_SEND.getAddress();
        Address getAddress = SUBSCRIBER_GET.getAddress();

        when(msg.getFrom()).thenReturn(sendAddress);
        when(msg.getTo()).thenReturn(getAddress);
        messageSystem.sendMessage(msg);
        verify(msg, atLeastOnce()).getTo();
    }

    @Test
    public void testGetAddressService() throws Exception
    {
        Assert.assertNotNull(messageSystem.getAddressService());
    }
}

