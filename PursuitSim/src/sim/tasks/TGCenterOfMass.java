/*
 * TGCenterOfMass.java
 * Created on Sep 4, 2007, 2:17:30 PM
 */
package sim.tasks;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import scio.coordinate.utils.PlanarMathUtils;
import sim.DistanceCache;
import sim.agent.PhantomAgent;
import sim.agent.SimulationAgent;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *  Player heads to center-of-mass of visible opponents.
 * </p>
 * @author Elisha Peterson
 */
public class TGCenterOfMass extends TaskerSupport {

    public TGCenterOfMass(){}

    /**
     * Constructs a generator that assigns the closets of the visible targets to the task's owner.
     * @param targetAgents list of targets
     */
    public TGCenterOfMass(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
        super(targetAgents, type);
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationAgent owner, Collection<? extends AgentSensorProxy> visibleOpponents, Collection<SimulationAgent> teammates) {
        assert visibleOpponents != null;
        assert teammates != null;

        // reduce to visible opponents only
        HashSet<AgentSensorProxy> visOpps = new HashSet<AgentSensorProxy>(visibleOpponents);
        visOpps.retainAll(targetAgents);
        if (visOpps.size() == 0) {
            return Collections.emptyList();
        }

        // finds the center of mass
        Point2D.Double com = new Point2D.Double();
        for (AgentSensorProxy asp : visOpps) {
            PlanarMathUtils.translate(com, asp.getPosition());
        }
        com.x /= visOpps.size();
        com.y /= visOpps.size();

        // return task with phantom agent target
        return Arrays.asList(new Task(owner, new PhantomAgent(com, PlanarMathUtils.ZERO, null), defaultPriority, type));
    }

    @Override
    public String toString() {
        return super.toString() + "center of mass";
    }
}
