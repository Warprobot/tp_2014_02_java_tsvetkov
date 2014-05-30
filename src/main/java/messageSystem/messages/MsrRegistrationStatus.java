package messageSystem.messages;

import frontend.Frontend;
import messageSystem.Address;

/**
 * Created by Andrey
 * 03.04.14.
 */
public class MsrRegistrationStatus extends MessageToFrontend{

    private String sessionId;
    private Long userId;

    public MsrRegistrationStatus(Address from, Address to, String sessionId, Long userId)
    {
        super(from, to);
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public void exec(Frontend frontend)
    {
        frontend.updateStatus(sessionId, userId);
    }
}
