/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.component;

import java.awt.Color;
import java.awt.geom.Point2D;
import scio.coordinate.utils.PlanarMathUtils;
import sim.SimComponent;

/**
 * This class is a simulation component that represents a static object at a fixed position.
 */
public class Obstacle extends SimComponent
        implements VisiblePlayer, InitialPositionSetter {

    String name;
    Color color;
    Point2D.Double position;

    public Obstacle() {
        this("", Color.BLACK, 0, 0);
    }

    public Obstacle(String name, Color color, Point2D.Double position) {
        this.name = name;
        this.color = color;
        if (position == null)
            throw new IllegalArgumentException();
        this.position = position;
    }

    public Obstacle(String name, Color color, double x, double y) {
        this.name = name;
        this.color = color;
        position = new Point2D.Double(x, y);
    }

    @Override public String toString() {
        return String.format(name + " @ (%.2f, %.2f)", position.x, position.y);
    }

    //
    // GETTERS & SETTERS
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    //
    // AgentSensorProxy METHODS
    //

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

    //
    // SimulationComponent METHODS
    //

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
