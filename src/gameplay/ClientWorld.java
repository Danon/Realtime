package gameplay;

import network.KeysState;
import ui.IWorldUpdateObserver;
import ui.gfx.IRenderObserver;

public class ClientWorld extends PhysicalWorld {
    private final IRenderObserver renderer;
    private final IWorldUpdateObserver updater;
    private int playerCharacterId = -1;

    public ClientWorld(IRenderObserver renderer, IWorldUpdateObserver updater) {
        this.renderer = renderer;
        this.updater = updater;
    }

    public void setMainPlayerId(int playerCharacterId) {
        this.playerCharacterId = playerCharacterId;
    }

    public void movePlayerCharacter(KeysState keysState) {
        moveCharacter(playerCharacterId, keysState);
    }

    @Override
    protected void whenPossible() {
        updater.worldUpdated();
        if (playerCharacterId != -1) {
            renderer.render(charactersCopy(), playerCharacterId);
        }
    }
}
