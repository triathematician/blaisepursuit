/**
 * PointPackingGUI.java
 * Created on Dec 7, 2009
 */

package main;

import data.propertysheet.PropertySheet;
import data.propertysheet.editor.EditorRegistration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import org.bm.blaise.scio.algorithm.PolygonUtils;
import org.bm.blaise.specto.plane.compgeom.CellVis;
import org.bm.blaise.specto.plane.compgeom.NumberLogPlottable;
import primitive.style.Anchor;
import primitive.style.PointLabeledStyle;
import primitive.style.PointStyle;
import stormtimer.BetterTimer;
import visometry.plane.PlaneAxes;
import visometry.plottable.VPointSet;
import visometry.plottable.VShape;

/**
 * <p>
 *    This class is the main GUI for the point distribution investigation applet.
 * </p>
 * @author Elisha Peterson
 */
public class PointDistributionMain extends javax.swing.JFrame
        implements ChangeListener {

    /** Stores the scenario. */
    DistributionScenario scenario;
    /** Stores the active movement algorithm */
    DistributionAlgorithm algorithm;

    /** Stores the displayed points */
    VPointSet<Point2D.Double> vPoints;
    /** Stores the displayed domain */
    VShape<Point2D.Double> vDomain;
    /** Stores the computed tesselation */
    CellVis vCells;

    NumberLogPlottable maxDevP, meanDevP, meanSqrDevP;
    
    /** Creates new form PointPackingGUI */
    public PointDistributionMain() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { }
        initComponents();
        fc = new JFileChooser();
        EditorRegistration.registerEditors();

        algoBox.setModel(new DefaultComboBoxModel(Algorithms.values()));
        algorithm = (DistributionAlgorithm) algoBox.getSelectedItem();

        metricPlot.setAspectRatio(0.02);
        metricPlot.setDesiredRange(0.0,0.0,300.0,3.0);
        metricPlot.add(new PlaneAxes("step", "metric", PlaneAxes.AxesType.QUADRANT1));
        metricPlot.add(maxDevP = new NumberLogPlottable());
        metricPlot.add(meanDevP = new NumberLogPlottable());
        metricPlot.add(meanSqrDevP = new NumberLogPlottable());

        mainPlot.setDesiredRange(-1, -1, 3, 3);
        mainPlot.add(new PlaneAxes("x", "y", PlaneAxes.AxesType.BOX));

        scenario = new DistributionScenario();
        scenario.addChangeListener(this);
        mainPlot.add(vDomain = new VShape<Point2D.Double>(scenario.getDomain()));
        mainPlot.add(vCells = new CellVis(scenario.polygonMap.values().toArray(new Point2D.Double[][]{})));
        mainPlot.add(vPoints = new VPointSet<Point2D.Double>(scenario.getPoints()));
        vPoints.setPointStyle(new PointLabeledStyle(PointStyle.PointShape.CROSS, 3, Anchor.Center));
        vPoints.addChangeListener(this);
        vDomain.addChangeListener(this);

        table.setScenario(scenario);
        scenario.recompute();

        propPanel.removeAll();
        propPanel.add("Scenario Parameters", new PropertySheet(scenario));
        propPanel.add("Algorithm Parameters", new PropertySheet(new MovementScenarioParameters()));
        propPanel.add("Visible Points", new PropertySheet(vPoints));
        propPanel.add("Visible Domain Polygon", new PropertySheet(vDomain));
        propPanel.add("Visible Area Cells", new PropertySheet(vCells));

    }
    
    AbstractAction initNumberAction = new AbstractAction("Set # of Points") {
        public void actionPerformed(ActionEvent e) {
            int i = -1;
            boolean fail = false;
            while (i <= 0) {
                String s = (String) JOptionPane.showInputDialog(null,
                        "<html>Enter number of initial points in the scenario<br>" + (fail ? "<b>VALUE MUST BE A POSITIVE INTEGER!</b>" : ""),
                        "Scenario setup " + (fail ? " (retry)" : ""),
                    JOptionPane.QUESTION_MESSAGE, null, null, "10");
                try {
                    i = Integer.decode(s);
                } catch (NumberFormatException ex) {}
                fail = true;
            }
            setNumberOfPoints(i);
        }
    };

    void setNumberOfPoints(int n) {
        // sets up with specified number of points, randomly generated inside the polygon and the window (-10,-10) to (10,10)
        Point2D.Double[] pts = new Point2D.Double[n];
        for (int i = 0; i < pts.length; i++) {
            pts[i] = null;
            while (pts[i] == null) {
                pts[i] = new Point2D.Double(20*Math.random()-10, 20*Math.random()-10);
                if (!PolygonUtils.inPolygon(pts[i], scenario.getDomain()))
                    pts[i] = null;
            }
        }
        scenario.setPoints(pts);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new main.PointDistributionTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        meanL = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        maxDevL = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        meanDevL = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        meanSqrDevL = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        scoreL = new javax.swing.JLabel();
        metricPlot = new visometry.plane.PlanePlotComponent();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        propPanel = new gui.RollupPanel();
        mainPlot = new visometry.plane.PlanePlotComponent();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        numButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        algoBox = new javax.swing.JComboBox();
        playButton = new javax.swing.JButton();
        goButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        stepLabel = new javax.swing.JLabel();
        statusBar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setMinimumSize(new java.awt.Dimension(200, 65));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jScrollPane2.setViewportView(table);

        jPanel1.add(jScrollPane2);

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        jPanel2.setLayout(new java.awt.GridLayout(5, 2, 4, 4));

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 18));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Mean Area =");
        jLabel5.setToolTipText("Average of Areas");
        jPanel2.add(jLabel5);

        meanL.setBackground(new java.awt.Color(0, 0, 0));
        meanL.setFont(new java.awt.Font("Courier New", 1, 24));
        meanL.setForeground(new java.awt.Color(255, 255, 255));
        meanL.setText("0.00");
        meanL.setToolTipText("Average of Areas");
        meanL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanL.setOpaque(true);
        jPanel2.add(meanL);

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 18));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Max Dev =");
        jLabel6.setToolTipText("Max Deviation  of Area from Average");
        jPanel2.add(jLabel6);

        maxDevL.setBackground(new java.awt.Color(0, 0, 0));
        maxDevL.setFont(new java.awt.Font("Courier New", 1, 24));
        maxDevL.setForeground(new java.awt.Color(255, 255, 255));
        maxDevL.setText("0.00");
        maxDevL.setToolTipText("Max Deviation  of Area from Average");
        maxDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        maxDevL.setOpaque(true);
        jPanel2.add(maxDevL);

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 18));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Mean Dev =");
        jLabel7.setToolTipText("Average Deviation of Area from Average");
        jPanel2.add(jLabel7);

        meanDevL.setBackground(new java.awt.Color(0, 0, 0));
        meanDevL.setFont(new java.awt.Font("Courier New", 1, 24));
        meanDevL.setForeground(new java.awt.Color(255, 255, 255));
        meanDevL.setText("0.00");
        meanDevL.setToolTipText("Average Deviation of Area from Average");
        meanDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanDevL.setOpaque(true);
        jPanel2.add(meanDevL);

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mean Sqr Dev =");
        jLabel8.setToolTipText("Mean Squared Deviation of Area from Average");
        jPanel2.add(jLabel8);

        meanSqrDevL.setBackground(new java.awt.Color(0, 0, 0));
        meanSqrDevL.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        meanSqrDevL.setForeground(new java.awt.Color(255, 255, 255));
        meanSqrDevL.setText("0.00");
        meanSqrDevL.setToolTipText("Mean Squared Deviation of Area from Average");
        meanSqrDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanSqrDevL.setOpaque(true);
        jPanel2.add(meanSqrDevL);

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("SCORE =");
        jLabel9.setToolTipText("Mean Squared Deviation of Area from Average");
        jPanel2.add(jLabel9);

        scoreL.setBackground(new java.awt.Color(0, 0, 0));
        scoreL.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        scoreL.setForeground(new java.awt.Color(255, 255, 255));
        scoreL.setText("0.00");
        scoreL.setToolTipText("Mean Squared Deviation of Area from Average");
        scoreL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scoreL.setOpaque(true);
        jPanel2.add(scoreL);

        jPanel1.add(jPanel2);

        metricPlot.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.add(metricPlot);

        getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(500, 300));

        jScrollPane1.setViewportView(propPanel);

        jSplitPane1.setLeftComponent(jScrollPane1);
        jSplitPane1.setRightComponent(mainPlot);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setRollover(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 13));
        jLabel2.setText("File: ");
        jToolBar1.add(jLabel2);

        loadButton.setText("Load");
        loadButton.setFocusable(false);
        loadButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loadButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(loadButton);

        saveButton.setText("Save");
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveButton);

        saveAsButton.setText("Save As...");
        saveAsButton.setFocusable(false);
        saveAsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveAsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveAsButton);
        jToolBar1.add(jSeparator1);

        numButton.setAction(initNumberAction);
        numButton.setText("# Points");
        numButton.setFocusable(false);
        numButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        numButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(numButton);
        jToolBar1.add(jSeparator3);

        jLabel1.setText("Algorithm: ");
        jToolBar1.add(jLabel1);

        algoBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        algoBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algoBoxActionPerformed(evt);
            }
        });
        jToolBar1.add(algoBox);

        playButton.setText("Play");
        playButton.setFocusable(false);
        playButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(playButton);

        goButton.setText("Step");
        goButton.setFocusable(false);
        goButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(goButton);

        stopButton.setText("Stop");
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(stopButton);
        jToolBar1.add(jSeparator2);

        stepLabel.setText("step = 0");
        stepLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        jToolBar1.add(stepLabel);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        statusBar.setText("STATUS:");
        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        scenario.setPoints(algorithm.getNewPositions(scenario));
        step++;
        stepLabel.setText("step = " + step);
        statusBar.setText("STATUS: updated point locations");
}//GEN-LAST:event_goButtonActionPerformed

    //
    // FILE HANDLING
    //

    JFileChooser fc;
    File openFile = null;

    /** @return extension of a file */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) ext = s.substring(i+1).toLowerCase();
        return ext;
    }
    /** A filter that shows only XML file */
    final static FileFilter xmlFilter = new FileFilter(){
        public boolean accept(File pathname) { return pathname != null && (pathname.isDirectory() || "xml".equals(getExtension(pathname))); }
        public String getDescription() { return "XML files"; }
    };

    private void loadAction(File file) {
        statusBar.setText("Opening: " + file.getName() + "...");
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            scenario = (DistributionScenario) decoder.readObject();
            decoder.close();
            table.setScenario(scenario);
            scenario.recompute();
            statusBar.setText(statusBar.getText() + "... successful!");
        } catch (FileNotFoundException ex) {
            statusBar.setText(statusBar.getText() + " ERROR -- FILE NOT FOUND!");
        }
    }

    private void saveAction(File file) {
        statusBar.setText("Saving: " + file.getName() + "...");
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
            encoder.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
            encoder.setPersistenceDelegate(Point2.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
            encoder.writeObject(scenario);
            encoder.close();
            statusBar.setText(statusBar.getText() + " successful.");
        } catch (FileNotFoundException ex) {
            statusBar.setText(statusBar.getText() + " ERROR -- FILE NOT FOUND!");
        }
    }

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        fc.setFileFilter(xmlFilter);
        if (openFile != null) {
            fc.setCurrentDirectory(openFile);
            fc.setSelectedFile(openFile);
        }
        if (fc.showOpenDialog(PointDistributionMain.this) == JFileChooser.APPROVE_OPTION) {
            openFile = fc.getSelectedFile();
            loadAction(openFile);
        } else {
            statusBar.setText("Opening file action cancelled by user.");
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (openFile != null) {
            saveAction(openFile);
        } else {
            saveAsButtonActionPerformed(evt);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void algoBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algoBoxActionPerformed
        algorithm = (DistributionAlgorithm) algoBox.getSelectedItem();
        statusBar.setText("STATUS: changed algorithm");
    }//GEN-LAST:event_algoBoxActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        fc.setFileFilter(xmlFilter);
        if (openFile != null) {
            fc.setCurrentDirectory(openFile);
            fc.setSelectedFile(openFile);
        }
        if (fc.showSaveDialog(PointDistributionMain.this) == JFileChooser.APPROVE_OPTION) {
            openFile = fc.getSelectedFile();
            saveAction(openFile);
        } else {
            System.out.println(" save command cancelled by user.");
        }
    }//GEN-LAST:event_saveAsButtonActionPerformed

    BetterTimer timer;
    int step = 0;

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        timer = new BetterTimer(100);
        timer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (!scenario.computing) {
                    try {
                        Point2D.Double[] newPos = algorithm.getNewPositions(scenario);
                        scenario.setPoints(newPos); // this recomputes the visometry
                        step++;
                        stepLabel.setText("step = " + step);
                        statusBar.setText("STATUS: updated point locations");
                    } catch (Exception ex) {
                        timer.stop();
                        statusBar.setText("STATUS: timer stopped unexpectedly due to an exception in computing the Voronoi diagram");
                    }
                }
            }
        });
        step = 0;
        stepLabel.setText("step = " + step);
        timer.start();
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        if (timer != null)
            timer.stop();
        step = 0;
    }//GEN-LAST:event_stopButtonActionPerformed


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PointDistributionMain main = new PointDistributionMain();
                main.initNumberAction.actionPerformed(null);
                main.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox algoBox;
    private javax.swing.JButton goButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton loadButton;
    private visometry.plane.PlanePlotComponent mainPlot;
    private javax.swing.JLabel maxDevL;
    private javax.swing.JLabel meanDevL;
    private javax.swing.JLabel meanL;
    private javax.swing.JLabel meanSqrDevL;
    private visometry.plane.PlanePlotComponent metricPlot;
    private javax.swing.JButton numButton;
    private javax.swing.JButton playButton;
    private gui.RollupPanel propPanel;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel scoreL;
    private javax.swing.JLabel statusBar;
    private javax.swing.JLabel stepLabel;
    private javax.swing.JButton stopButton;
    private main.PointDistributionTable table;
    // End of variables declaration//GEN-END:variables

    boolean adjustingScenario = false;

    public void stateChanged(ChangeEvent e) {

        if (e.getSource() == vPoints) {
            adjustingScenario = true;
            scenario.setPoints(vPoints.getPoint());
            adjustingScenario = false;
        } else if (e.getSource() == vDomain) {
            adjustingScenario = true;
            scenario.setDomain(vDomain.getPoint());
            adjustingScenario = false;
        } else if (e.getSource() == scenario) {
            if (!adjustingScenario) {
                vPoints.setPoint(scenario.getPoints());
                vDomain.setPoint(scenario.getDomain());
            }

            vCells.setPolys(scenario.polygonMap.values().toArray(new Point2D.Double[][]{}));
            table.updateModel();

            
            int n = scenario.getPoints().length;
            double avg = scenario.meanArea();
            double maxdiff = scenario.maxAreaDeviation();
            double sumdiff = scenario.sumDeviation();
            double sumsqdiff = scenario.sumSquaredDeviation();

            meanL.setText(String.format("%.3f", avg));
            maxDevL.setText(varFormatDouble(maxdiff / avg));
            meanDevL.setText(varFormatDouble(sumdiff / (n * avg)));
            meanSqrDevL.setText(varFormatDouble(sumsqdiff / (n * avg * avg)));
            scoreL.setText(varFormatDouble(DistributionMetrics.teamScore(scenario)));

            maxDevP.logValue(maxdiff / avg);
            meanDevP.logValue(sumdiff / (n * avg));
            meanSqrDevP.logValue(sumsqdiff / (n * avg * avg));
            metricPlot.repaint();
        }
    }

    String varFormatDouble(double d) {
        if (Math.abs(d) < .001)
            return String.format("%.3e", d);
        else
            return String.format("%.3f", d);
    }


}
