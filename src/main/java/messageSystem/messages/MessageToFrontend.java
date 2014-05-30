package messageSystem.messages;

import frontend.Frontend;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.Subscriber;

/**
 * Created by Andrey
 * 02.04.14.
 */
public abstract class  MessageToFrontend extends Message{
    public MessageToFrontend(Address from, Address to)
    {
        super(from, to);
    }

    public void exec(Subscriber subscriber)
    {
        if(subscriber instanceof Frontend){
            exec( (Frontend) subscriber);
        }
    }

    public abstract void exec(Frontend frontend);
}
