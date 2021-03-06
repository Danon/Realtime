package gameplay;

import gameplay.geometry.Rectangle;
import gameplay.physics.PhysicSimulation;
import gameplay.scene.GameMap;
import network.KeysState;
import network.Network.Command;
import ui.gfx.frame.FrameAnimation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract public class PhysicalWorld extends PhysicSimulation implements Runnable {
    private final static int TPS = 100;
    private final static double MS_PER_UPDATE = 1000.0 / TPS;

    final protected Map<Integer, Character> characters = new ConcurrentHashMap<>();
    private final Thread loopThread = new Thread(this);

    PhysicalWorld(GameMap map) {
        super(map);
    }

    final public void startLoop() {
        loopThread.start();
    }

    public void stopLoop() {
        loopThread.interrupt();
    }

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

    public void addCharacter(Character character) {
        synchronized (characters) {
            beforeAddCharacter(character);
            characters.putIfAbsent(character.characterId, character);
        }
    }

    public void addCharacters(Character[] characters) {
        synchronized (this.characters) {
            for (Character character : characters) {
                beforeAddCharacter(character);
                this.characters.putIfAbsent(character.characterId, character);
            }
        }
    }

    public void beforeAddCharacter(Character character) {
    }

    void moveCharacter(int characterId, KeysState keysState) {
        synchronized (characters) {
            Character cha = characters.get(characterId);
            cha.shared.keysState.set(keysState);
        }
    }

    void moveCharacter(int characterId, Command.ChangePlayerControls message) {
        synchronized (characters) {
            Character cha = characters.get(characterId);
            cha.shared.keysState.set(message.keysState);
            cha.shared.leftClick = message.leftClick;
            cha.shared.rightClick = message.rightClick;
        }
    }

    public void updateCharacter(Command.UpdateSharedState updateRequest) {
        synchronized (characters) {
            int charId = updateRequest.state.getCharacterId();
            characters.get(charId).shared.setValues(updateRequest.state);
        }
    }

    public void removeCharacterById(int characterId) {
        synchronized (characters) {
            characters.remove(characterId);
        }
    }

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
            this.perform(character);
            if (character.common.basicFrame == 6 * FrameAnimation.Speed.Basic) {
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
        return System.nanoTime() / 1000000.0;
    }
}
