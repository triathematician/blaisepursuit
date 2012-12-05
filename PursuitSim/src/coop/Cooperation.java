/**
 * Cooperation.java
 * Created on Jul 24, 2009
 */
package coop;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *   <code>Cooperation</code> is a class that can measure cooperative contribution
 *   of a subset of a team within a simulation. Also coding here the minimum class requirements
 *   (as interfaces) for the values to be computable.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Cooperation {

    /** Not intended for instantiation. */
    private Cooperation() {}

    /**
     * Computes cooperative value of a simulation, with the specified utility function, for the provided team subset.
     * @param simulation
     * @param utility
     * @param team
     * @param subset
     * @return A point in cooperation space. The x-value is the <i>altruistic cooperation</i>, and the y-value is the <i>competitive cooepration</i>.
     */
    public static Point2D.Double compute(SimInterface simulation, UtilityInterface utility, TeamInterface team, SubsetInterface subset) {
        double result1, result2, result3;

        HashSet<AgentInterface> compSubset = new HashSet<AgentInterface>(team.getAgents());
        compSubset.removeAll(subset.getAgents());

        MetricInterface metric1 = utility.getInstance(team.getAgents());
        MetricInterface metric2 = utility.getInstance(compSubset);

        // first run (partial team)
        for (AgentInterface a : subset.getAgents()) {
            a.setInitiallyActive(false);
        }
        simulation.setMetrics(metric2);
        simulation.run();
        result1 = metric2.getValue(); // result with partial team, as measured by partial team
        
        // second run (entire team)
        for (AgentInterface a : subset.getAgents()) {
            a.setInitiallyActive(false);
        }
        simulation.setMetrics(metric1, metric2);
        simulation.run();
        result2 = metric2.getValue(); // result with whole team, as measured by partial team
        result3 = metric1.getValue(); // result with whole team, as measured by whole team

        return new Point2D.Double(result2 - result1, result3-result2);
    }

    interface SimInterface {
        public void setMetrics(MetricInterface... metrics);
        public void run();
    }

    interface TeamInterface {
        public Set<AgentInterface> getAgents();
    }

    interface SubsetInterface {
        public Set<AgentInterface> getAgents();
    }

    interface AgentInterface {
        public void setInitiallyActive(boolean b);
    }

    interface UtilityInterface {
        public MetricInterface getInstance(Set<AgentInterface> agents);
    }

    interface MetricInterface {
        public double getValue();
    }

}
