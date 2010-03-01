/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.agent;

import java.awt.geom.Point2D;
import scio.coordinate.utils.PlanarMathUtils;
import sim.SimulationComponent;

/**
 * This class is a simulation component that represents a static object at a fixed position.
 */
public class SimulationObstacle extends SimulationComponent
        implements AgentSensorProxy, InitialPositionSetter {

    String name;
    Point2D.Double position;

    public SimulationObstacle(String name, Point2D.Double position) {
        this.name = name;
        if (position == null)
            throw new IllegalArgumentException();
        this.position = position;
    }

    public SimulationObstacle(String name, double x, double y) {
        this.name = name;
        position = new Point2D.Double(x, y);
    }

    public SimulationObstacle() {
        this("", 0, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() { return String.format(name + " @ (%.2f, %.2f)", position.x, position.y); }

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getInitialPosition() {
        return position;
    }

    public void setInitialPosition(Point2D.Double pos) {
        if (pos == null)
            throw new IllegalArgumentException();
        this.position = pos;
    }

    public Point2D.Double getVelocity() {
        return PlanarMathUtils.ZERO;
    }

    public boolean isActive() {
        return true;
    }

    @Override
    public void initStateVariables() {
    }

    @Override
    public void setControlVariables(double curTime, double timePerStep) {
    }

    @Override
    public void adjustState(double timePerStep) {
    }

}
