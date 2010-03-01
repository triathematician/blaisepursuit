/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.agent;

import java.awt.geom.Point2D;
import org.apache.commons.math.FunctionEvaluationException;
import org.bm.blaise.scio.function.ParsedUnivariateRealFunction;
import org.bm.blaise.scribo.parser.ParseException;
import sim.SimulationComponent;

/**
 * This class is a simulation component that represents a "path", i.e. returns a point for every time in the simulation.
 *
 * @author ae3263
 */
public class SimulationPath extends SimulationComponent
        implements AgentSensorProxy {

    ParsedUnivariateRealFunction funcX;
    ParsedUnivariateRealFunction funcY;
    Point2D.Double position;
    Point2D.Double velocity;

    public SimulationPath() {
        this("0", "0");
    }

    public SimulationPath(String sx, String sy) {
        setFunctionX(sx);
        setFunctionY(sy);
    }

    @Override
    public String toString() {
        return "SimulationPath (" + funcX.getFunctionString() + ", " + funcY.getFunctionString() + ")";
    }

    //
    // GETTERS & SETTERS
    //

    public void setFunctionX(String sx) {
        try {
            funcX = new ParsedUnivariateRealFunction(sx, "t");
        } catch (ParseException ex) {
        }
    }

    public String getFunctionX() {
        return funcX.getFunctionString();
    }

    public void setFunctionY(String sy) {
        try {
            funcY = new ParsedUnivariateRealFunction(sy, "t");
        } catch (ParseException ex) {
        }
    }

    public String getFunctionY() {
        return funcY.getFunctionString();
    }

    //
    // AgentSensorProxy METHODS: **getters ONLY**
    //

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public boolean isActive() {
        return true;
    }

    //
    // SimulationComponent METHODS
    //

    public void initStateVariables() {
        try {
            position = new Point2D.Double(funcX.value(0), funcY.value(0));
        } catch (FunctionEvaluationException ex) {
            position = new Point2D.Double();
        }
        velocity = new Point2D.Double();
    }

    public void setControlVariables(double simTime, double timePerStep) {
        try {
            velocity.x = funcX.value(simTime) - position.x;
            velocity.y = funcY.value(simTime) - position.y;
        } catch (FunctionEvaluationException ex) {
            System.out.println("unable to evaluate function!!");
        }
    }

    public void adjustState(double timePerStep) {
        position.x += velocity.x;
        position.y += velocity.y;
    }

}
