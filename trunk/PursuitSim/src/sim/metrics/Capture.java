/**
 * CaptureCondition.java
 * Created on Jul 23, 2009
 */

package sim.metrics;

import java.util.ArrayList;
import java.util.List;
import sim.DistanceCache;
import sim.component.team.Team;
import sim.component.VisiblePlayer;

/**
 * <p>
 *   <code>CaptureCondition</code> encodes a condition of capture, which depends on a particular
 *   metric between two teams.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Capture {

    AgentMetrics metric = AgentMetrics.DISTANCE;
    ComparisonType type = ComparisonType.LESS;
    Action ac = Action.DEACTIVATE_BOTH;
    double threshold = 5.0;
    Team team1;
    Team team2;
    VisiblePlayer target;

    public Capture() {
    }

    public Capture(Team team1, Team team2, double threshold) {
        this(team1, team2, threshold, Action.DEACTIVATE_BOTH);
    }

    public Capture(Team team1, Team team2, double threshold, Action action) {
        this.team1 = team1;
        this.team2 = team2;
        this.threshold = threshold;
        this.ac = action;
    }

    public Capture(Team team1, VisiblePlayer target, double threshold, Action action) {
        this.team1 = team1;
        this.target = target;
        this.threshold = threshold;
        this.ac = action;
    }

    @Override
    public String toString() {
        return "Capture [" + metric + "(" + team2 + ") " + type + " " + threshold + "]";
    }

    //
    // GETTERS & SETTERS
    //

    public AgentMetrics getMetric() {
        return metric;
    }

    public void setMetric(AgentMetrics metric) {
        this.metric = metric;
    }

    public String getMetricByName() {
        return metric==null ? "" : metric.name();
    }

    public void setMetricByName(String name) {
        metric = AgentMetrics.valueOf(name);
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public VisiblePlayer getTarget() {
        return target;
    }

    public void setTarget(VisiblePlayer target) {
        this.target = target;
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

    public void setType(ComparisonType type) {
        this.type = type;
    }

    public String getTypeByName() {
        return type==null ? "" : type.name();
    }

    public void setTypeByName(String name) {
        type = ComparisonType.valueOf(name);
    }

    public Action getCaptureAction() {
        return ac;
    }

    public void setCaptureAction(Action ac) {
        this.ac = ac;
    }



    //
    // BEHAVIORAL METHODS
    //

    /** Tests to see if a capture has been made between the two agents. */
    public boolean hasBeenMet(DistanceCache dt, double curTime, VisiblePlayer a1, VisiblePlayer a2) {
        boolean result = type.compare(metric.getValue(dt, curTime, a1, a2), threshold);
        return result;
    }

    /**
     * Returns a list of captures made, as a map pointing from the capturing agent to the captured agent.
     */
    public List<VisiblePlayer[]> findCaptures(DistanceCache dt, double curTime) {
        ArrayList<VisiblePlayer[]> captures = new ArrayList<VisiblePlayer[]>();
        if (team2 == null) {
            for(VisiblePlayer a1 : team1.state.activePlayers)
                if (hasBeenMet(dt, curTime, a1, target))
                    captures.add(new VisiblePlayer[]{a1, target});
        } else {
            for(VisiblePlayer a1 : team1.state.activePlayers)
                for (VisiblePlayer a2 : team2.state.activePlayers)
                    if (hasBeenMet(dt, curTime, a1, a2))
                        captures.add(new VisiblePlayer[]{a1, a2});
        }
        return captures;
    }

    /** @return an instance of a capture condition that always returns false */
    public static final Capture NONE = new Capture() {
        @Override
        public boolean hasBeenMet(DistanceCache dt, double curTime, VisiblePlayer a1, VisiblePlayer a2) {
            return false;
        }
    };

    public enum Action {
        DEACTIVATE_OWNER,
        DEACTIVATE_TARGET,
        DEACTIVATE_BOTH,
        SAFETY_OWNER;
    }

}
