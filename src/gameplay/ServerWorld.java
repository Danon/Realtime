package gameplay;

import network.Network.Command;
import network.Network.WorldMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class ServerWorld extends PhysicalWorld {
    final private Map<Integer, Character> out = new HashMap<>();
    final private Map<Integer, WorldMessage> queue = new ConcurrentHashMap<>();

    synchronized public void messageArrived(int characterId, WorldMessage message) {
        queue.put(characterId, message);
    }

    public Command.UpdateSharedState waitForAcceptance(int characterId) {
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException ignored) {
        }

        Character c;
        synchronized (out) {
            c = out.get(characterId);
        }

        return new Command.UpdateSharedState(c.shared);
    }

    private void MovePendingForAcceptance(int characterId, Command.ChangePlayerControls message) {
        synchronized (characters) {
            Character cha = characters.get(characterId);

            cha.shared.keysState.set(message.keysState);
            cha.shared.leftClick = message.leftClick;
            cha.shared.rightClick = message.rightClick;

            out.put(characterId, cha);
        }
    }

    private int firstElementFromSet(Map<Integer, WorldMessage> map) {
        Integer[] bunchOfIds = map.keySet().toArray(new Integer[map.size()]);
        return bunchOfIds[0];
    }

    private void processInput() {
        if (queue.isEmpty()) {
            return;
        }

        int characterId = this.firstElementFromSet(queue); // take first character's id
        WorldMessage message = queue.get(characterId);     // take a message for that character

        if (message instanceof Command.ChangePlayerControls) {
            synchronized (characters) {
                moveCharacter(characterId, (Command.ChangePlayerControls) message);
                MovePendingForAcceptance(characterId, (Command.ChangePlayerControls) message);
            }
        } else if (message instanceof Command.UserLeft) {
            removeCharacterById(((Command.UserLeft) message).characterId);
        }
    }

    @Override
    protected void whenPossible() {
        processInput();
    }
}
