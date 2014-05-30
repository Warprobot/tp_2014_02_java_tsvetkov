package messageSystem.messages;

import game.GameMechanics;
import game.Player;
import messageSystem.Address;
import utils.TimeHelper;

import java.util.Date;
import java.util.Map;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class MsgStartGameSession extends MessageToGameMechanics {

    public MsgStartGameSession(Address from, Address to, Map<String, Player> players)
    {
        super(from, to);
    }

    @Override
    public void exec(GameMechanics accountService) {

    }
}
