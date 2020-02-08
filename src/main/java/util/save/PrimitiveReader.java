package util.save;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PrimitiveReader implements Saveable, SaveableFactory<Primitive> {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @NotNull
    @Override
    public Primitive load(@NotNull SaveInput input) throws IOException {
        return new Primitive(value = input.readInt());
    }

    @NotNull
    @Override
    public void save(@NotNull SaveOutput output, @NotNull Primitive saveable) throws IOException {
        output.writeInt(saveable.getValue());
    }
}
