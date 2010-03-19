/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.geom.Point2D;
import javax.swing.event.ChangeEvent;
import org.bm.blaise.specto.plottable.VPointSet;
import org.bm.blaise.specto.plottable.VPolygon;
import org.bm.blaise.specto.primitive.PointStyle;
import org.bm.blaise.specto.primitive.PointStyle.PointShape;
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
    /** Whether tesselation is displayed. */
    boolean tessVis = true;

    //
    // CONSTRUCTORS
    //
    public DistributionScenarioVis() {
        this(new DistributionScenario());
    }

    public DistributionScenarioVis(DistributionScenarioInterface scenario) {
        this.scenario = scenario;
        points = new VPointSet<Point2D.Double>(scenario.getPoints());
        points.setPointStyle(new PointStyle(PointShape.CROSS, 3));
        boundaryPolygon = new VPolygon<Point2D.Double>(scenario.getBoundaryPolygon());
        add(points);
        add(boundaryPolygon);
    }

    //
    // GETTERS & SETTERS
    //
    public VPolygon<Point2D.Double> getVisualPolygon() {
        return boundaryPolygon;
    }

    public void setVisualPolygon(VPolygon<Point2D.Double> boundaryPolygon) {
        this.boundaryPolygon = boundaryPolygon;
        fireStateChanged();
    }

    public VPointSet<Point2D.Double> getVisualPoints() {
        return points;
    }

    public void setVisualPoints(VPointSet<Point2D.Double> points) {
        this.points = points;
        fireStateChanged();
    }

    public boolean isMouseEditsPoints() {
        return pointsActive;
    }

    public void setMouseEditsPoints(boolean pointsActive) {
        this.pointsActive = pointsActive;
    }

    public boolean isTesselationVisible() {
        return tessVis;
    }

    public void setTesselationVisible(boolean tessVis) {
        this.tessVis = tessVis;
        fireStateChanged();
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

    public void setPolygon(int i, Point2D.Double p) {
        scenario.setBoundaryPolygon(i, p);
    }

    public void setPolygon(Point2D.Double[] polygon) {
        scenario.setBoundaryPolygon(polygon);
    }

    public Point2D.Double getPolygon(int i) {
        return scenario.getBoundaryPolygon(i);
    }

    public Point2D.Double[] getPolygon() {
        return scenario.getBoundaryPolygon();
    }

    public void setPoints(int i, Point2D.Double p) {
        scenario.setPoints(i, p);
    }

    public void setPoints(Point2D.Double[] points) {
        scenario.setPoints(points);
        this.points.setValues(scenario.getPoints());
    }

    public Point2D.Double getPoints(int i) {
        return scenario.getPoints(i);
    }

    public Point2D.Double[] getPoints() {
        return scenario.getPoints();
    }

    @Override
    public String toString() {
        return "Packing Scenario Visualization";
    }

    //
    // CHANGE HANDLING
    //
    @Override
    public void stateChanged(ChangeEvent e) {
        if (!adjusting) {
            if (e.getSource() == points) {
                scenario.setPoints(points.getValues());
            } else if (e.getSource() == boundaryPolygon) {
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
        return new Color((c.getRed() + cr) % 255, (c.getGreen() + cg) % 255, (c.getBlue() + cb) % 255);
    }

    /** @return i'th point color */
    static Color getColor(int i) {
        return new Color((128 + i * 13) % 255, (128 + i * 87) % 255, (128 + i * 147) % 255);
    }

    @Override
    public void draw(VisometryGraphics<Point2D.Double> vg) {
        boundaryPolygon.draw(vg);

        // draw polygons
        if (tessVis) {
            ShapeStyle interiorStyle = new ShapeStyle();
            vg.setShapeStyle(interiorStyle);
            Color is = interiorStyle.getFillColor();
            int i = 0;
            for (Point2D.Double p : scenario.getPoints()) {
                interiorStyle.setFillColor(getColor(i));
                i++;
                try {
                    vg.drawShape(scenario.getNearestPolygon(p));
                } catch (NullPointerException ex) {
                }
            }
            interiorStyle.setFillColor(is);
        }

        points.draw(vg);
    }

    //
    // EVENT HANDLING
    //
    public boolean handleCoordinate(Point2D.Double coord) {
        return pointsActive ? points.handleCoordinate(coord) : boundaryPolygon.handleCoordinate(coord);
    }
}
