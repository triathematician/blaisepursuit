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

    /**
     * Computes individual score for player at point p. The score is 1-|area-mean|/mean.
     * @param sc scenario
     * @param p point within the scenario
     * @return individual score, using the mean score of the scenario
     */
    public static double individualScore(DistributionScenarioInterface sc, Point2D.Double p) {
        return individualScore(sc, p, sc.meanArea());
    }

    /**
     * Computes individual score for player at point p, using specified "target" score. The score is 1-|area-target|/target.
     * @param sc scenario
     * @param p point within the scenario
     * @param target target area for computation
     * @return individual score, using the specified target score
     */
    public static double individualScore(DistributionScenarioInterface sc, Point2D.Double p, double target) {
        return 1 - Math.abs(1 - sc.cellArea(p) / target);
    }

    /**
     * Computes sum of individual scores for indices specified in list
     * @param sc the scenario
     * @param subset subset of indices to use
     * @return sum of individual scores in list
     */
    public static double sumScore(DistributionScenarioInterface sc, List<Integer> subset) {
        return sumScore(sc, subset, sc.meanArea());
    }

    /**
     * Computes sum of individual scores for indices in list, using specified target score
     * @param sc the scenario
     * @param subset subset of indices to use
     * @param target target area for computation
     * @return sum of individual scores in list
     */
    public static double sumScore(DistributionScenarioInterface sc, List<Integer> subset, double target) {
        double tot = 0;
        Point2D.Double[] p = sc.getPoints();
        for (Integer i : subset) tot += individualScore(sc, p[i], target);
        return tot;
    }

    /**
     * Computes sum of individual scores for indices NOT specified in list
     * @param sc the scenario
     * @param omitted subset of indices to NOT use
     * @return sum of individual scores in list
     */
    public static double complementScore(DistributionScenarioInterface sc, List<Integer> omitted) {
        return complementScore(sc, omitted, sc.meanArea());
    }

    /**
     * Computes sum of individual scores for indices NOT in list, using specified target score
     * @param sc the scenario
     * @param omitted subset of indices to NOT use
     * @param target target area for computation
     * @return sum of individual scores in list
     */
    public static double complementScore(DistributionScenarioInterface sc, List<Integer> omitted, double target) {
        double tot = 0;
        Point2D.Double[] p = sc.getPoints();
        for (int i = 0; i < p.length; i++) if (!omitted.contains(i)) tot += individualScore(sc, p[i], target);
        return tot;
    }

    /**
     * Computes sum of all individual scores for a scenario
     * @param sc the scenario
     * @return sum of all indiviudal scores
     */
    public static double teamScore(DistributionScenarioInterface sc) {
        return teamScore(sc, sc.meanArea());
    }

    /**
     * Computes sum of all individual scores for a scenario, using specified target score.
     * @param sc the scenario
     * @param target target area for computation
     * @return sum of all indiviudal scores
     */
    public static double teamScore(DistributionScenarioInterface sc, double target) {
        double tot = 0;
        for (Point2D.Double p : sc.getPoints()) tot += individualScore(sc, p, target);
        return tot;
    }

    //
    // COOPERATION METRICS
    //

    /**
     * Computes cooperation scores for given subset of points. Requires passing in two scenarios,
     * one representative of the scenario with all the players, and one representative of the same
     * scenario with only a subset of the players.
     * 
     * @param sc1 the scenario with all players participating
     * @param sc2 the scenario with only a subset participating (the complementary subset)
     * @param subset indices for subset of players whose cooperation metrics are being analyzed
     * @param useMean1 whether to use the mean area in the first scenario for the metrics of the second
     * @return array of length 5 representing
     *      { team score, complement-valued score, complement-participate score, selfish score, altruistic score }
     */
    public static double[] cooperationScore(
            DistributionScenarioInterface sc1, DistributionScenarioInterface sc2,
            List<Integer> subset, boolean useMean1) {
        double mean1 = sc1.meanArea();
        double selfish = sumScore(sc1, subset, mean1);
        double compl = complementScore(sc1, subset, mean1);
        double subgame = teamScore(sc2, useMean1 ? mean1 : sc2.meanArea());
        return new double[] { selfish + compl, compl, subgame, selfish, compl - subgame };
    }


}
