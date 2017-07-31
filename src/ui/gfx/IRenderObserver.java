package ui.gfx;

public interface IRenderObserver {
    void render(gameplay.Character[] characters, int playerCharacterId);
}
