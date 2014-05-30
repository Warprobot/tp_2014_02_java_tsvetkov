package messageSystem;

/**
 * Created by Andrey
 * 02.04.14.
 */
public abstract class Message {
    private final Address from;
    private final Address to;

    public Message(Address _from, Address _to)
    {
        from = _from;
        to = _to;
    }

    protected Address getFrom()
    {
        return from;
    }

    protected Address getTo()
    {
        return to;
    }

    public abstract void exec(Subscriber subscriber);
}
