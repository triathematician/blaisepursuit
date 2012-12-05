/*
 * ScenarioController.java
 * Created Aug 18, 2010
 */
package equidistribution.scenario;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.behavior.WeightedAreaMovement;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.bm.blaise.scio.algorithm.PolygonIntersectionUtils;
import org.bm.blaise.scio.algorithm.PolygonUtils;
import simutils.SimulationEvent;
import simutils.SimulationEventListener;

/**
 * Provides overall control of an equi-distribution scenario.
 * @author Elisha Peterson
 */
public class EquiScenario
        implements simutils.Simulation {

    // STATIC

    /** The large border */
    Point2D.Double[] border;
    /** The default border priority */
    double priority = 1.0;
    /** Special zones */
    List<Point2D.Double[]> zones;
    /** Priorities of zones */
    List<Double> pZones;

    /** The team attempting equi-distribution */
    EquiTeam team;

    /** Used to compute the partition and network structure */
    final RegionPartitioner MAPPER = new RegionPartitioner();
    /** The global parameters */
    final GlobalParameters GLOBAL = new GlobalParameters();

    // COMPUTED VALUES
    
    /** The step # of the simulation */
    transient int step = 0;
    /** The target area, computed based on the region's size and zones */
    transient Double targetArea;
    /** The subregions as computed by the partitioner for current team locations */
    transient LinkedHashMap<EquiTeam.Agent, Point2D.Double[]> subRegions;
    /** The network structure as computed by the partitioner for current team locations */
    transient HashMap<EquiTeam.Agent, Set> network;
    /** The boundary nodes as comptued by the partitioner for current team locations */
    transient HashSet<EquiTeam.Agent> boundaryAgents;

    //
    // CONSTRUCTOR
    //

    /** Constructs scenario with default region and team */
    public EquiScenario() {
        border = new Point2D.Double[]{
                    new Point2D.Double(0, 0), new Point2D.Double(2, 0), new Point2D.Double(2, 1), new Point2D.Double(0, 1)
                };
        zones = new ArrayList<Point2D.Double[]>();
        pZones = new ArrayList<Double>();
        addZone(4.0, new Point2D.Double(.5, .25), new Point2D.Double(1, .25), new Point2D.Double(1, .75), new Point2D.Double(.5, .75));
        team = EquiTeam.getInstance(WeightedAreaMovement.DEFAULT_INSTANCE, 50);
        for (int i = 0; i < 5; i++)
            team.agents.get(i).capacity = 2.0;
        randomizeAgentLocations();
        validate();
    }

    /** Adds a priority-zone to the region */
    public void addZone(double priority, Point2D.Double... points) {
        zones.add(points);
        pZones.add(priority);
    }

    //
    // UTILITIES
    //

    /** @return random point in region */
    public Point2D.Double randomPointInRegion() {
        Rectangle2D.Double bounds = PolygonUtils.boundingBox(border);
        Point2D.Double potential = new Point2D.Double();
        do {
            potential.x = bounds.x + Math.random() * bounds.width;
            potential.y = bounds.y + Math.random() * bounds.height;
        } while (!PolygonUtils.inPolygon(potential, border));
        return potential;
    }

    /** @return true if point is in region */
    public boolean regionContains(Point2D.Double p) {
        return PolygonUtils.inPolygon(p, border);
    }

    /**
     * If point is not in the region, moves it to the closest in-region point.
     * @param p pointer to point (may be changed)
     */
    public void movePointInsideRegion(Point2D.Double p) {
        if (!regionContains(p)) {
            Point2D.Double closest = PolygonUtils.closestPointOnPolygon(p, border);
            if (!(closest.equals(p)))
                p.setLocation(closest);
        }
    }
    
    /** Randomizes locations of agents */
    public void randomizeAgentLocations() {
        for (Agent a : team.agents())
            a.setLocation(randomPointInRegion());
    }

    //
    // COMPUTED VALUES
    //

    transient Double totalArea = null;

    /** @return area of border */
    public double area() {
        return PolygonUtils.areaOf(border);
    }

    /** @return total area, weighted by priority */
    public double totalWeightedArea() {
        double result = priority * area();
        for (int i = 0; i < zones.size(); i++) {
            result += pZones.get(i) * PolygonUtils.areaOf(zones.get(i));
        }
        return result;
    }

    /** @return step time */
    public int step() {
        return step;
    }

    /** @return target area for each agent, with current region & team */
    public Double targetArea() {
        return targetArea;
    }

    /**
     * Computes weighted area of specified sub-border.
     * The weighting corresponds to the zones. The weights are additive, so
     * that overlapping zones' weights are added together (and added to the default weight).
     * @param subregion a polygonal subregion of the large border
     * @return weighted area of the subregion
     */
    public double weightedArea(Point2D.Double[] subregion) {
        double result = priority * PolygonUtils.areaOf(subregion);
        for (int i = 0; i < zones.size(); i++)
            result += pZones.get(i) * overlap(zones.get(i), subregion);
        return result;
    }

    /** Utility to return the area of overlap of two polygons */
    private static double overlap(Point2D.Double[] poly1, Point2D.Double[] poly2) {
        Point2D.Double[] poly = PolygonIntersectionUtils.intersectionOfConvexPolygons(poly1, poly2);
        double result = PolygonUtils.areaOf(poly);
        return result;
    }

    //
    // VALIDATION
    //

    /** Whether scenario is currently set up and ready to go */
    private boolean valid = false;

    /** EquiScenario becomes invalid; notifies listeners that scenario is invalid */
    public void invalidate() {
        System.out.println("invalidate");
        
        valid = false;
        targetArea = null;
        subRegions = null;
        network = null;
        boundaryAgents = null;
    }

    /** Sets up scenario and notifies listeners when "ready to go" */
    public void validate() {
        System.out.println("validate");
        
        valid = false;
        team.totalCapacity = null;
        targetArea = totalWeightedArea() / team.totalCapacity();
        team.validate(border);
        subRegions = new LinkedHashMap<EquiTeam.Agent, Point2D.Double[]>();
        network = new HashMap<EquiTeam.Agent, Set>();
        boundaryAgents = new HashSet<EquiTeam.Agent>();
        for (Agent a : team.agents)
            if (!regionContains(a))
                a.setLocation(randomPointInRegion());
                
        calculatePartition();
        valid = true;
        
        fireSimResetEvent(step);
    }

    /** Revalidates scenario */
    void revalidate() {
        invalidate();
        validate();
    }

    //
    // ITERATION
    //
    
    /** @return whether simulation should terminate */
    public boolean isFinished() {
        return false;
    }

    public void run() {
        System.out.println("run");

        step = 0;
        validate();
        while (!isFinished())
            iterate();
    }

    /** A single iteration of the scenario, with current algorithms. */
    public void iterate() {
        if (!valid)
            throw new IllegalStateException("Cannot iterate while scenario is not valid!");
        team.move(border, targetArea, network, GLOBAL);
        calculatePartition();
        step++;
        fireSimIterateEvent(step);
    }

    /** Computes partition and sets up team/agents with their areas. */
    public void calculatePartition() {
        System.out.println("calcPartition");
        
        for (Agent a : team.agents)
            movePointInsideRegion(a);

        // perform partition computation
        subRegions.clear();
        network.clear();
        boundaryAgents.clear();
        MAPPER.networkPartition(border, team.agents(), subRegions, network, boundaryAgents);

        // assign subregions
        for (EquiTeam.Agent a : subRegions.keySet()) {
            Point2D.Double[] s = subRegions.get(a);
            a.area = weightedArea(s) / a.capacity;
            a.boundaryAgent = boundaryAgents.contains(a);
        }
    }
    
    //
    // SIMULATION LISTENING METHODS
    //

    transient protected SimulationEvent simResetEvent = new SimulationEvent(this, "Reset", 0);
    transient protected SimulationEvent simIterateEvent = new SimulationEvent(this, "Iterate", 0);
    protected ArrayList<SimulationEventListener> simListeners = new ArrayList<SimulationEventListener>();

    public void addSimulationEventListener(SimulationEventListener l) { simListeners.add(l); }
    public void removeSimulationEventListener(SimulationEventListener l) { simListeners.remove(l); }
    public void removeAllSimulationEventListeners() { simListeners.clear(); }
    protected void fireSimResetEvent(double curTime) { simResetEvent.time = curTime; for (SimulationEventListener l : simListeners) l.handleResetEvent(simResetEvent); }
    protected void fireSimIterateEvent(double curTime) { simIterateEvent.time = curTime; for (SimulationEventListener l : simListeners) l.handleIterationEvent(simIterateEvent); }

}
