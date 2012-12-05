/**
 * AutonomousTaskGenerator.java
 * Created on May 9, 2008
 */

package tasking;

import java.util.Collection;
import java.util.Vector;
import scio.coordinate.V2;
import simulation.Agent;
import simulation.Team;
import utility.DistanceTable;

/**
 * Autonomous tasking allows each individual player to determine their own tasking. As such, this requires an
 * individual agent method for generating tasks rather than an entire team method.
 * @author Elisha Peterson
 */
public abstract class AutonomousTaskGenerator extends TaskGenerator {
    
    public AutonomousTaskGenerator(Team target,int type){super(target,type);}

    public abstract V2 generate(Agent agent, DistanceTable table);
    
    public void generate(Collection<Agent> team, DistanceTable table, double weight, double threshold, double exponent) {
        V2 result;
        for(Agent a:team){
            result = generate(a, table);
            if (result != null) {
                a.assign(new Task(this, result, type, weight, threshold, exponent));
            }
        }
    }
}
