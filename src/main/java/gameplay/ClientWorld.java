package gameplay;

import network.KeysState;
import ui.IWorldUpdateObserver;
import ui.gfx.IRenderObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientWorld extends PhysicalWorld {
    private final List<IRenderObserver> renderer = new CopyOnWriteArrayList<>();
    private final List<IWorldUpdateObserver> updater = new CopyOnWriteArrayList<>();
    private final int playerCharacterId;

    public ClientWorld(GameMap map, int playerCharacterId, Character[] characters) {
        super(map);
        this.playerCharacterId = playerCharacterId;
        this.addCharacters(characters);
    }

    public void movePlayerCharacter(KeysState keysState) {
        moveCharacter(playerCharacterId, keysState);
    }

    public void addRenderObserver(IRenderObserver observer) {
        this.renderer.add(observer);
    }

    public void addUpdateObserver(IWorldUpdateObserver observer) {
        this.updater.add(observer);
    }

    @Override
    protected void whenPossible() {
        Character[] copy = charactersCopy();
        updater.forEach(IWorldUpdateObserver::worldUpdated);
        renderer.forEach(renderer -> renderer.render(copy, playerCharacterId));
    }
}
