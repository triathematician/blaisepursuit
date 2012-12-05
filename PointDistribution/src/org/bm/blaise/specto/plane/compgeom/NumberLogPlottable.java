/*
 * NumberLogPlottable.java
 * Created Mar 2010
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.Vector;
import visometry.plane.PlanePathPlottable;

/**
 * Displays the logged values of some number over time. The number of entries is capped.
 *
 * @param <N> a type of number to log
 * 
 * @author Elisha Peterson
 */
public class NumberLogPlottable<N extends Number> extends PlanePathPlottable {

    /** The log of values */
    private Vector<N> val2;
    /** Max # of entries */
    int MAX_ENTRIES = 300;

    public NumberLogPlottable() {
        val2 = new Vector<N>();
    }

    public NumberLogPlottable(Color color) {
        val2 = new Vector<N>();
        getStyle().setStrokeColor(color);
    }

    /** Removes all entries from the log */
    public void clear() { val2.clear(); }
    /** Adds specified value to the log, capping the size. */
    public void logValue(N v) {
        val2.add(v);
        while (val2.size() > MAX_ENTRIES)
            val2.remove(0);
        firePlottableChanged();
    }

    @Override
    protected void recompute() {
        entry.local = path = new GeneralPath();
        if (val2.size() > 0) {
            path.moveTo(0, val2.get(0).floatValue());
            for (int i = 0; i < val2.size(); i++)
                path.lineTo(i, val2.get(i).floatValue());
        }

        needsComputation = false;
        entry.needsConversion = true;
    }

}
