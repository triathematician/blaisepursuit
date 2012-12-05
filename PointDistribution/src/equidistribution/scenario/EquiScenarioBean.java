/*
 * EquiScenarioBean.java
 * Created Aug 24, 2010
 */

package equidistribution.scenario;

import equidistribution.scenario.EquiTeam.Agent;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * Provides bean patterns for working with an underlying scenario.
 * Useful for saving to and from a file using default XML handling.
 * 
 * @author Elisha Peterson
 */
public class EquiScenarioBean {

    EquiScenario scenario = new EquiScenario();

    public EquiScenario getScenario() { return scenario; }
    public void useController(EquiController c) { this.scenario = c.scenario; }
    public void updateController(EquiController c) { c.setScenario(scenario); }

    public GlobalParameters getParameters() { return scenario.GLOBAL; }
    public void setParameters(GlobalParameters par) { }

    /** @return global region priority */
    public double getPriority() { return scenario.priority; }
    /** Sets global region priority */
    public void setPriority(double p) { scenario.priority = p; }

    /** @return copy of the current border */
    public Point2D.Double[] getBorder() { return scenario.border; }
    /** @return copy of the i'th border point */
    public Point2D.Double getBorder(int i) { return scenario.border[i]; }
    /** Sets the border to the specified list */
    public void setBorder(Point2D.Double[] b) { scenario.border = b; }
    /** Sets i'th border point */
    public void setBorder(int i, Point2D.Double b) { scenario.border[i] = b; }
    
    /** @return zone priorities */
    public Double[] getZonePriority() { return scenario.pZones.toArray(new Double[]{}); }
    /** @return i'th zone priority */
    public Double getZonePriority(int i) { return scenario.pZones.get(i); }
    /** Sets all zone priorities */
    public void setZonePriority(Double[] pp) { scenario.pZones.clear(); for (Double p : pp) scenario.pZones.add(p); }
    /** Sets i'th zone priority */
    public void setZonePriority(int i, Double p) { scenario.pZones.set(i, p); }

    /** @return copy of the current zones */
    public Point2D.Double[][] getZones() { return scenario.zones.toArray(new Point2D.Double[][]{}); }
    /** @return copy of the i'th zone */
    public Point2D.Double[] getZones(int i) { return scenario.zones.get(i); }
    /** Sets the zones to the specified list */
    public void setZones(Point2D.Double[][] b) { scenario.zones.clear(); for (Point2D.Double[] z : b) scenario.zones.add(z); }
    /** Sets i'th zone */
    public void setZones(int i, Point2D.Double[] z) { scenario.zones.set(i, z); }

    /** @return list of agents on team */
    public Agent[] getAgent() { return scenario.team.agents().toArray(new Agent[]{}); }
    /** @return i'th agent on team */
    public Agent getAgent(int i) { return scenario.team.agents().get(i); }
    /** Sets team to the specified agent list */
    public void setAgent(Agent[] arr) { scenario.team = new EquiTeam(arr); }
    /** Sets i'th member of team to be the specified agent */
    public void setAgent(int i, Agent a) { scenario.team.agents.set(i, a); }
}
