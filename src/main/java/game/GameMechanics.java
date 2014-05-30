package game;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class GameMechanics implements Runnable, Subscriber {

    private final MessageSystem messageSystem;
    private final Address address;
    private final Map<String, Player> players = new HashMap<>();

    public GameMechanics(MessageSystem ms)
    {
        messageSystem = ms;
        address = new Address();
        messageSystem.registerService(this);
    }


    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
            messageSystem.execForSubscriber(this);
        }
    }
}
