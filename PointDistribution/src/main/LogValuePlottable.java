/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;
import java.util.Vector;
import org.bm.blaise.specto.plottable.VPath;
import org.bm.blaise.specto.visometry.VisometryGraphics;

/**
 * Represents a plottable that displays the log of a given value on a plot.
 * @author ae3263
 */
public class LogValuePlottable extends VPath<Point2D.Double> {

    public LogValuePlottable() {
        super(new Point2D.Double[]{});
        clear();
    }

    Vector<Double> val2;

    public void clear() {
        val2 = new Vector<Double>();
    }

    int MAX_ENTRIES = 300;

    public void logValue(double v) {
        if (val2.size() > MAX_ENTRIES)
            val2.remove(0);
        val2.add(v);
    }

    @Override
    public void draw(VisometryGraphics<Point2D.Double> vg) {
        values = new Point2D.Double[val2.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = new Point2D.Double(i, val2.get(i));
        super.draw(vg);
    }


}
