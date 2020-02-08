package gameplay;

import gameplay.scene.Floor;
import gameplay.scene.GameMap;
import gameplay.scene.Ladder;
import org.jetbrains.annotations.NotNull;
import util.save.SaveInput;
import util.save.SaveOutput;
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

    @Override
    public void save(@NotNull SaveOutput output, @NotNull GameMap saveable) throws IOException {
        output.writeString(saveable.getName());
        output.writeInt(saveable.getWidth());
        output.writeInt(saveable.getHeight());

        output.writeInt(saveable.getFloors().size());
        for (Floor floor : saveable.getFloors()) {
            output.writeInt(floor.getLeft());
            output.writeInt(floor.getTop());
            output.writeInt(floor.getTiles());
        }

        output.writeInt(saveable.getLadders().size());
        for (Ladder ladder : saveable.getLadders()) {
            output.writeInt(ladder.getLeft());
            output.writeInt(ladder.getBottom());
            output.writeInt(ladder.getTiles());
        }
    }
}
