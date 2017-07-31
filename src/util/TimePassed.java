package util;

public class TimePassed {
    private final static int NANO_IN_MICROSECONDS = 1000 * 1000;

    private final String canonicalName;
    private final long startTime;
    private double storedTime;

    public TimePassed() {
        this("unnamed");
    }

    public TimePassed(String name) {
        canonicalName = name;
        startTime = System.nanoTime();
    }

    public void storeTime() {
        storedTime = getTimeToStore();
    }

    private double getTimeToStore() {
        return (double) (System.nanoTime() - startTime) / NANO_IN_MICROSECONDS;
    }

    public double restoreTime() {
        return storedTime;
    }

    @Override
    public String toString() {
        this.storeTime();
        return String.format("%s: + %.2fms  (FPS loss: -%.2fs)", canonicalName, storedTime, 1000.0 / storedTime);
    }
}
