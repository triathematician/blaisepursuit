/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package equidistribution.gui;

import equidistribution.scenario.EquiController;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.bm.blaise.specto.plane.compgeom.CellVis;
import primitive.style.Anchor;
import primitive.style.PointLabeledStyle;
import primitive.style.PointStyle;
import primitive.style.ShapeStyle;
import visometry.plane.PlanePathPlottable;
import visometry.plottable.Plottable;
import visometry.plottable.PlottableGroup;
import visometry.plottable.VPointSet;
import visometry.plottable.VShape;

/**
 * Contains elements for plotting a region with special subzones.
 *
 * @author Elisha Peterson
 */
public class RegionPlottable extends PlottableGroup<Point2D.Double>
        implements PropertyChangeListener {

    EquiController controller;
    
    ShapeStyle zoneStyle;
    VPointSet<Point2D.Double> agents;
    VShape<Point2D.Double> borderShape;
    LotsOLines network;
    CellVis partition;
    ArrayList<VShape<Point2D.Double>> zoneShapes;

    public RegionPlottable() {
        this(null);
    }

    public RegionPlottable(EquiController sc) {
        zoneStyle = new ShapeStyle(new Color(255, 32, 32), new Color(255, 192, 192));
        zoneStyle.setThickness(2f);
        zoneStyle.setFillOpacity(.5f);
        setController(sc);
    }

    public EquiController getController() {
        return controller;
    }

    public void setController(EquiController sc) {
        if (controller != null)
            controller.removePropertyChangeListener(this);
        controller = sc;
        if (controller != null) {
            controller.addPropertyChangeListener(this);
            clear();
            initRegion();
            initPartition();
            initNetwork();
            initZones();
            initAgents();
            firePlottableChanged();
        }
    }

    // INITIALIZERS

    /** Sets up the region plottables. */
    private void initRegion() {
        if (borderShape == null)
            borderShape = new VShape<Point2D.Double>(controller.getBorder());
        else
            borderShape.setPoint(controller.getBorder());
        borderShape.setShapeStyle(new ShapeStyle(Color.BLACK, null));
        add(borderShape);
    }

    private void initPartition() {
        if (partition == null) {
            partition = new CellVis(controller.getPartition());
        } else
            partition.setPolys(controller.getPartition());
        add(partition);
    }

    private void initNetwork() {
        if (network == null) {
            network = new LotsOLines(controller.getNetwork());
        } else
            network.setLines(controller.getNetwork());
        add(network);
    }

    private void initZones() {
        if (zoneShapes == null)
            zoneShapes = new ArrayList<VShape<Point2D.Double>>();
        else {
            for (VShape sh : zoneShapes)
                remove(sh);
            zoneShapes.clear();
        }
        if (agents != null)
            remove(agents);
        VShape<Point2D.Double> zone = null;
        for (Point2D.Double[] sh : controller.getZones()) {
            zone = new VShape<Point2D.Double>(sh);
            zone.setShapeStyle(zoneStyle);
            zoneShapes.add(zone);
            add(zone);
        }
        if (agents != null)
            add(agents);
    }

    private void initAgents() {
        if (agents == null) {
            agents = new VPointSet<Point2D.Double>(controller.getAgentLoc());
            PointLabeledStyle pStyle = new PointLabeledStyle(PointStyle.PointShape.CROSS, 4, Anchor.North);
            pStyle.setThickness(2f);
            pStyle.setLabelVisible(false);
            agents.setPointStyle(pStyle);
        } else
            agents.setPoint(controller.getAgentLoc());
        add(agents);
    }

    // PROPERTIES

    public ShapeStyle getStyle() { return zoneStyle; }
    public void setStyle(ShapeStyle newStyle) {
        if (zoneStyle != newStyle) {
            zoneStyle = newStyle;
            borderShape.setShapeStyle(newStyle);
            for (VShape<Point2D.Double> sh : zoneShapes)
                sh.setShapeStyle(newStyle);
        }
    }
    
    // LISTENING METHODS

    private Plottable changing = null;

    @Override
    public void plottableChanged(Plottable p) {
        if (p == borderShape) {
            changing = borderShape;
            controller.setBorder(borderShape.getPoint());
        } else if (p == agents) {
            changing = agents;
            controller.setAgentLoc(agents.getPoint());
        } else {
            int i = 0;
            for (VShape<Point2D.Double> s : zoneShapes) {
                if (p == s) {
                    changing = s;
                    controller.setZones(i, s.getPoint());
                    break;
                }
                i++;
            }
        }
        changing = null;
        firePlottableChanged();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == controller) {
            IndexedPropertyChangeEvent iEvt = evt instanceof IndexedPropertyChangeEvent ? (IndexedPropertyChangeEvent) evt : null;
            Object newVal = evt.getNewValue();
            if (evt.getPropertyName().equals("scenario")) {
                setController((EquiController) evt.getNewValue());
            } else if (evt.getPropertyName().equals("border")) {
                if (iEvt == null)
                    borderShape.setPoint((Point2D.Double[]) newVal);
                else
                    borderShape.setPoint(iEvt.getIndex(), (Point2D.Double) newVal);
            } else if (evt.getPropertyName().equals("zones")) {
                if (iEvt == null) {
                    initZones();
                    firePlottableChanged();
                } else
                    zoneShapes.get(iEvt.getIndex()).setPoint((Point2D.Double[]) newVal);
            } else if (evt.getPropertyName().equals("locations") && changing != agents) {
                if (iEvt == null)
                    agents.setPoint((Point2D.Double[]) newVal);
                else
                    agents.setPoint(iEvt.getIndex(), (Point2D.Double) newVal);
            } else if (evt.getPropertyName().equals("partition")) {
                partition.setPolys((Point2D.Double[][])evt.getNewValue());
                network.setLines(controller.getNetwork());
            }
        }
    }


    // INNER CLASSES

    static class LotsOLines extends PlanePathPlottable {

        Point2D.Double[][] lines = null;

        public LotsOLines(Point2D.Double[][] lines) {
            this.lines = lines;
            getStyle().setStrokeColor(Color.GRAY);
            getStyle().setThickness(.5f);
        }

        /** Sets line-pairs used to draw */
        public void setLines(Point2D.Double[][] lines) {
            this.lines = lines;
            firePlottableChanged();
            entry.local = path = new GeneralPath();
            for (Point2D.Double[] l : lines) {
                path.moveTo((float) l[0].x, (float) l[0].y);
                path.lineTo((float) l[1].x, (float) l[1].y);
            }
            needsComputation = false;
            entry.needsConversion = true;
        }

        @Override
        protected void recompute() {
            super.recompute();
        }
        
    }
}
