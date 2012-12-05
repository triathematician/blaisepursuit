/*
 * EquiMetrics.java
 * Created Aug 19, 2010
 */

package equidistribution.scenario;

import simutils.Simulation;
import simutils.SimulationMetric;

/**
 * Provides metrics for scoring a simulation.
 * @author Elisha Peterson
 */
public final class EquiMetrics {

    private EquiMetrics() {} // NO instantiation
    
    /** 
     * Metric that returns a double vector containing (i) the L-infinity (max deviation),
     * (ii) the L-one (sum deviation), and (iii) the L-two (sum of squared deviations)
     * norms of differences between assigned areas and target area.
     */
    final public static SimulationMetric<double[]> L_METRICS = new SimulationMetric<double[]> () {
        public double[] value(Simulation sim, double time) {
            EquiScenario sc = (EquiScenario) sim;
            
            double ellInfinity = 0.0;
            double ellOne = 0.0;
            double ellTwo = 0.0;
            double diff = 0;

            for (EquiTeam.Agent a : sc.team.agents()) {
                diff = Math.abs(a.area - sc.targetArea());
                ellInfinity = Math.max(ellInfinity, diff);
                ellOne += diff;
                ellTwo += diff*diff;
            }
            
            return new double[] { ellInfinity, ellOne, Math.sqrt(ellTwo) };
        }
    };

    /**
     * Metric that returns a double vector containing (i) the L-infinity (max deviation),
     * (ii) the L-one (sum deviation), and (iii) the L-two (sum of squared deviations)
     * norms of differences between assigned areas and target area.
     * These metrics are further weighted by averaging according to team size (for L-one and L-two cases),
     * and by using a percentage deviation.
     */
    final public static SimulationMetric<double[]> L_METRICS_ADJUSTED = new SimulationMetric<double[]>() {
        public double[] value(Simulation sim, double time) {
            double[] scores = L_METRICS.value(sim, time);

            double targetArea = ((EquiScenario)sim).targetArea();
            double n = ((EquiScenario)sim).team.size();
            
            scores[0] /= targetArea;
            scores[1] /= (n * targetArea);
            scores[2] /= (Math.sqrt(n) * targetArea);

            return scores;
        }
    };

    /**
     * Team score weighted to have maximum value equal to team size.
     */
    final public static SimulationMetric<Double> TEAM_DEVIATION_SCORE = new SimulationMetric<Double> () {
        public Double value(Simulation sim, double time) {
            EquiScenario sc = (EquiScenario) sim;

            double total = 0.0;

            for (EquiTeam.Agent a : sc.team.agents())
                total += 1 - Math.abs(a.area/sc.targetArea() - 1);
            return total;
        }
    };

}
