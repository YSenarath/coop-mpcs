package util;

public class DoubleKey {

    public final int X;
    public final int Y;

    public DoubleKey(final int X, final int Y) {
        this.X = X;
        this.Y = Y;
    }

    @Override
    public boolean equals(final Object O) {
        if (!(O instanceof DoubleKey)) {
            return false;
        }
        if (((DoubleKey) O).X != X) {
            return false;
        }
        return ((DoubleKey) O).Y == Y;
    }

    @Override
    public int hashCode() {
        return (X << 16) + Y;
    }
}
