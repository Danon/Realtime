package gameplay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Ladder {
    private final static int WIDTH = 24;

    private final int left;
    private final int bottom;
    private final int tiles;

    int getCenter() {
        return left + WIDTH / 2;
    }

    public int getRight() {
        return left + WIDTH;
    }

    public int getHeight() {
        return tiles * 32;
    }

    int getPeek() {
        return bottom + tiles * 32;
    }
}
