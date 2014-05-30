package messageSystem.messages;

import accountServcie.AccountService;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Andrey
 * 02.04.14.
 */
public class MsgGetUserId extends MessageToAccountService{

    private String name;
    private String sessionId;

    public MsgGetUserId(Address from, Address to, String name, String sessionId)
    {
        super(from, to);
        this.name = name;
        this.sessionId = sessionId;
    }

    public void exec(AccountService accountService)
    {
        long id = accountService.getUserId(name);
        Message back = new MsgUpdateUserId(getTo(), getFrom(), sessionId, id);
        accountService.getMessageSystem().sendMessage(back);
    }
}
