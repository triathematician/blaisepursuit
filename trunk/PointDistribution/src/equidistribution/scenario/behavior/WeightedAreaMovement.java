/*
 * RandomMovement.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;

/**
 * Moves an agent based on a weighted sum of direction vectors to neighbor agents,
 * multiplied by a specified SCALE_PARAMETER.
 * @author Elisha Peterson
 */
public class WeightedAreaMovement implements AgentBehavior {

    public static WeightedAreaMovement DEFAULT_INSTANCE = new WeightedAreaMovement();

    double SCALE_PARAMETER = .4;
    public double getParameter() { return SCALE_PARAMETER; }
    public void setParameter(double d) { SCALE_PARAMETER = d; }

    @Override public String toString() { return "move weighted by neighbor area differences"; }

    public Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par) {
        Point2D.Double result = new Point2D.Double();
        for (Object o : neighbors) {
            Agent n = (Agent) o;
            double dArea = n.area - self.area;
            Point2D.Double summand = AlgorithmUtils.scaledUnitVector(dArea, self, n);
            result.x += summand.x;
            result.y += summand.y;
        }
        result.x *= SCALE_PARAMETER;
        result.y *= SCALE_PARAMETER;
        
        // ensure that the result's length is no more than the global max
        double length = result.distance(0, 0);
        if (length > par.MAX_MOVE) {
            result.x *= par.MAX_MOVE / length;
            result.y *= par.MAX_MOVE / length;
        }
        
        return result;
    }

}
