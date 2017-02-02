package gameplay;

import network.KeysState;
import network.Network.Command;
import ui.gfx.FrameAnimation;

import java.awt.*;
import java.util.HashMap;

/**
 * PhysicalWorld.java
 * <p>
 * Base class with physics world for game, used by both Client-Part and
 * Server-Part. Contains physical boundries and physics symulation, like
 * blocking the movement outside the map and jumps.
 * <p>
 * Invokes <code>update();</code> exactly <code>TPS</code> times a second. In
 * spare time between updates invokes <code>whenPossible();</code>.
 * <p>
 * <code>HasMap characters;</code> is not Thread-Safe
 * <p>
 * API:
 * Constructor(Integer);
 * startLoop();
 * addCharacter(Character);
 * moveCharacter(Character);
 * moveCharacter(Integer, Command.MoveCharacter)
 * removeCharacter(Character);
 *
 * @author Danio
 * @version 1.0 03/04/2015
 */

abstract public class PhysicalWorld extends PhysicSimulation implements Runnable {
    public final static int TPS = 100;
    final static double MS_PER_UPDATE = 1000 / TPS;
    public final Insets boundries = new Insets(480, 0, 0, 640);
    final protected HashMap<Integer, Character> characters = new HashMap<>();
    final protected Thread loopThread;

    /**
     * Constructor
     */
    public PhysicalWorld() {
        loopThread = new Thread(this, "LoopThread");
    }

    /**
     * Start physics simulation engine.
     */
    final public void startLoop() {
        loopThread.start();
    }

    /**
     * Physics loop. Invokes <code>update();</code> exactly <code>TPS</code> times
     * a second. <code>whenPossible</code> in spare time
     * between updates.
     */
    @Override
    final public void run() {

        double previous = millisecondsPassed();
        double lag = 0.0;

        while (true) {
            double current = millisecondsPassed();
            double elapsed = current - previous;
            previous = current;
            lag += elapsed;

            whenPossible();
            while (lag >= MS_PER_UPDATE) {
                synchronized (characters) {
                    updateGameState();
                }
                lag -= MS_PER_UPDATE;
            }
            synchronized (this) {
                this.notify();
            }
        }
    }

    /**
     * Thread-safe addition of character to the simulation.
     *
     * @param character Character to be added to the simulation.
     */
    public void addCharacter(Character character) {
        synchronized (characters) {
            character.shared.hp = 500;
            character.shared.y = 32;
            character.shared.x = Math.random() * (this.getMapWidth() - 200) + 100;
            characters.putIfAbsent(character.characterId, character);
        }
    }

    /**
     * Thread-safe moving a character on the same machine.
     *
     * @param characterId Id of a character to be moved
     * @param keysState   keysState to be updated.
     */
    void moveCharacter(int characterId, KeysState keysState) {
        synchronized (characters) {
            Character cha = characters.get(characterId);
            cha.shared.keysState.set(keysState);
        }
    }

    /**
     * Thread-safe moving a character.
     *
     * @param characterId Id of a character to be moved
     * @param message     Command containing move information.
     */
    void moveCharacter(int characterId, Command.ChangePlayerControls message) {
        synchronized (characters) {
            Character cha = characters.get(characterId);
            cha.shared.keysState.set(message.keysState);
            cha.shared.leftClick = message.leftClick;
            cha.shared.rightClick = message.rightClick;
        }
    }

    /**
     * Thread-safe moving a character.
     *
     * @param updateRequest Command containing move information.
     */
    public void updateCharacter(Command.UpdateSharedState updateRequest) {
        synchronized (characters) {
            int charId = updateRequest.state.getCharacterId();
            characters.get(charId).shared.setValues(updateRequest.state);
        }
    }

    /**
     * Thread-safe deleting of a character.
     *
     * @param characterId Id of a character to delete.
     */
    public void removeCharacterById(int characterId) {
        synchronized (characters) {
            characters.remove(characterId);
        }
    }

    /**
     * Thread-safe deep copy characters.
     *
     * @return Array of characters. Containing deep copy of all characters in
     * game, cause of thread-safety.
     */
    Character[] charactersCopy() {
        Character[] result;
        synchronized (characters) {
            result = new Character[characters.size()];
            int i = 0;
            for (Character character : characters.values()) {
                result[i++] = character.copy();
            }
        }

        return result;
    }

    private void updateGameState() {
        for (Character character : characters.values()) {
            this.step(character);
            if (character.common.basicFrame == 6 * FrameAnimation.FrameAnimationSpeed.Basic) {
                Rectangle hitZone;
                if (character.isTurnedRight()) {
                    hitZone = new Rectangle(
                            character.getX() + 6,
                            character.getY() - 14,
                            58, 67
                    );
                } else {
                    hitZone = new Rectangle(
                            character.getX() - 6 - 58,
                            character.getY() - 14,
                            58, 67
                    );
                }

                for (Character enemy : characters.values()) {
                    if (hitZone.contains(enemy.getPosition())) {
                        enemy.shared.hp -= 25;
                    }
                }
            }
        }
    }

    protected abstract void whenPossible();

    private double millisecondsPassed() {
        return System.nanoTime() / 1000000;
    }

}
