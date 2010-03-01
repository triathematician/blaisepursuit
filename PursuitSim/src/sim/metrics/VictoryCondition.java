/**
 * VictoryCondition.java
 * Created on Jul 23, 2009
 */
package sim.metrics;

import sim.DistanceCache;
import sim.agent.SimulationTeam;

/**
 * <p>
 *   <code>VictoryCondition</code> encodes conditions of victory, which depends on a particular
 *   metric between two teams.
 * </p>
 *
 * @author Elisha Peterson
 */
public class VictoryCondition {

    TeamMetrics metric = TeamMetrics.MAXIMUM_DISTANCE;
    ComparisonType type = ComparisonType.LESS;
    double threshold = 10.0;
    SimulationTeam team1;
    SimulationTeam team2;

    /** Basic instantiation */
    public VictoryCondition() {
    }

    /** Construct with specified values. */
    public VictoryCondition(SimulationTeam team1, SimulationTeam team2, TeamMetrics metric, ComparisonType type, double threshold) {
        setTeam1(team1);
        setTeam2(team2);
        setMetricByName(metric.name());
        setTypeByName(type.name());
        setThreshold(threshold);
    }

    @Override
    public String toString() {
        return "Victory [ " + metric + "(" + team2 + ") " + type + " " + threshold + " ]";
    }

    //
    // GETTERS & SETTERS
    //

    public TeamMetrics getMetric() {
        return metric;
    }

    public String getMetricByName() {
        return metric==null ? "" : metric.name();
    }

    public void setMetricByName(String name) {
        metric = TeamMetrics.valueOf(name);
    }

    public SimulationTeam getTeam1() {
        return team1;
    }

    public void setTeam1(SimulationTeam team1) {
        this.team1 = team1;
    }

    public SimulationTeam getTeam2() {
        return team2;
    }

    public void setTeam2(SimulationTeam team2) {
        this.team2 = team2;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public ComparisonType getType() {
        return type;
    }

    public String getTypeByName() {
        return type==null ? "" : type.name();
    }

    public void setTypeByName(String name) {
        type = ComparisonType.valueOf(name);
    }

    //
    // BEHAVIORAL
    //

    /** @return true if victory has been achieved at the specified time */
    public boolean hasBeenMet(DistanceCache dt, double curTime) {
        return type.compare(metric.getValue(dt, curTime, team1, team2), threshold);
    }

    /** @return an instance of a victory condition that always returns false */
    public static final VictoryCondition NONE = new VictoryCondition() {
        @Override
        public boolean hasBeenMet(DistanceCache dt, double curTime) {
            return false;
        }
    };
}
