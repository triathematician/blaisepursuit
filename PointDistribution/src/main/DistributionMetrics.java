/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author ae3263
 */
public class DistributionMetrics {


    public static double subScore(DistributionScenarioInterface ds, List<Integer> subsetIndices) {
        double tot = 0;
        int i = 0;
        for (Point2D.Double p : ds.getPoints()) {
            if (!subsetIndices.contains(i))
                tot += individualScore(ds, p);
            i++;
        }
        return tot;
    }

    public static double individualScore(DistributionScenarioInterface sc, Point2D.Double p) {
        return 1-Math.abs(1-sc.cellArea(p)/sc.meanArea());
    }

    public static double teamScore(DistributionScenarioInterface sc) {
        double tot = 0;
        for (Point2D.Double p : sc.getPoints())
            tot += individualScore(sc, p);
        return tot;
    }

    static double teamScore(DistributionScenario sc, double meanArea) {
        double tot = 0;
        for (Point2D.Double p : sc.getPoints())
            tot += 1-Math.abs(1-sc.cellArea(p)/meanArea);
        return tot;
    }

}
