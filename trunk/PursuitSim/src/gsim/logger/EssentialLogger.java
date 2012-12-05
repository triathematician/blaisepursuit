/**
 * EssentialLogger.java
 * Created on Jul 28, 2009
 */

package gsim.logger;

import java.util.ArrayList;
import java.util.List;
import sim.Simulation;
import sim.SimComponent;
import sim.component.team.Team;
import sim.metrics.VictoryCondition;
import sim.component.VisiblePlayer;

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
    public static List<SimulationLogger> getEssentialLoggersFor(Simulation sim) {
        sim.removeAllSimulationEventListeners();
        List<SimulationLogger> result = new ArrayList<SimulationLogger>();
        SimComponent[] scs = sim.getComponent();
        for (SimComponent sc : scs) {
            if (sc instanceof Team) {
                Team st = (Team) sc;
                result.add(new TeamLogger(sim, st));
                if (st.getParameters().getVictoryCondition() != VictoryCondition.NONE) {
                    result.add(new TeamMetricLogger(
                            st.getParameters().getVictoryCondition().getMetricByName(),
                            sim,
                            st.getParameters().getVictoryCondition().getMetric(),
                            st.getParameters().getVictoryCondition().getTeam1(),
                            st.getParameters().getVictoryCondition().getTeam2()
                            ));
                }
            } else if (sc instanceof VisiblePlayer) {
                result.add(new AgentLogger(sim, (VisiblePlayer) sc));
            }
        }
        return result;
    }

}
