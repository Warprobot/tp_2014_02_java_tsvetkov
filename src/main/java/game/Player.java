package game;

import frontend.UserSession;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class Player extends UserSession{
    public Player(String id, String name, Long uid) {
        super(id, name, uid);
    }
}
