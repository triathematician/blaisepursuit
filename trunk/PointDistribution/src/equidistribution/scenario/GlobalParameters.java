/*
 * GlobalParameters.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario;

/**
 * Parameters useful throughout a particular simulation.
 * @author elisha
 */
public class GlobalParameters {

    public double MAX_MOVE = .02;

    public double getMaxMovement() { return MAX_MOVE; }
    public void setMaxMovement(double val) { MAX_MOVE = val; }

}
