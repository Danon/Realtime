package gameplay;

import gameplay.scene.Floor;
import gameplay.scene.Ladder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import util.save.SaveOutput;
import util.save.Saveable;
import util.save.SaveableFactory;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameMap implements Saveable {

    private String name;
    private int width;
    private int height;

    private final List<Floor> floors;
    private final List<Ladder> ladders;

    @Override
    public void storeState(SaveOutput output) {
    }

    @NotNull
    public static SaveableFactory<GameMap> factory() {
        return new GameMapSaveableFactory();
    }
}
