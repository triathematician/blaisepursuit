/*
 * RandomMovement.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * Moves an agent at random. Parameter is distance of movement.
 * @author Elisha Peterson
 */
public class RandomMovement implements AgentBehavior {

    public static RandomMovement INSTANCE = new RandomMovement();

    private RandomMovement() {}

    @Override public String toString() { return "random movement"; }

    public Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par) {
        return PlanarMathUtils.toCartesianFromPolar(par.MAX_MOVE, 2*Math.PI*Math.random());
    }

}
