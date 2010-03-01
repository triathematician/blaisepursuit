/**
 * TeamMetrics.java
 * Created on Jul 23, 2009
 */
package sim.metrics;

import sim.DistanceCache;
import sim.agent.SimulationTeam;

/**
 * <p>
 *   <code>TeamMetrics</code> is a metric that may be defined between two teams.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum TeamMetrics implements Metric<SimulationTeam> {
    /** minimum distance between the two teams. */
    MINIMUM_DISTANCE("Minimum distance") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return dt.getMinimumDistance(team1.getActiveAgents(), team2.getActiveAgents());
        }
    },
    /** average distance between the two teams. */
    AVERAGE_DISTANCE("Average distance") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return dt.getAverageDistance(team1.getActiveAgents(), team2.getActiveAgents());
        }
    },
    /** maximum distance between the two teams. */
    MAXIMUM_DISTANCE("Maximum distance") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return dt.getMaximumDistance(team1.getActiveAgents(), team2.getActiveAgents());
        }
    },
    /** # of agents on the team. */
    NUMBER_OF_AGENTS("# of agents") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return team1.getNumberOfActiveAgents();
        }
    },
    /** # of agents on the opposing team. */
    NUMBER_OF_OPPONENTS("Number of Opponents") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return team2.getNumberOfActiveAgents();
        }
    },
    /** advantage in #s over the opposing team */
    AGENT_ADVANTAGE("Advantage in numbers") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return team1.getNumberOfActiveAgents() - team2.getNumberOfActiveAgents();
        }
    },
    /** percentage of agents active */
    PERCENTAGE_OF_AGENTS_ACTIVE("Percent of agents active") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return team1.getNumberOfActiveAgents() / team1.getInitiallyActiveAgents().size();
        }
    },
    /** percentage of opponents active */
    PERCENTAGE_OF_OPPONENTS_ACTIVE("Percent of opponents active") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return team2.getNumberOfActiveAgents() / team2.getInitiallyActiveAgents().size();
        }
    },
    /** Computes the time of the simulation. */
    SIMULATION_TIME("Time of simulation") {
        public double getValue(DistanceCache dt, double curTime, SimulationTeam team1, SimulationTeam team2) {
            return curTime;
        }
    };
    
    String description;
    @Override public String toString() { return description; }
    TeamMetrics(String str) { this.description = str; }

    // TODO (later) - agent closest to capture
    // TODO (later) - numbre of team "safe"
    // TODO (later) - number of opponents "safe"
    // TODO (later) - number of opponents captured by this team
    // TODO (later) - percentage of opponents captured by this team
    // TODO (later) - number of opponents that have not been captured by the team
    // TODO (later) - number of captures that can still be made
    // TODO (later) - time since last capture
}
