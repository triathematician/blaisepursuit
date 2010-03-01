/*
 * TGGradient.java
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
 *  Player heads in direction to minimize sum of distances to opponents.
 * </p>
 * @author Elisha Peterson
 */
public class TGGradient extends TaskerSupport {

    public TGGradient(){}

    /**
     * Constructs a generator that provides a direction to minimize sum of distances to opponents.
     * @param targetAgents list of targets
     */
    public TGGradient(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
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

        // minimize sum of distances
        int POWER = -1;
        Point2D.Double dir = new Point2D.Double();
        for (AgentSensorProxy b : visOpps) {
            double multiplier = Math.pow(loc.distance(b.getPosition()), POWER - 1);
            PlanarMathUtils.translate(
                    dir,
                    new Point2D.Double((b.getPosition().x - loc.x) * multiplier, (b.getPosition().y - loc.y) * multiplier));
        }
        
        // return task with phantom agent target
        Point2D.Double tLoc = new Point2D.Double(loc.x + dir.x, loc.y + dir.y);
        return Arrays.asList(new Task(owner, new PhantomAgent(tLoc, PlanarMathUtils.ZERO, null), defaultPriority, type));
    }

    @Override
    public String toString() {
        return super.toString() + "gradient";
    }
}
