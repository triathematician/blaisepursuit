/**
 * DistanceCache.java
 * Created on Jul 20, 2009
 */
package sim;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import scio.coordinate.utils.PlanarMathUtils;
import sim.agent.AgentSensorProxy;
import scio.matrix.HashHashMatrix;

/**
 * <p>
 *   <code>DistanceCache</code> is a convenience/efficiency class for getting/receiving hhm
 *   between agents in the simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class DistanceCache {

    HashHashMatrix<AgentSensorProxy, AgentSensorProxy, Double> hhm;

    /** Sets up distance table using the specified simulation. */
    public DistanceCache(Simulation sim) {
        Object[] agentArr = sim.getAllAgents().toArray();
        hhm = new HashHashMatrix<AgentSensorProxy, AgentSensorProxy, Double>(agentArr, agentArr);
    }

    /** Recalculates hhm based on current time step.
     * @param simTime the current simulation time */
    public void recalculate(double simTime) {
        // TODO (later) - increase efficiency
        for (AgentSensorProxy a : hhm.getRows())
            for (AgentSensorProxy b : hhm.getCols())
                hhm.put(a, b, a.getPosition().distance(b.getPosition()));
    }
    
    /**
     * Gives distance between two agents.
     * @param a the first agent
     * @param b the second agent
     * @return the distance between the agents
     */
    public double get(AgentSensorProxy a, AgentSensorProxy b) {
        return hhm.get(a,b);
    }

    //
    // GETTER METHODS
    //

    /**
     * @return all agents in the simulation
     */
    public Set<AgentSensorProxy> getAllAgents() {
        return new HashSet<AgentSensorProxy>(hhm.getRows());
    }

    /**
     * Returns all agents within a specified radius of the provided position.
     * @param position the central position
     * @param radius the radius of the sensor
     * @return list of all agents within that disk
     */
    public Set<AgentSensorProxy> getAgentsInRadius(Point2D.Double position, double radius) {
        HashSet<AgentSensorProxy> result = new HashSet<AgentSensorProxy>();
        for (AgentSensorProxy b : hhm.getRows())
            if (b.getPosition().distance(position) <= radius)
                result.add(b);
        return result;
    }

    /** @return values between -2pi and 2pi */
    private static double relativeBearing(Point2D.Double pos, Point2D.Double vel, Point2D.Double pos2) {
        double angle1 = PlanarMathUtils.angle(vel); // -pi to pi
        double angle2 = Math.atan2(pos2.y - pos.y, pos2.x - pos.x); // -pi to pi
        return (angle2 - angle1) % (2*Math.PI);
    }
    
    /**
     * Returns all agents within a specified radius and relative angle of the provided position.
     * @param position the central position
     * @param radius the radius of the sensor
     * @param velocity the velocity of the agent
     * @param angle the angle to search within, relative to the heading (plus or minus)
     * @return list of all agents within that disk
     */
    public Set<AgentSensorProxy> getAgentsInWedge(Point2D.Double position, double radius, Point2D.Double velocity, double theta) {
        HashSet<AgentSensorProxy> result = new HashSet<AgentSensorProxy>();
        for (AgentSensorProxy b : hhm.getRows())
            if (b.getPosition().distance(position) <= radius && Math.abs(relativeBearing(position, velocity, b.getPosition())) < theta)
                result.add(b);
        return result;
    }

    /**
     *
     * @return closest agent within provided list to the agent, or <code>null</code> if there are no agents
     */
    public AgentSensorProxy getClosestAgent(AgentSensorProxy a, Collection<? extends AgentSensorProxy> s2) {
        assert a != null;
        assert a.getPosition() != null;
        assert s2 != null;

        Point2D.Double ownerPos = a.getPosition();
        double minDist = Double.MAX_VALUE;
        double testDist;
        AgentSensorProxy closest = null;
        for (AgentSensorProxy asp : s2) {
            testDist = ownerPos.distance(asp.getPosition());
            if (testDist < minDist && asp.isActive()) {
                closest = asp;
                minDist = testDist;
            }
        }
        return closest;
    }

    /**
     * Use the cache to generate a greedy assignment of elements of ta to elements of tb,
     * assigning the smallest paired distance first.
     * @param ta the first team
     * @param tb the second team
     * @return a map of assignments between the agents
     */
    public Map<AgentSensorProxy, AgentSensorProxy> getAssignmentMapByClosestFirst(Collection<? extends AgentSensorProxy> s1, Collection<? extends AgentSensorProxy> s2) {
        assert s1 != null;
        assert s2 != null;

        HashMap<AgentSensorProxy, AgentSensorProxy> result = new HashMap<AgentSensorProxy, AgentSensorProxy>();

        Vector<AgentSensorProxy> ag1 = new Vector<AgentSensorProxy>(s1);
        Vector<AgentSensorProxy> ag2 = new Vector<AgentSensorProxy>(s2);
        AgentSensorProxy closest1 = null, closest2 = null;
        double closestDist = Double.MAX_VALUE;
        double testDist;

        while (!ag1.isEmpty() && !ag2.isEmpty()) {
            for (AgentSensorProxy asp1 : ag1) {
                for (AgentSensorProxy asp2 : ag2) {
                    testDist = hhm.get(asp1, asp2);
                    if (testDist < closestDist) {
                        closestDist = testDist;
                        closest1 = asp1;
                        closest2 = asp2;
                    }
                }
            }
            result.put(closest1, closest2);
            ag1.remove(closest1);
            ag2.remove(closest2);
            closestDist = Double.MAX_VALUE;
        }
        return result;
    }



    /** @return average distance between agents in the two sets, 0.0 if one of the sets is empty */
    public double getAverageDistance(Collection<? extends AgentSensorProxy> s1, Collection<? extends AgentSensorProxy> s2) {
        assert s1 != null;
        assert s2 != null;

        if (s1.size() == 0 || s2.size() == 0) {
            return 0.0;
        }

        double tot = 0.0;
        for (AgentSensorProxy sa : s1) {
            for (AgentSensorProxy sb : s2) {
                tot += hhm.get(sa, sb);
            }
        }
        tot = tot / (s1.size() * s2.size());
        return tot;
    }

    /** @return maximum distance between agents in the two sets, -1.0 if one of the sets is empty */
    public double getMaximumDistance(Collection<? extends AgentSensorProxy> s1, Collection<? extends AgentSensorProxy> s2) {
        assert s1 != null;
        assert s2 != null;

        if (s1.size() == 0 || s2.size() == 0) {
            return -1.0;
        }

        double curMax = 0.0;
        double testDist;
        AgentSensorProxy cura = null;
        AgentSensorProxy curb = null;

        for (AgentSensorProxy sa : s1) {
            for (AgentSensorProxy sb : s2) {
                testDist = hhm.get(sa, sb);
                if (testDist > curMax) {
                    curMax = testDist;
                    cura = sa;
                    curb = sb;
                }
            }
        }

        return curMax;
    }

    /** @return minimum distance between agents in the two sets, -1.0 if one of the sets is empty */
    public double getMinimumDistance(Collection<? extends AgentSensorProxy> s1, Collection<? extends AgentSensorProxy> s2) {
        assert s1 != null;
        assert s2 != null;

        if (s1.size() == 0 || s2.size() == 0) {
            return -1.0;
        }

        double curMin = Double.MAX_VALUE;
        double testDist;
        AgentSensorProxy cura = null;
        AgentSensorProxy curb = null;

        for (AgentSensorProxy sa : s1) {
            for (AgentSensorProxy sb : s2) {
                testDist = hhm.get(sa, sb);
                if (testDist < curMin) {
                    curMin = testDist;
                    cura = sa;
                    curb = sb;
                }
            }
        }

        return curMin;
    }

    @Override public String toString() { return hhm.toString(); }
}
