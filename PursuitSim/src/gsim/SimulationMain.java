/**
 * SimulationMain.java
 * Created on Jul 22, 2009
 */

package gsim;

import gsim.samples.SampleSims;
import gsim.logger.SimulationLogger;
import gsim.logger.EssentialLogger;
import java.util.ArrayList;
import java.util.List;
import sim.*;

/**
 * <p>
 *   <code>SimulationMain</code> ...
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationMain {

    public static void main(String[] args) throws InstantiationException {

        Simulation sim1 = SampleSims.RANDOM_PURSUIT.getSimulation(new ArrayList<SimulationLogger>());
        sim1.setMaxTime(100);
        List<SimulationLogger> el = EssentialLogger.getEssentialLoggersFor(sim1);
        sim1.run();

    }

}
