/*
 * TGAverageHeading.java
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
 *   Player uses an average heading of opponents, weighted by the distance from the owner
 * </p>
 * @author Elisha Peterson
 */
public class TGWeightedAverageHeading extends TaskerSupport {

    public TGWeightedAverageHeading(){}

    /**
     * Constructs a generator that assigns the closets of the visible targets to the task's owner.
     * @param targetAgents list of targets
     */
    public TGWeightedAverageHeading(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
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

        Point2D.Double loc = owner.getPosition();

        // finds the center of mass
        Point2D.Double avgHeading = new Point2D.Double();
        Point2D.Double heading;
        double dist;
        for (AgentSensorProxy asp : visOpps) {
            heading = PlanarMathUtils.normalize((Point2D.Double) asp.getVelocity().clone());
            dist = asp.getPosition().distance(loc);
            avgHeading.x += heading.x / dist;
            avgHeading.y += heading.y / dist;
        }
        avgHeading.x /= visOpps.size();
        avgHeading.y /= visOpps.size();

        // return task with phantom agent target
        Point2D.Double tLoc = new Point2D.Double(loc.x + avgHeading.x, loc.y + avgHeading.y);
        return Arrays.asList(new Task(owner, new PhantomAgent(tLoc, PlanarMathUtils.ZERO, null), defaultPriority, type));
    }

    @Override
    public String toString() {
        return super.toString() + "weighted average heading";
    }
}
