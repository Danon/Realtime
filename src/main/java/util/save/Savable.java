package util.save;

public interface Savable {
    void restoreState(SaveInput input) throws java.io.IOException;

    void storeState(SaveOutput output) throws java.io.IOException;
}
