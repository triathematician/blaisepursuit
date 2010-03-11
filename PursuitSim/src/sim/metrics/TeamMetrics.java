/**
 * TeamMetrics.java
 * Created on Jul 23, 2009
 */
package sim.metrics;

import sim.DistanceCache;
import sim.component.team.Team;

/**
 * <p>
 *   <code>TeamMetrics</code> is a metric that may be defined between two teams.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum TeamMetrics {

    /** minimum distance between the two teams. */
    MINIMUM_DISTANCE("Minimum distance") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return dt.getMinimumDistance(team1.state.activePlayers, team2.state.activePlayers);
        }
    },
    /** maximum distance between the two teams. */
    MAXIMUM_DISTANCE("Maximum distance") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return dt.getMaximumDistance(team1.state.activePlayers, team2.state.activePlayers);
        }
    },
    /** average distance between the two teams. */
    AVERAGE_DISTANCE("Average distance") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return dt.getAverageDistance(team1.state.activePlayers, team2.state.activePlayers);
        }
    },


    /** # of agents on the team. */
    NUMBER_OF_AGENTS_ACTIVE("Number of agents in play") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfActiveAgents();
        }
    },
    /** percentage of agents active */
    PERCENTAGE_OF_AGENTS_ACTIVE("Percent of agents active") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfActiveAgents() / team1.numAgents;
        }
    },
    /** # of agents on the team that are "safe" */
    NUMBER_OF_AGENTS_SAFE("Number of agents safe") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfSafeAgents();
        }
    },
    /** percentage of agents safe */
    PERCENTAGE_OF_AGENTS_SAFE("Percent of agents safe") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfSafeAgents() / team1.numAgents;
        }
    },





    /** # of agents on the opponents. */
    NUMBER_OF_OPPONENTS_ACTIVE("Number of opponents in play") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team2.getNumberOfActiveAgents();
        }
    },
    /** percentage of opponents active */
    PERCENTAGE_OF_OPPONENTS_ACTIVE("Percent of opponents active") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team2.getNumberOfActiveAgents() / team2.numAgents;
        }
    },
    /** # of agents on the opponents that are "safe" */
    NUMBER_OF_OPPONENTS_SAFE("Number of opponents safe") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team2.getNumberOfSafeAgents();
        }
    },
    /** percentage of opponents safe */
    PERCENTAGE_OF_OPPONENTS_SAFE("Percent of opponents safe") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team2.getNumberOfSafeAgents() / team2.numAgents;
        }
    },
    /** number of opponents that have not reached safety and are not active */
    NUMBER_OF_OPPONENTS_UNCAPTURED("Number of opponents uncaptured") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team2.getNumberOfActiveAgents() + team2.getNumberOfSafeAgents();
        }
    },


    /** advantage in #s over the opposing team */
    AGENT_ADVANTAGE("Advantage in numbers") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfActiveAgents() - team2.getNumberOfActiveAgents();
        }
    },


    /** number of opponents captured */
    NUMBER_OPPONENTS_CAPTURED("Number of opponents captured") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfCaptures(team2);
        }
    },
    /** percentage of opponents captured */
    PERCENT_OPPONENTS_CAPTURED("Percent of opponents captured") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return team1.getNumberOfCaptures(team2) / team2.numAgents;
        }
    },

    
    /** Computes the time of the simulation. */
    SIMULATION_TIME("Time of simulation") {
        public double getValue(DistanceCache dt, double curTime, Team team1, Team team2) {
            return curTime;
        }
    };
    
    String description;
    @Override public String toString() { return description; }
    TeamMetrics(String str) { this.description = str; }

    abstract public double getValue(DistanceCache dt, double curTime, Team team1, Team team2);

    // TODO (later) - agent closest to capture
    // TODO (later) - time since last capture
}
