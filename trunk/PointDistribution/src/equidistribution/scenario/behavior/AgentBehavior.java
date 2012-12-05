/*
 * AgentBehavior.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;

/**
 * Provides behavior that moves a single agent based on a scenario.
 * @author Elisha Peterson
 */
public interface AgentBehavior {

    /**
     * Computes movement for an individual agent based on current parameters. Does not need to worry
     * whether movement will send agent outside of region; another check will be performed for this.
     * @param border the region border
     * @param target target area (weighted)
     * @param self the agent to move
     * @param neighbors neighboring agents
     * @param par global parameters (e.g. limiting movement)
     *
     * @return Movement vector for specified agent within a team, based on current region and partition.
     */
    public abstract Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par);

}
