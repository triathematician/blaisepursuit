/**
 * EssentialLogger.java
 * Created on Jul 28, 2009
 */

package gsim.logger;

import java.util.ArrayList;
import java.util.List;
import sim.Simulation;
import sim.SimulationComponent;
import sim.agent.SimulationTeam;
import sim.metrics.VictoryCondition;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>EssentialLogger</code> returns the "essential" loggers for a simulation, i.e. those
 *   that contain information about player locations.
 * </p>
 *
 * @author Elisha Peterson
 */
public class EssentialLogger {

    /** No instantiation for now. */
    private EssentialLogger() {}

    /**
     * Obtain a set of logging classes for the specified simulation. Also removes
     * any current loggers from the simulation.
     * @param sim the simulation
     * @return list of essential loggers (initially a list of teamlogger's)
     */
    public static List<AbstractSimulationLogger> getEssentialLoggersFor(Simulation sim) {
        sim.removeAllChangeListeners();
        List<AbstractSimulationLogger> result = new ArrayList<AbstractSimulationLogger>();
        SimulationComponent[] scs = sim.getComponent();
        for (SimulationComponent sc : scs) {
            if (sc instanceof SimulationTeam) {
                SimulationTeam st = (SimulationTeam) sc;
                result.add(new TeamLogger(sim, st));
                if (st.getParameters().getVictoryCondition() != VictoryCondition.NONE) {
                    result.add(new MetricLogger(
                            st.getParameters().getVictoryCondition().getMetricByName(),
                            sim,
                            st.getParameters().getVictoryCondition().getMetric(),
                            st.getParameters().getVictoryCondition().getTeam1(),
                            st.getParameters().getVictoryCondition().getTeam2()
                            ));
                }
            } else if (sc instanceof AgentSensorProxy) {
                result.add(new AgentLogger(sim, (AgentSensorProxy) sc));
            }
        }
        return result;
    }

}
