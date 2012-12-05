/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

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

    /** @return map of old positions */
    static int[] removeReindex(int n, List<Integer> complement) {
        int[] result = new int[n-complement.size()];
        int cur = 0;
        for (int i = 0; i < n; i++)
            if (!complement.contains(i))
                result[cur++] = i;
        return result;
    }

    /**
     * Adjusts old list of integers to new list of integers,
     * using provided list describing where old values come from
     * @param old old list of indices to convert
     * @param map array describing old indices mapping to new indices (position is new, entry is old)
     * @return new list of indices
     */
    static int[] reindex(int[] old, int[] map) {
        ArrayList<Integer> result1 = new ArrayList<Integer>();
        for (int i = 0; i < old.length; i++) {
            // if entry old[i] is an entry in map array, convert it
            for (int pos = 0; pos < map.length; pos++)
                if (old[i] == map[pos]) {
                    result1.add(pos);
                    break;
                }
        }
        // now copy to appropriate return type
        int[] result = new int[result1.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = result1.get(i);
        return result;
    }

    /**
     * Pulls out subset of points specified by COMPLEMENT of given indices.
     * @param points the points
     * @param complement the indices to REMOVE
     * @return array of points not containing those of given indices
     */
    static Point2D.Double[] deriveComplement(Point2D.Double[] points, List<Integer> complement) {
        Point2D.Double[] result = new Point2D.Double[points.length - complement.size()];
        int[] reindex = removeReindex(points.length, complement);
        for (int i = 0; i < reindex.length; i++)
            result[i] = points[reindex[i]];
        return result;
    }

    /**
     * Creates an algorithm for use with a subset of players and an existing algorithm.
     * @param a the base algorithm
     * @param n original # of players
     * @param subset the players to "remove" in the resulting algorithm
     * @return
     */
    static ComboAlgorithm deriveComplement(ComboAlgorithm a, int n, List<Integer> subset) {
        ComboAlgorithm result = new ComboAlgorithm(a.defaultAlgorithm);
        int[] reindex = removeReindex(n, subset);
        for (Entry<DistributionAlgorithm,int[]> en : a.alternates.entrySet()) {
            result.addAlternateAlgorithm(en.getKey(), reindex(en.getValue(), reindex));
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
        DistributionAlgorithm a2 = a instanceof ComboAlgorithm ? deriveComplement((ComboAlgorithm) a, n, subset) : a;
        for (int i = 0; i < nSamples; i++) {
            if (i%10==0) System.out.println("Running sample " + i + "/" + nSamples);
            ds.randomizeLocations(n);
            ds2.setPoints(deriveComplement(ds.getPoints(), subset));
            for (int j = 0; j <= nSteps; j++) {
                result[i][j] = DistributionMetrics.cooperationScore(ds, ds2, subset, true);
                ds.setPoints(a.getNewPositions(ds));
                ds2.setPoints(a2.getNewPositions(ds2));
            }
        }
        return result;
    }

    //
    // RUNNING
    //

    public static void main(String[] args) {
        Point2D.Double[] poly = DistributionScenario.DEFAULT_POLY;
        int NRUNS = 200;
        int NSTEPS = 140;
        int NPTS = 20; List<Integer> SUBSET = Arrays.asList(0);
        // set up algorthm, first 1 points use alternate algorithm
        DistributionAlgorithm ALGORITHM1 = new ComboAlgorithm(Algorithms.Go_to_Neighbor_with_Largest_Area, Algorithms.Go_to_Neighbors_Weighted_by_Difference_in_Areas, 15);
        // set up algorithm, first 1 points use altenrate algorithm
        DistributionAlgorithm ALGORITHM2 = new ComboAlgorithm(Algorithms.Go_to_Neighbors_Weighted_by_Difference_in_Areas, Algorithms.Go_to_Neighbor_with_Largest_Area, 5);
        
        double[][][] result = DistributionStats.compileCooperationScores(poly, NPTS, SUBSET, Algorithms.Go_to_Neighbor_with_Largest_Area, NRUNS, NSTEPS);
        double[][][] stats3 = new double[5][NSTEPS][8];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < NSTEPS; j++)
                stats3[i][j] = StatUtils.stats3(result, j, i);
        System.out.println("Stats 0 Array = team score { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }:");
        StatUtils.tabPrint(stats3[0]);
        System.out.println("Stats 1 Array = complement score { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }:");
        StatUtils.tabPrint(stats3[1]);
        System.out.println("Stats 2 Array = complement game score { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }:");
        StatUtils.tabPrint(stats3[2]);
        System.out.println("Stats 3 Array = selfish score { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }:");
        StatUtils.tabPrint(stats3[3]);
        System.out.println("Stats 4 Array = altruistic score { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }:");
        StatUtils.tabPrint(stats3[4]);
    }
}
