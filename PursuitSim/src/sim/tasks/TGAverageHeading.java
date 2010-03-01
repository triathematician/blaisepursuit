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
 *   Player uses the average heading of opponents
 * </p>
 * @author Elisha Peterson
 */
public class TGAverageHeading extends TaskerSupport {

    public TGAverageHeading(){}

    /**
     * Constructs a generator that assigns the closets of the visible targets to the task's owner.
     * @param targetAgents list of targets
     */
    public TGAverageHeading(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
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

        // finds the center of mass by averaging all headings
        Point2D.Double avgHeading = new Point2D.Double();
        for (AgentSensorProxy asp : visOpps) {
            PlanarMathUtils.translate(avgHeading, PlanarMathUtils.normalize((Point2D.Double) asp.getVelocity().clone()));
        }
        avgHeading.x /= visOpps.size();
        avgHeading.y /= visOpps.size();

        // return task with phantom agent target
        Point2D.Double tLoc = new Point2D.Double(owner.getPosition().x + avgHeading.x, owner.getPosition().y + avgHeading.y);
        return Arrays.asList(new Task(owner, new PhantomAgent(tLoc, PlanarMathUtils.ZERO, null), defaultPriority, type));
    }

    @Override
    public String toString() {
        return super.toString() + "average heading";
    }
}
