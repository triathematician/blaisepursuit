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

    //
    // HELPFUL MULTI-SAMPLING ALGORITHMS
    //

    /**
     * Uses the specified algorithm to compile statistics on TEAM scores.
     * @param polygon the polygon to use in the scenario
     * @param n number of points in the scenario (will be randomized every time)
     * @param algorithm the algorithm to use for the stats
     * @param nSamples number of times to repeat the scenario simulation
     * @param nSteps number of steps to run each scenario (will run nSteps times, producing nSteps+1 data points)
     * @return a double array whose (i,j) entry represents the i'th sample and j'th step
     */
    public static double[][] compileTeamScores(Point2D.Double[] polygon, int n, DistributionAlgorithm a, int nSamples, int nSteps) {
        double[][] result = new double[nSamples][nSteps+1];
        DistributionScenario ds = new DistributionScenario(polygon, n);
        double target = ds.meanArea();
        for (int i = 0; i < nSamples; i++) {
            ds.randomizeLocations(n);
            for (int j = 0; j <= nSteps; j++) {
                result[i][j] = DistributionMetrics.teamScore(ds, target);
                ds.setPoints(a.getNewPositions(ds));
            }
        }
        return result;
    }

    /**
     * Uses the specified algorithm to compile statistics on a SUBSET of player scores.
     * @param polygon the polygon to use in the scenario
     * @param n number of points in the scenario (will be randomized every time)
     * @param subset index of player to sample
     * @param algorithm the algorithm to use for the stats
     * @param nSamples number of times to repeat the scenario simulation
     * @param nSteps number of steps to run each scenario (will run nSteps times, producing nSteps+1 data points)
     * @return a double array whose (i,j) entry represents the i'th sample and j'th step
     */
    public static double[][] compileSubsetScores(Point2D.Double[] polygon, int n, List<Integer> subset, DistributionAlgorithm a, int nSamples, int nSteps) {
        double[][] result = new double[nSamples][nSteps+1];
        DistributionScenario ds = new DistributionScenario(polygon, n);
        double target = ds.meanArea();
        for (int i = 0; i < nSamples; i++) {
            ds.randomizeLocations(nSteps);
            for (int j = 0; j <= nSteps; j++) {
                result[i][j] = DistributionMetrics.sumScore(ds, subset, target);
                ds.setPoints(a.getNewPositions(ds));
            }
        }
        return result;
    }

    /**
     * Pulls out subset of points specified by COMPLEMENT of given indices.
     * @param points the points
     * @param complement the indices to REMOVE
     * @return array of points not containing those of given indices
     */
    static Point2D.Double[] deriveComplement(Point2D.Double[] points, List<Integer> complement) {
        int nPts = points.length, n = nPts - complement.size();
        Point2D.Double[] result = new Point2D.Double[n];
        int i2 = 0;
        for (int i1 = 0; i1 < nPts; i1++)
            if (!complement.contains(i1)) {
                result[i2] = points[i1];
                i2++;
            }
        return result;
    }

    /**
     * Uses the specified algorithm to compile statistics on a SUBSET's COOPERATION METRIC.
     * The algorithm is used in parallel from a random set of starting positions with n points.
     * @param polygon the polygon to use in the scenario
     * @param n number of points in the scenario (will be randomized every time)
     * @param algorithm the algorithm to use for the stats
     * @param nSamples number of times to repeat the scenario simulation
     * @param nSteps number of steps to run each scenario (will run nSteps times, producing nSteps+1 data points)
     * @param subset index of player to sample
     * @return a double array whose (i,j) entry represents the coop scores 
     *      { team score, complement-valued score, complement-participate score, selfish score, altruistic score }
     *      in the i'th sample and j'th step
     */
    public static double[][][] compileCooperationScores(Point2D.Double[] polygon, int n, List<Integer> subset, DistributionAlgorithm a, int nSamples, int nSteps) {
        double[][][] result = new double[nSamples][nSteps+1][5];
        DistributionScenario ds = new DistributionScenario(polygon, n);
        DistributionScenario ds2 = new DistributionScenario(polygon, n-subset.size());
        for (int i = 0; i < nSamples; i++) {
            ds.randomizeLocations(n);
            ds2.setPoints(deriveComplement(ds.getPoints(), subset));
            for (int j = 0; j <= nSteps; j++) {
                result[i][j] = DistributionMetrics.cooperationScore(ds, ds2, subset, true);
                ds.setPoints(a.getNewPositions(ds));
                ds2.setPoints(a.getNewPositions(ds2));
            }
        }
        return result;
    }

    //
    // RUNNING
    //

    public static void main(String[] args) {
        Point2D.Double[] poly = DistributionScenario.DEFAULT_POLY;
        int NPTS = 50;
        List<Integer> SUBSET = Arrays.asList(10);
        int NRUNS = 300;
        int NSTEPS = 200;
        double[][][] result = DistributionStats.compileCooperationScores(poly, NPTS, SUBSET, Algorithms.TEST01, NRUNS, NSTEPS);
        double[][][] stats3 = new double[5][NSTEPS][8];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < NSTEPS; j++)
                stats3[i][j] = StatUtils.stats3(result, j, i);
        System.out.println("Stats 0 Array = team score (mean-dev, mean, mean+dev:");
        StatUtils.tabPrint(stats3[0]);
        System.out.println("Stats 1 Array = complement score (mean-dev, mean, mean+dev:");
        StatUtils.tabPrint(stats3[1]);
        System.out.println("Stats 2 Array = complement game score (mean-dev, mean, mean+dev:");
        StatUtils.tabPrint(stats3[2]);
        System.out.println("Stats 3 Array = selfish score (mean-dev, mean, mean+dev:");
        StatUtils.tabPrint(stats3[3]);
        System.out.println("Stats 4 Array = altruistic score (mean-dev, mean, mean+dev:");
        StatUtils.tabPrint(stats3[4]);
    }
}
