package ui.gfx;

/**
 * RenderObserver.java
 * 
 * @author Danio
 * @version 1.0 09/04/2015
 */
public interface IRenderObserver 
{
    void render(gameplay.Character[] characters, int playerCharacterId);
}
