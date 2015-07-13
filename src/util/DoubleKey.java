/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Shehan
 */
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
