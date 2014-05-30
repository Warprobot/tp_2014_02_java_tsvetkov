package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Andrey
 * 02.04.14.
 */
public class Address {
    private static AtomicInteger subscriberIdGenerator = new AtomicInteger();
    private final int subscriberId;

    public Address()
    {
        subscriberId = subscriberIdGenerator.incrementAndGet();
    }

    public int hashCode()
    {
        return subscriberId;
    }
}
