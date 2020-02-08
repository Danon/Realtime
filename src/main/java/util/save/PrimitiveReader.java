package util.save;

import java.io.IOException;

public final class PrimitiveReader implements Saveable {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void restoreState(SaveInput input) throws IOException {
        value = input.readInt();
    }

    @Override
    public void storeState(SaveOutput output) throws IOException {
        output.writeInt(value);
    }
}
