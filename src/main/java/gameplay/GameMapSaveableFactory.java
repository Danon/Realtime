package gameplay;

import gameplay.scene.Floor;
import gameplay.scene.Ladder;
import org.jetbrains.annotations.NotNull;
import util.save.SaveInput;
import util.save.SaveableFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameMapSaveableFactory implements SaveableFactory<GameMap> {
    @NotNull
    @Override
    public GameMap load(SaveInput input) throws IOException {
        List<Floor> floors = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        String name = input.readString();
        int width = input.readInt();
        int height = input.readInt();

        int floorsCount = input.readInt();
        for (int i = 0; i < floorsCount; i++) {
            floors.add(new Floor(
                    input.readInt(),
                    input.readInt(),
                    input.readInt()));
        }

        int laddersCount = input.readInt();
        for (int i = 0; i < laddersCount; i++) {
            ladders.add(new Ladder(
                    input.readInt(),
                    input.readInt(),
                    input.readInt()));
        }
        return new GameMap(name, width, height, floors, ladders);
    }
}
