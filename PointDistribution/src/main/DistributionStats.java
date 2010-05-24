/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import org.bm.blaise.scio.algorithm.PolygonUtils;

/**
 *
 * @author ae3263
 */
public class DistributionStats {

    /** Compile average scores across iterations i1 through i2 using specified algorithm */
    public static void printStats(DistributionScenarioInterface ds, DistributionAlgorithm a, int i1, int i2) {
        if (i1 > i2)
            printStats(ds, a, i2, i1);
        else
            for (int i = 0; i <= i2; i++) {
                if (i >= i1 && i <= i2) {
                    System.out.println("SCORE " + i + ": " + DistributionMetrics.teamScore(ds));
                }
                ds.setPoints(a.getNewPositions(ds));
            }
    }

    /** Averages score across several iterations */
    public static double[] randomizedStats(DistributionScenarioInterface ds, int nPts, DistributionAlgorithm a, int nRep, int stepSample) {
        double[] result = new double[nRep];
        for (int i = 0; i < result.length; i++) {
            randomize(ds, nPts);
            for (int step = 0; step <= stepSample; step++) ds.setPoints(a.getNewPositions(ds));
            result[i] = DistributionMetrics.teamScore(ds);
        }
        return result;
    }

    /** Computes min/max/avg/var/dev of a list of doubles. */
    public static double[] stats(double[] list) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double avg = 0.0;
        for (double d : list) {
            min = Math.min(min, d);
            max = Math.max(max, d);
            avg += d;
        }
        avg /= list.length;

        double var = 0.0;
        for (double d : list)
            var += (d - avg)*(d - avg);
        var /= list.length;

        return new double[] { min, max, avg, var, Math.sqrt(var) };
    }

    /** Scores all available algorithms */
    public static void scoreAlgorithms(DistributionScenarioInterface ds, int nPts, int nRep, int stepSample) {
        for (DistributionAlgorithm da : Algorithms.values())
            try {
                double[] result = stats(randomizedStats(ds, nPts, da, nRep, stepSample));
                System.out.println("Algorithm " + da + ": average score = " + result[2] + " +/- " + result[4]);
            } catch (Exception ex) {}
    }

    /** Measure cooperative score of a subset of points for nSteps steps */
    public static void coopScore(DistributionScenarioInterface ds, DistributionAlgorithm a, int nSteps, List<Integer> subsetIndices) {
        Point2D.Double[] subset = new Point2D.Double[ds.getPoints().length - subsetIndices.size()];
        int j = 0;
        for (int i = 0; i < ds.getPoints().length; i++) if (!subsetIndices.contains(i)) { subset[j] = ds.getPoints(i); j++; }

        DistributionScenario ds2 = new DistributionScenario(ds.getDomain(), subset);
        for (int i = 0; i <= nSteps; i++) {
            if (i < 5 || i%10==0 || i == nSteps) {
                double t1 = DistributionMetrics.teamScore(ds);
                double t2 = DistributionMetrics.subScore(ds, subsetIndices);
                double t3 = DistributionMetrics.teamScore(ds2, ds.meanArea());
                System.out.println(String.format("SCORES @ STEP %d: (%.2f,%.2f,%.2f) => (sc,ac) = (%.2f,%.2f) ; tc = %.2f",
                        i, t1, t2, t3, t1-t2, t2-t3, t1-t3));
            }
            ds.setPoints(a.getNewPositions(ds));
            ds2.setPoints(a.getNewPositions(ds2));
        }
    }


    public static void main(String[] args) {
        DistributionScenario ds = new DistributionScenario(); randomize(ds, 15);
        Point2D.Double[] initial = ds.points;
        System.out.println("=== LARGEST (1) ===");
        coopScore(ds, Algorithms.Go_to_Neighbor_with_Largest_Area, 30, Arrays.asList(0,1,2));
        System.out.println("=== WEIGHTED (1) ===");
        ds.setPoints(initial);
        coopScore(ds, Algorithms.Go_to_Neighbors_Weighted_by_Difference_in_Areas, 30, Arrays.asList(0,1,2));
        System.out.println("=== COMBO (1 - rogue) ===");
        ds.setPoints(initial);
        coopScore(ds, Algorithms.Test_Combo, 30, Arrays.asList(0,1,2));
        System.out.println("=== COMBO (1 - team) ===");
        ds.setPoints(initial);
        coopScore(ds, Algorithms.Test_Combo, 30, Arrays.asList(3,4,5));
//        scoreAlgorithms(ds, 20, 40, 40);
    }


    static void randomize(DistributionScenarioInterface sc, int n) {
        // sets up with specified number of points, randomly generated inside the polygon and the window (-10,-10) to (10,10)
        Point2D.Double[] pts = new Point2D.Double[n];
        for (int i = 0; i < pts.length; i++) {
            pts[i] = null;
            while (pts[i] == null) {
                pts[i] = new Point2D.Double(20*Math.random()-10, 20*Math.random()-10);
                if (!PolygonUtils.inPolygon(pts[i], sc.getDomain()))
                    pts[i] = null;
            }
        }
        sc.setPoints(pts);
    }
}
