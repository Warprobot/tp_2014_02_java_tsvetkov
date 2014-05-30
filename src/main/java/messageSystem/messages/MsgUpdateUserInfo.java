package messageSystem.messages;

import frontend.Frontend;
import messageSystem.Address;

/**
 * Created by Andrey
 * 03.04.14.
 */
public class MsgUpdateUserInfo extends MessageToFrontend{

    private String sessionId;
    private String name;
    private long userId;

    public MsgUpdateUserInfo(Address from, Address to, String sessionId, String name, long userId)
    {
        super(from, to);
        this.sessionId = sessionId;
        this.name = name;
        this.userId = userId;
    }

    public void exec(Frontend frontend)
    {
        frontend.updateUserInfo(sessionId , name, userId);
    }
}
