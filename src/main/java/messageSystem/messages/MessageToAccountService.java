package messageSystem.messages;

import accountServcie.AccountService;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.Subscriber;

/**
 * Created by Andrey
 * 02.04.14.
 */
public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to)
    {
        super(from, to);
    }

    public void exec(Subscriber subscriber)
    {
        if(subscriber instanceof AccountService){
            exec( (AccountService) subscriber);
        }
    }

    public abstract void exec(AccountService accountService);
}
