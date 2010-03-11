/**
 * PositionCommEvent.java
 * Created on Jul 20, 2009
 */

package sim.comms;

import sim.component.VisiblePlayer;
import java.util.Collection;
import java.util.HashSet;
import sim.SimComponent;

/**
 * <p>
 *   <code>PositionCommEvent</code> represents a communication event that consists of
 *   a set of agent locations.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SensorComm extends Comm {

    HashSet<VisiblePlayer> positions;

    public SensorComm(SimComponent source, double timeOfCreation, double timeAvailable, Collection<VisiblePlayer> positions) {
        super(source, timeOfCreation, timeAvailable);
        this.positions = new HashSet<VisiblePlayer>(positions);
    }

    /** @return the positional information transmitted by this event. */
    public Collection<VisiblePlayer> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return "Time "+super.getTimeOfCreation() + "  " + positions.toString();
    }
}
