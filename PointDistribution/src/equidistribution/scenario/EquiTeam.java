/*
 * EquiTeam.java
 * Created Jan 26, 2010
 * Modified Aug 18, 2010
 */
package equidistribution.scenario;

import equidistribution.scenario.behavior.AgentBehavior;
import equidistribution.scenario.behavior.TowardLargestArea;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A team of sensors/agents whose goal is equi-distribution.
 * @author Elisha Peterson
 */
public class EquiTeam {

    /** Agents on team */
    List<Agent> agents;
    /** The total team capacity */
    Double totalCapacity = null;

    /** Flag indicating whether locations are currently changing */
    boolean moving = false;

    //
    // CONSTRUCTORS
    //

    /** Constructs team with specified agents */
    public EquiTeam(Agent... agents) {
        this.agents = new ArrayList<Agent>();
        for (Agent a : agents)
            this.agents.add(a);
    }

    /** Constructs team with specified agents */
    public EquiTeam(List<Agent> agents) {
        this.agents = agents;
    }

    /** Constructs with fixed total capacity & specified agents */
    public EquiTeam(List<Agent> agents, Double capacity) {
        this.agents = agents;
        totalCapacity = capacity;
    }

    public static EquiTeam getInstance(AgentBehavior a, int size) {
        if (size < 1 || a == null) throw new IllegalArgumentException("Invalid");
        ArrayList<Agent> agents = new ArrayList<Agent>(size);
        for (int i = 0; i < size; i++)
            agents.add(new Agent(a));
        return new EquiTeam(agents);
    }


    //
    // UTILITIES
    //

    /** @return subteam consisting of specified agents, with the same total capacity */
    public EquiTeam createCoalition(List<Agent> agents) {
        ArrayList<Agent> newAgents = new ArrayList<Agent>();
        for (Agent a : agents)
            if (this.agents.contains(a))
                newAgents.add(new Agent(a.capacity, a.behavior));
        return new EquiTeam(newAgents, totalCapacity());
    }

    //
    // BASE METHODS
    //

    /** @return list of agents */
    public List<Agent> agents() {
        return Collections.unmodifiableList(agents);
    }

    /** @return size, equal to number of agents on the team */
    public int size() {
        return agents.size();
    }

    /** @return total capacity of the team */
    public double totalCapacity() {
        if (totalCapacity != null)
            return totalCapacity;
        
        double result = 0;
        for (Agent a : agents)
            result += a.capacity;
        return result;
    }

    //
    // VALIDATION
    //

    boolean valid = false;
    
    /**
     * Ensures team is set up for the specified region:
     * moves any points outside region to the region, and zero's out any agent movements.
     * @param r limits of region, to ensure all agents are contained within
     */
    public void validate(Point2D.Double[] r) {
        valid = false;
        for (Agent a : agents)
            a.validate();
        valid = true;
    }

    //
    // MOVEMENT
    //

    /**
     * Moves the team according to their behaviors.
     * @param region the region of the scenario
     * @param targetArea the target area for the agents
     * @param network the network structure of the agents
     * @param GLOBAL global parameters
     */
    public void move(Point2D.Double[] border, Double targetArea, HashMap<Agent, Set> network, GlobalParameters GLOBAL) {
        moving = true;

        // compute movements
        HashMap<EquiTeam.Agent, Point2D.Double> moves = new HashMap<EquiTeam.Agent, Point2D.Double>();
        for (EquiTeam.Agent a : agents())
            moves.put(a, a.behavior == null ? new Point2D.Double()
                        : a.behavior.compute(border, targetArea, a, network.get(a), GLOBAL));

        // make movements
        for (Entry<EquiTeam.Agent, Point2D.Double> en : moves.entrySet())
            en.getKey().move(en.getValue());

        moving = false;
    }

    
    //
    // INNER CLASSES
    //

    /** Represents an agent on a team performing equi-distribution algorithm */
    public static class Agent extends Point2D.Double {

        // STATIC PROPERTIES

        /** Capacity of agent */
        double capacity;
        /** Behavioral algorithm */
        AgentBehavior behavior;

        // DYNAMIC PROPERTIES

        /** Last movement of agent */
        transient public Point2D.Double move;

        // COMPUTED PROPERTIES

        /** Weighted area of assigned subregion, defined as the weighted area assigned divided by the capacity. */
        transient public java.lang.Double area;
        /** Whether agent borders boundary. */
        transient public boolean boundaryAgent = false;
        /** Neighbors of agent */
        transient List<Agent> neighbors;

        public Agent() {
            this(1.0, TowardLargestArea.INSTANCE);
        }

        public Agent(double x, double y) {
            super(x, y);
        }

        public Agent(AgentBehavior behavior) {
            this(1.0, behavior);
        }

        public Agent(double capacity, AgentBehavior behavior) {
            this.capacity = capacity;
            this.behavior = behavior;
        }

        public Agent(double x, double y, double capacity, AgentBehavior behavior) {
            super(x,y);
            this.capacity = capacity;
            this.behavior = behavior;
        }

        @Override
        public String toString() {
            return "Agent[" + String.format("loc=(%.2f,%.2f), capacity=%.2f", x, y, capacity)
                    + ", behavior=" + behavior + "]";
        }

        public Point2D.Double getLoc() { return new Point2D.Double(x, y); }
        public void setLoc(Point2D.Double loc) { setLocation(loc); }

        /** Prepares agent for start of scenario, nulling out any elements that will change */
        void validate() {
            if (move == null)
                move = new Point2D.Double();
            area = null;
            neighbors = null;
            boundaryAgent = false;
        }

        /** Moves agent by specified vector */
        public void move(Point2D.Double vec) {
            move = vec;
            x += move.x;
            y += move.y;
        }

        public double getCapacity() { return capacity; }
        public void setCapacity(double cap) { capacity = cap; }
        public AgentBehavior getBehavior() { return behavior; }
        public void setBehavior(AgentBehavior b) { behavior = b; }

    } // INNER CLASS Agent
}
