/**
 * PositionCommEvent.java
 * Created on Jul 20, 2009
 */

package sim.comms;

import sim.agent.AgentSensorProxy;
import java.util.Collection;
import java.util.HashSet;
import sim.SimulationComponent;

/**
 * <p>
 *   <code>PositionCommEvent</code> represents a communication event that consists of
 *   a set of agent locations.
 * </p>
 *
 * @author Elisha Peterson
 */
public class PositionCommEvent extends CommEvent {

    HashSet<AgentSensorProxy> positions;

    public PositionCommEvent(SimulationComponent source, double timeOfCreation, double timeAvailable, Collection<AgentSensorProxy> positions) {
        super(source, timeOfCreation, timeAvailable);
        this.positions = new HashSet<AgentSensorProxy>(positions);
    }

    /** @return the positional information transmitted by this event. */
    public Collection<AgentSensorProxy> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return "Time "+super.getTimeOfCreation() + "  " + positions.toString();
    }
}
