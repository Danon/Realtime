package gameplay;

import util.save.Savable;
import util.save.SaveInput;
import util.save.SaveOutput;

import java.util.ArrayList;
import java.util.List;

public class GameMap implements Savable {
    private String _name;
    private final List<Floor> _workFloors = new ArrayList<>();
    private final List<Ladder> _workLadders = new ArrayList<>();
    private Floor[] _floors;
    private Ladder[] _ladders;

    private int _width = -1, _height = -1;

    public GameMap(String name, int width, int height) {
        this._name = name;
        this._width = width;
        this._height = height;
    }

    public String getName() {
        return _name;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public void add(Floor floor) {
        _workFloors.add(floor);
    }

    public void add(Ladder ladder) {
        _workLadders.add(ladder);
    }

    void accept() {
        acceptFloors();
        acceptLadders();
    }

    private void acceptFloors() {
        _floors = _workFloors.toArray(new Floor[0]);
    }

    private void acceptLadders() {
        _ladders = _workLadders.toArray(new Ladder[0]);
    }

    public Floor[] getFloors() {
        return _floors;
    }

    public Ladder[] getLadders() {
        return _ladders;
    }

    @Override
    public void restoreState(SaveInput input) throws java.io.IOException {
        this._name = input.readString();
        this._width = input.readInt();
        this._height = input.readInt();

        int floorsCount = input.readInt();
        for (int i = 0; i < floorsCount; i++) {
            this.add(new Floor(
                    input.readInt(),
                    input.readInt(),
                    input.readInt()));
        }

        int laddersCount = input.readInt();
        for (int i = 0; i < laddersCount; i++) {
            this.add(new Ladder(
                    input.readInt(),
                    input.readInt(),
                    input.readInt()));
        }
    }

    @Override
    public void storeState(SaveOutput output) throws java.io.IOException {
        output.writeString(this.getName());
        output.writeInt(this.getWidth());
        output.writeInt(this.getHeight());

        output.writeInt(this.getFloors().length);
        for (Floor floor : this.getFloors()) {
            output.writeInt(floor.getLeft());
            output.writeInt(floor.getTop());
            output.writeInt(floor.getTiles());
        }

        output.writeInt(this.getLadders().length);
        for (Ladder ladder : this.getLadders()) {
            output.writeInt(ladder.getLeft());
            output.writeInt(ladder.getBottom());
            output.writeInt(ladder.getHeightTiles());
        }
    }
}
