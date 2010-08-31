/*
 * MetricPlotPanel.java
 * Created Aug 24, 2010
 */

package equidistribution.gui;

import equidistribution.scenario.EquiController;
import equidistribution.scenario.EquiMetrics;
import java.awt.Color;
import java.awt.Dimension;
import org.bm.blaise.specto.plane.compgeom.NumberLogPlottable;
import simutils.MetricLogger;
import simutils.SimulationEvent;
import simutils.SimulationEventListener;
import visometry.plane.PlaneAxes;
import visometry.plane.PlanePlotComponent;

/**
 * Displays metrics changing over time.
 * @author Elisha Peterson
 */
public class MetricPlotPanel extends PlanePlotComponent
        implements SimulationEventListener {
    
    MetricLogger<double[]> logger;
    NumberLogPlottable maxDevP, meanDevP, meanSqrDevP;

    public MetricPlotPanel() {
        setPreferredSize(new Dimension(300,200));
//        setBackground(Color.BLACK);
        
        setAspectRatio(0.02);
        setDesiredRange(0.0,0.0,350.0,3.0);
        add(new PlaneAxes("step", "metric", PlaneAxes.AxesType.QUADRANT1));

        logger = new MetricLogger<double[]> ( "L-metrics", EquiMetrics.L_METRICS_ADJUSTED );

        add(maxDevP = new NumberLogPlottable());
        add(meanDevP = new NumberLogPlottable());
        add(meanSqrDevP = new NumberLogPlottable());
    }

    public EquiController getController() { 
        return (EquiController) logger.getSimulation();
    }
    public void setController(EquiController sc) {
        EquiController cur = getController();
        if (cur != null)
            cur.removeSimulationEventListener(this);
        logger.setSimulation(sc);
        if (sc != null)
            sc.addSimulationEventListener(this);
    }

    public void handleResetEvent(SimulationEvent e) {}
    public void handleIterationEvent(SimulationEvent e) {
        double[] last = logger.getLastValue();
        maxDevP.logValue(last[0]);
        meanDevP.logValue(last[1]);
        meanSqrDevP.logValue(last[2]);
    }
    public void handleGenericEvent(SimulationEvent e) {}

}
