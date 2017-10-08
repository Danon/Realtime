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
            out.remove(characterId);
        }

        return new Command.UpdateSharedState(c.shared);
    }

    @Override
    protected void whenPossible() {
        processInput();
    }

    private void processInput() {
        if (queue.isEmpty()) {
            return;
        }

        int characterId = this.firstElementFromSet(queue);
        WorldMessage message = queue.get(characterId);
        queue.remove(characterId);

        if (message instanceof Command.ChangePlayerControls) {
            synchronized (characters) {
                moveCharacter(characterId, (Command.ChangePlayerControls) message);
                movePendingForAcceptance(characterId, (Command.ChangePlayerControls) message);
            }
        } else if (message instanceof Command.UserLeft) {
            removeCharacterById(((Command.UserLeft) message).characterId);
        }
    }

    private int firstElementFromSet(Map<Integer, WorldMessage> map) {
        Integer[] bunchOfIds = map.keySet().toArray(new Integer[map.size()]);
        return bunchOfIds[0];
    }

    private void movePendingForAcceptance(int characterId, Command.ChangePlayerControls message) {
        synchronized (characters) {
            Character cha = characters.get(characterId);

            cha.shared.keysState.set(message.keysState);
            cha.shared.leftClick = message.leftClick;
            cha.shared.rightClick = message.rightClick;

            out.put(characterId, cha);
        }
    }
}
