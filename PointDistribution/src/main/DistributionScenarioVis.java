/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.bm.blaise.specto.plottable.VPointSet;
import org.bm.blaise.specto.plottable.VPolygon;
import org.bm.blaise.specto.primitive.ShapeStyle;
import org.bm.blaise.specto.visometry.CoordinateHandler;
import org.bm.blaise.specto.visometry.PlottableGroup;
import org.bm.blaise.specto.visometry.VisometryGraphics;

/**
 * Visualization of the point-packing in a polygon scenario.
 * @author ae3263
 */
public class DistributionScenarioVis extends PlottableGroup<Point2D.Double> implements CoordinateHandler<Point2D.Double> {

    /** Stores the scenario */
    DistributionScenarioInterface scenario;
    /** Visuals for the points */
    VPointSet<Point2D.Double> points;
    /** Visuals for the polygon */
    VPolygon<Point2D.Double> boundaryPolygon;

    /** Controls who handles new coordinates. */
    boolean pointsActive = true;

    //
    // CONSTRUCTORS
    //

    public DistributionScenarioVis() {
        this(new DistributionScenario());
    }

    public DistributionScenarioVis(DistributionScenarioInterface scenario) {
        this.scenario = scenario;
        points = new VPointSet<Point2D.Double>(scenario.getPoints());
        boundaryPolygon = new VPolygon<Point2D.Double>(scenario.getBoundaryPolygon());
        add(points);
        add(boundaryPolygon);
    }

    //
    // GETTERS & SETTERS
    //

    public VPolygon<Double> getBoundaryPolygon() {
        return boundaryPolygon;
    }

    public void setBoundaryPolygon(VPolygon<Double> boundaryPolygon) {
        this.boundaryPolygon = boundaryPolygon;
        fireStateChanged();
    }

    public VPointSet<Double> getPoints() {
        return points;
    }

    public void setPoints(VPointSet<Double> points) {
        this.points = points;
        fireStateChanged();
    }

    public boolean isPointsActive() {
        return pointsActive;
    }

    public void setPointsActive(boolean pointsActive) {
        this.pointsActive = pointsActive;
    }

    public DistributionScenarioInterface getScenario() {
        return scenario;
    }

    public void setScenario(DistributionScenarioInterface scenario) {
        adjusting = true;
        this.scenario = scenario;
        points.setValues(scenario.getPoints());
        boundaryPolygon.setValues(scenario.getBoundaryPolygon());
        adjusting = false;
        fireStateChanged();
    }

    @Override public String toString() { return "Packing Scenario Visualization"; }

    //
    // CHANGE HANDLING
    //

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!adjusting) {
            if (e.getSource()==points) {
                scenario.setPoints(points.getValues());
            } else if (e.getSource()==boundaryPolygon) {
                scenario.setBoundaryPolygon(boundaryPolygon.getValues());
            }
            super.stateChanged(e);
        }
    }

    //
    // DRAWING METHOSD
    //
    
    /** Cycles through colors. */
    protected static Color nextColor(Color c, int cr, int cg, int cb) {
        return new Color((c.getRed()+cr) % 255, (c.getGreen()+cg)%255, (c.getBlue()+cb) % 255);
    }

    /** @return i'th point color */
    static Color getColor(int i) {
        return new Color((128+i*13) % 255, (128+i*87) % 255, (128+i*147) % 255);
    }

    @Override
    public void paintComponent(VisometryGraphics<Point2D.Double> vg) {
        boundaryPolygon.paintComponent(vg);

        // draw polygons
        ShapeStyle interiorStyle = new ShapeStyle();
        vg.setShapeStyle(interiorStyle);
        Color is = interiorStyle.getFillColor();
        int i = 0;
        for (Point2D.Double p : scenario.getPoints()) {
            interiorStyle.setFillColor(getColor(i));
            i++;
            try {
                vg.drawClosedPath(scenario.getNearestPolygon(p));
            } catch (NullPointerException ex) {
            }
        }
        interiorStyle.setFillColor(is);

        points.paintComponent(vg);
    }

    //
    // EVENT HANDLING
    //

    public boolean handleCoordinate(Point2D.Double coord) {
        return pointsActive ? points.handleCoordinate(coord) : boundaryPolygon.handleCoordinate(coord);
    }

}
