/**
 * PointPackingGUI.java
 * Created on Dec 7, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import data.propertysheet.PropertySheet;
import data.propertysheet.editor.EditorRegistration;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bm.blaise.scio.algorithm.Tesselation;
import org.bm.blaise.specto.plane.compgeom.PlaneConvexHull;
import org.bm.blaise.specto.plane.compgeom.PlaneTesselation;
import org.bm.blaise.specto.plane.compgeom.PlaneVoronoiFrontier;
import visometry.PlotComponent;
import visometry.plane.PlaneAxes;
import visometry.plane.PlaneGrid;
import visometry.plottable.Plottable;
import visometry.plottable.VLine;
import visometry.plottable.VPointSet;
import visometry.plottable.VShape;

/**
 * <p>
 *    This class is the main GUI for the point packing investigation applet.
 * </p>
 * @author Elisha Peterson
 */
public class TestApp extends javax.swing.JFrame {

    VShape<Point2D.Double> polygon;
    VPointSet<Point2D.Double> points;

    VShape<Point2D.Double> pClip;
    VShape<Point2D.Double> pClipee;
    VShape<Point2D.Double> pClipped;

    /** Creates new form PointPackingGUI */
    public TestApp() {
        initComponents();

        tessPlot.add(new PlaneAxes("x", "y", PlaneAxes.AxesType.BOX));
        //scenarioPlot.addPlottable(PlaneGrid.instance());
        tessPlot.setDesiredRange(-5, -5, 5, 5);

        Tesselation tess = new Tesselation();
        tess.addPolygon(new Double(0,0), new Double(1,0), new Double(0,1));
        tess.addPolygon(new Double(1,0), new Double(0,1), new Double(1,1));
        tess.addPolygon(new Double(1,0), new Double(1,1), new Double(2,1), new Double(2,0));
        tess.addPolygon(new Double(1,1), new Double(2,1), new Double(java.lang.Double.POSITIVE_INFINITY, Math.PI/4), new Double(java.lang.Double.POSITIVE_INFINITY, 3*Math.PI/4));
        tessPlot.add(new PlaneTesselation(tess));

        voronoiPlot.add(new PlaneAxes("x", "y", PlaneAxes.AxesType.BOX));
        voronoiPlot.add(new PlaneGrid());
        voronoiPlot.setDesiredRange(-2, -5, 5, 5);
        PlaneVoronoiFrontier vor = new PlaneVoronoiFrontier( new Point2D.Double[]{
            new Point2D.Double(0,0), new Point2D.Double(0,1), new Point2D.Double(.25,.5)
        });
        voronoiPlot.add(vor);
//        voronoiPlot.setDefaultCoordinateHandler(vor);

        PlaneConvexHull hull = new PlaneConvexHull( new Point2D.Double[]{
            new Point2D.Double(1,0), new Point2D.Double(0,1), new Point2D.Double(.5,3), new Point2D.Double(1.2, .2),
            new Point2D.Double(-.5,.6)
        });
        hullPlot.add(hull);
//        hullPlot.setDefaultCoordinateHandler(hull);

//        TestClipSegment tSeg = new TestClipSegment();
//        clipPlot.add(tSeg);
////        clipPlot.setDefaultCoordinateHandler(tSeg);
//
//        pClip = new VShape<Point2D.Double>(new Point2D.Double(0,0), new Point2D.Double(1,0), new Point2D.Double(1,1), new Point2D.Double(0,1));
//        pClipee = new VShape<Point2D.Double>(new Point2D.Double(.5,.5), new Point2D.Double(-.1,.3), new Point2D.Double(1.5,.2));
//        pClipped = new VShape<Point2D.Double>(PolygonIntersectionUtils.intersectionOfConvexPolygons(pClipee.getValues(), pClip.getValues()));
//        clipPlot1.add(pClip);
//        clipPlot1.add(pClipee);
//        clipPlot1.add(pClipped);
////        clipPlot1.setDefaultCoordinateHandler(pClipee);
//        pClipped.setEditable(false);
//        ChangeListener reclip = new ChangeListener(){
//            public void stateChanged(ChangeEvent e) {
//                pClipped.setValues(PolygonIntersectionUtils.intersectionOfConvexPolygons(pClipee.getValues(), pClip.getValues()));
//            }
//        };
//        pClip.addChangeListener(reclip);
//        pClipee.addChangeListener(reclip);

        EditorRegistration.registerEditors();
        propPanel.removeAll();
        propPanel.add("Visometry", new PropertySheet(tessPlot.getVisometry()));
        for (Plottable p : tessPlot.getPlottableArray())
            propPanel.add(p.toString(), new PropertySheet(p));
        // add stuff to rollup panel
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scenario = new main.DistributionScenario();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tessPlot = new visometry.plane.PlanePlotComponent();
        hullPlot = new visometry.plane.PlanePlotComponent();
        voronoiPlot = new visometry.plane.PlanePlotComponent();
        clipPlot = new visometry.plane.PlanePlotComponent();
        clipPlot1 = new visometry.plane.PlanePlotComponent();
        jScrollPane1 = new javax.swing.JScrollPane();
        propPanel = new gui.RollupPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tessPlotLayout = new org.jdesktop.layout.GroupLayout(tessPlot);
        tessPlot.setLayout(tessPlotLayout);
        tessPlotLayout.setHorizontalGroup(
            tessPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );
        tessPlotLayout.setVerticalGroup(
            tessPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Tesselation", tessPlot);

        org.jdesktop.layout.GroupLayout hullPlotLayout = new org.jdesktop.layout.GroupLayout(hullPlot);
        hullPlot.setLayout(hullPlotLayout);
        hullPlotLayout.setHorizontalGroup(
            hullPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );
        hullPlotLayout.setVerticalGroup(
            hullPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Hull", hullPlot);

        org.jdesktop.layout.GroupLayout voronoiPlotLayout = new org.jdesktop.layout.GroupLayout(voronoiPlot);
        voronoiPlot.setLayout(voronoiPlotLayout);
        voronoiPlotLayout.setHorizontalGroup(
            voronoiPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );
        voronoiPlotLayout.setVerticalGroup(
            voronoiPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Voronoi", voronoiPlot);

        org.jdesktop.layout.GroupLayout clipPlotLayout = new org.jdesktop.layout.GroupLayout(clipPlot);
        clipPlot.setLayout(clipPlotLayout);
        clipPlotLayout.setHorizontalGroup(
            clipPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );
        clipPlotLayout.setVerticalGroup(
            clipPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Clip #1", clipPlot);

        org.jdesktop.layout.GroupLayout clipPlot1Layout = new org.jdesktop.layout.GroupLayout(clipPlot1);
        clipPlot1.setLayout(clipPlot1Layout);
        clipPlot1Layout.setHorizontalGroup(
            clipPlot1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );
        clipPlot1Layout.setVerticalGroup(
            clipPlot1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Clip #2", clipPlot1);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(propPanel);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.EAST);

        jToolBar1.setRollover(true);

        jButton1.setText("GO!");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("do something here...");
}//GEN-LAST:event_jButton1ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
    if (jTabbedPane1.getSelectedComponent() instanceof PlotComponent) {
        propPanel.removeAll();
        PlotComponent ppc = (PlotComponent) jTabbedPane1.getSelectedComponent();
        propPanel.add("Visometry", new PropertySheet(ppc.getVisometry()));
        for (Object p : ppc.getPlottableArray()) {
            propPanel.add(p.toString(), new PropertySheet(p));
        }
    }

    }//GEN-LAST:event_jTabbedPane1StateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private visometry.plane.PlanePlotComponent clipPlot;
    private visometry.plane.PlanePlotComponent clipPlot1;
    private visometry.plane.PlanePlotComponent hullPlot;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private gui.RollupPanel propPanel;
    private main.DistributionScenario scenario;
    private visometry.plane.PlanePlotComponent tessPlot;
    private visometry.plane.PlanePlotComponent voronoiPlot;
    // End of variables declaration//GEN-END:variables

}

//class TestClipSegment extends VPolygon<Point2D.Double> {
//
//    VLine<Point2D.Double> segment;
//
//    public TestClipSegment() {
//        super(new Point2D.Double(0,0), new Point2D.Double(1,1), new Point2D.Double(2,0));
//        segment = new VLine<Point2D.Double>(new Point2D.Double(1,0), new Point2D.Double(2,1));
//        segment.addChangeListener(this);
//    }
//
//    @Override
//    public void draw(VisometryGraphics<Double> vg) {
//        super.draw(vg);
//        segment.draw(vg);
////        Point2D.Double[] clipped = PolygonUtils.clipSegment(new Point2D.Double[]{segment.getValue1(), segment.getValue2()}, super.getValues());
//        Point2D.Double[][] res = PolygonUtils.intersectionOf(new Point2D.Double[]{segment.getValue1(), segment.getValue2()}, super.getValues(), new ArrayList<Integer[]>());
//        for (Point2D.Double[] seg : res) {
//            vg.drawSegment(seg[0], seg[1]);
//        }
//    }
//
//    @Override
//    public boolean isClickablyCloseTo(VisometryMouseEvent<Double> e) {
//        return super.isClickablyCloseTo(e) || segment.isClickablyCloseTo(e);
//    }
//
//    @Override
//    public void mousePressed(VisometryMouseEvent<Double> e) {
//        if (super.isClickablyCloseTo(e)) {
//            adjusting = true;
//        } else if (segment.isClickablyCloseTo(e)) {
//            segment.adjusting = true;
//        }
//    }
//
//    @Override
//    public void mouseClicked(VisometryMouseEvent<Double> e) {
//        if (selectedIndex == -1)
//            segment.mouseClicked(e);
//        else
//            super.mouseClicked(e);
//    }
//
//    @Override
//    public void mouseDragged(VisometryMouseEvent<Double> e) {
//        if (selectedIndex == -1)
//            segment.mouseDragged(e);
//        else
//            super.mouseDragged(e);
//    }
//
//    @Override
//    public void mouseReleased(VisometryMouseEvent<Double> e) {
//        if (selectedIndex == -1)
//            segment.mouseReleased(e);
//        else
//            super.mouseReleased(e);
//    }
//}