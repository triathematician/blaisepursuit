/**
 * SimulationTerminatedException.java
 * Created on Jul 17, 2009
 */

package sim;

/**
 * <p>
 *   <code>SimulationTerminatedException</code> indicates that a simulation has been terminated.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationTerminatedException extends Exception {
    Object explanation;

    public SimulationTerminatedException(Object explanation) {
        this.explanation = explanation;
    }
}
