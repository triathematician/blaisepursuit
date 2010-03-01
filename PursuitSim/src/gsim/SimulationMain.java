/**
 * SimulationMain.java
 * Created on Jul 22, 2009
 */

package gsim;

import gsim.samples.SampleSims;
import gsim.logger.AbstractSimulationLogger;
import gsim.logger.EssentialLogger;
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

        Simulation sim1 = SampleSims.BUG_LIGHT.getSimulation();
        sim1.setMaxTime(1);
        List<AbstractSimulationLogger> el = EssentialLogger.getEssentialLoggersFor(sim1);
        sim1.run();
        for (AbstractSimulationLogger l : el) {
            l.printData();
        }

//        Simulation sim2 = SampleSims.getCopsRobbersSimulation();
//        sim2.setMaxTime(1);
//        el = EssentialLogger.getEssentialLoggersFor(sim1);
//        sim2.run();
//        for (AbstractSimulationLogger l : el) {
//            l.printData();
//        }

    }

}
