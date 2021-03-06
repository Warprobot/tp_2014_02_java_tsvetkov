package messageSystem.messages;

import frontend.Frontend;
import messageSystem.Address;

/**
 * Created by Andrey
 * 02.04.14.
 */
public class MsgUpdateUserId extends MessageToFrontend{

    private String sessionId;
    private long userId;

    public MsgUpdateUserId(Address from, Address to, String sessionId, long userId)
    {
        super(from, to);
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public void exec(Frontend frontend)
    {
        frontend.updateUserId(sessionId , userId);
    }

}
