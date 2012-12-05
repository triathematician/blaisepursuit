/**
 * DistanceCache.java
 * Created on Jul 20, 2009
 */
package sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import scio.coordinate.utils.PlanarMathUtils;
import sim.component.VisiblePlayer;

/**
 * <p>
 *   <code>DistanceCache</code> is a convenience/efficiency class for getting/receiving hhm
 *   between agents in the simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class DistanceCache {

    int n; // # of players
    List<VisiblePlayer> players;
    Set<VisiblePlayer> activePlayers;
    List<Integer> activeIndices;
    double[][] positions;
    double[][] distances;

    /** Sets up distance table using the specified simulation. */
    public DistanceCache(Simulation sim) {
        players = sim.getAllPlayers();
        n = players.size();
        activePlayers = new HashSet<VisiblePlayer>();
        activePlayers.addAll(players);
        activeIndices = new ArrayList<Integer>();
        for (int i = 0; i < n; i++)
            activeIndices.add(i);
        positions = new double[n][2];
        distances = new double[n][n];
    }

    @Override
    public String toString() {
        return Arrays.deepToString(distances);
    }

    /** Recalculates table of distances based on current time step.
     * @param simTime the current simulation time */
    public void recalculate(double simTime) {
        // calculate active agents
        activePlayers.clear();
        activeIndices.clear();
        for (int i = 0; i < n; i++)
            if (players.get(i).isActive()) {
                activePlayers.add(players.get(i));
                activeIndices.add(i);
            }
        // store positions
        Point2D.Double loc = null;
        for (int i = 0; i < n; i++) {
            loc = players.get(i).getPosition();
            positions[i][0] = loc.x;
            positions[i][1] = loc.y;
        }
        // calculate distances
        double dx, dy;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                dx = positions[i][0] - positions[j][0];
                dy = positions[i][1] - positions[j][1];
                distances[i][j] = Math.sqrt(dx*dx + dy*dy);
                distances[j][i] = distances[i][j];
            }
    }
    
    /**
     * Gives distance between two agents.
     * @param a the first agent
     * @param b the second agent
     * @return the distance between the agents
     */
    public double getDistance(VisiblePlayer a, VisiblePlayer b) {
        return distances[players.indexOf(a)][players.indexOf(b)];
    }

    //
    // METHODS TO FIND AGENTS BY POSITION
    //

    /**
     * Returns all active agents.
     */
    public Set<VisiblePlayer> getAllAgents() {
        return activePlayers;
    }
    

    /**
     * Returns all agents within a specified radius of the provided position.
     * @param vp the player
     * @param radius the radius of the sensor
     * @return list of all agents within that disk
     */
    public Set<VisiblePlayer> getAgentsInRadius(VisiblePlayer vp, double radius) {
        HashSet<VisiblePlayer> result = new HashSet<VisiblePlayer>();
        int i = players.indexOf(vp);
        for (int j : activeIndices)
            if (distances[i][j] < radius)
                result.add(players.get(j));
        return result;
    }

    /** @return values between -2pi and 2pi */
    static double relativeBearing(Point2D.Double pos, Point2D.Double vel, Point2D.Double pos2) {
        double angle1 = PlanarMathUtils.angle(vel); // -pi to pi
        double angle2 = Math.atan2(pos2.y - pos.y, pos2.x - pos.x); // -pi to pi
        return (angle2 - angle1) % (2*Math.PI);
    }
    
    /**
     * Returns all agents within a specified radius and relative angle of the provided position.
     * @param vp the player
     * @param radius the radius of the sensor
     * @param theta the angle to search within, relative to the heading (plus or minus)
     * @return list of all agents within that disk
     */
    public Set<VisiblePlayer> getAgentsInWedge(VisiblePlayer vp, double radius, double theta) {
        HashSet<VisiblePlayer> result = new HashSet<VisiblePlayer>();
        int i = players.indexOf(vp);
        for (int j : activeIndices)
            if (distances[i][j] < radius &&
                    Math.abs(relativeBearing(vp.getPosition(), vp.getVelocity(), players.get(j).getPosition())) < theta)
                result.add(players.get(j));
        return result;
    }

    //
    // METHODS TO FIND AGENTS BY TEAM
    //

    int[] getPlayerIndices(Collection<? extends VisiblePlayer> team) {
        int[] result = new int[team.size()];
        int i = 0;
        for (VisiblePlayer vp : team)
            result[i++] = players.indexOf(vp);
        return result;
    }

    /**
     * Finds and returns the closest agent to a given agent in a list. Will NOT return the agent itself.
     * @return closest agent within provided list to the agent, or <code>null</code> if there are no agents
     */
    public VisiblePlayer getClosestAgent(VisiblePlayer agent, Collection<? extends VisiblePlayer> team) {
        if (players.size() == 0)
            return null;
        if (players.contains(agent)) {
            int i = players.indexOf(agent);
            int[] js = getPlayerIndices(team);
            int minJ = js[0];
            for (int j = 1; j < js.length; j++)
                if (i != j && distances[i][j] < distances[i][minJ])
                    minJ = j;
            return players.get(minJ);
        } else {
            Point2D.Double ownerPos = agent.getPosition();
            double minDist = Double.MAX_VALUE;
            double testDist;
            VisiblePlayer closest = null;
            for (VisiblePlayer asp : team) {
                if (asp == agent)
                    continue;
                testDist = ownerPos.distance(asp.getPosition());
                if (testDist < minDist && asp.isActive()) {
                    closest = asp;
                    minDist = testDist;
                }
            }
            return closest;
        }
    }

    /**
     * Use the cache to generate a greedy assignment of elements of ta to elements of tb,
     * assigning the smallest paired distance first.
     * @param team1 the first team
     * @param team2 the second team
     * @return a map of assignments between the agents
     */
    public Map<VisiblePlayer, VisiblePlayer> getAssignmentMapByClosestFirst(
            Collection<? extends VisiblePlayer> team1,
            Collection<? extends VisiblePlayer> team2) {

        HashMap<VisiblePlayer, VisiblePlayer> result = new HashMap<VisiblePlayer, VisiblePlayer>();

        // indices of the two teams
        int[] iIndex = getPlayerIndices(team1);
        int[] jIndex = getPlayerIndices(team2);
        if (iIndex.length == 0 || jIndex.length == 0)
            return result;        
        Vector<Integer> is = new Vector<Integer>();
        for (int i : iIndex)
            is.add(i);
        Vector<Integer> js = new Vector<Integer>();
        for (int i : jIndex)
            js.add(i);

        Integer bestI;
        Integer bestJ;

        // add optimal pairings, minimum pairing first
        while (!is.isEmpty() && !js.isEmpty()) {
            bestI = is.firstElement();
            bestJ = js.firstElement();
            for (int ii = 0; ii < is.size(); ii++)
                for (int jj = 0; jj < js.size(); jj++)
                    if (distances[is.get(ii)][js.get(jj)] < distances[bestI][bestJ]) {
                        bestI = is.get(ii);
                        bestJ = js.get(jj);
                    }
            result.put(players.get(bestI), players.get(bestJ));
            is.remove(bestI);
            js.remove(bestJ);
        }
        // add additional pairings if js is now empty but is is not
        while (!is.isEmpty()) {
            bestI = is.firstElement();
            bestJ = jIndex[0];
            for (Integer j : jIndex)
                if (distances[bestI][j] < distances[bestI][bestJ])
                    bestJ = j;
            result.put(players.get(bestI), players.get(bestJ));
            is.remove(bestI);
            js.remove(bestJ);
        }
        return result;
    }



    /** @return average distance between agents in the two sets, 0.0 if one of the sets is empty */
    public double getAverageDistance(Collection<? extends VisiblePlayer> team1, Collection<? extends VisiblePlayer> team2) {
        if (team1.size() == 0 || team2.size() == 0)
            return 0.0;

        double tot = 0.0;
        for (int i : getPlayerIndices(team1))
            for (int j : getPlayerIndices(team2))
                tot += distances[i][j];
        return tot / team1.size() / team2.size();
    }

    /** @return maximum distance between agents in the two sets, -1.0 if one of the sets is empty */
    public double getMaximumDistance(Collection<? extends VisiblePlayer> team1, Collection<? extends VisiblePlayer> team2) {
        if (team1.size() == 0 || team2.size() == 0)
            return -1.0;

        int[] is = getPlayerIndices(team1);
        int[] js = getPlayerIndices(team2);

        int bestI = is[0];
        int bestJ = js[0];

        for (int i : is)
            for (int j : js)
                if (distances[i][j] > distances[bestI][bestJ]) {
                    bestI = i;
                    bestJ = j;
                }
            
        return distances[bestI][bestJ];
    }

    /** @return minimum distance between agents in the two sets, -1.0 if one of the sets is empty */
    public double getMinimumDistance(Collection<? extends VisiblePlayer> team1, Collection<? extends VisiblePlayer> team2) {
        if (team1.size() == 0 || team2.size() == 0)
            return -1.0;

        int[] is = getPlayerIndices(team1);
        int[] js = getPlayerIndices(team2);

        int bestI = is[0];
        int bestJ = js[0];

        for (int i : is)
            for (int j : js)
                if (distances[i][j] < distances[bestI][bestJ]) {
                    bestI = i;
                    bestJ = j;
                }

        return distances[bestI][bestJ];
    }
}
