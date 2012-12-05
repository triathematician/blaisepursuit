/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author ae3263
 */
public class ComboAlgorithm implements DistributionAlgorithm {

    /** Default algorithm used */
    DistributionAlgorithm defaultAlgorithm;
    /** Maps alternates by indices */
    HashMap<DistributionAlgorithm, int[]> alternates;

    public ComboAlgorithm(DistributionAlgorithm defaultAlgorithm) {
        this.defaultAlgorithm = defaultAlgorithm;
        alternates = new HashMap<DistributionAlgorithm, int[]>();
    }

    /**
     * Sets up an algorithm in which the first nAlternates points use the alternate algorithm,
     * and the remainder of the team uses the default algorithm.
     * @param defaultAlgorithm
     * @param alternate
     * @param nAlternates
     */
    public ComboAlgorithm(DistributionAlgorithm defaultAlgorithm, DistributionAlgorithm alternate, int nAlternates) {
        this(defaultAlgorithm);
        int[] alt = new int[nAlternates];
        for (int i = 0; i < alt.length; i++) alt[i] = i;
        addAlternateAlgorithm(alternate, alt);
    }

    public void addAlternateAlgorithm(DistributionAlgorithm key, int[] values) {
        if (alternates.containsKey(key)) {
            List<Integer> newvalList = new ArrayList<Integer>();
            for (int val : alternates.get(key)) newvalList.add(val);
            for (int val : values) newvalList.add(val);
            int[] newval = new int[newvalList.size()];
            for (int i = 0; i < newval.length; i++)
                newval[i] = newvalList.get(i);
        } else {
            alternates.put(key, values);
        }
    }

    public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
        Point2D.Double[] result = defaultAlgorithm.getNewPositions(scenario);
        for (Entry<DistributionAlgorithm, int[]> en : alternates.entrySet()) {
            Point2D.Double[] eResult = en.getKey().getNewPositions(scenario);
            for (int i = 0; i < en.getValue().length; i++)
                result[en.getValue()[i]] = eResult[en.getValue()[i]];
        }
        return result;

    }

}
