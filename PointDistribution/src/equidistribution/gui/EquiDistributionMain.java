/*
 * EquiDistributionMain.java
 * Created on Aug 19, 2010, 3:34:07 PM
 */

package equidistribution.gui;

import data.propertysheet.PropertySheet;
import data.propertysheet.PropertySheetDialog;
import data.propertysheet.editor.EditorRegistration;
import equidistribution.scenario.EquiScenarioBean;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import stormtimer.BetterTimer;
import visometry.plane.PlaneAxes;

/**
 *
 * @author elisha
 */
public class EquiDistributionMain extends javax.swing.JFrame {

    EquiFileHandler efh;
    PlaneAxes axes;

    /** Creates new form EquiDistributionMain */
    public EquiDistributionMain() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { }
        initComponents();
        EditorRegistration.registerEditors();

//        propertyP.add("Scenario", scenarioPS);
//        propertyP.add("Region Viz", new PropertySheet(regionPlottable1));

        fitAction.actionPerformed(null);
        plot.add(axes = new PlaneAxes("x", "y", PlaneAxes.AxesType.BOX));
        plot.add(plottable);

        efh = new EquiFileHandler(this);

        controller.addPropertyChangeListener("agents", new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                if (!agentNumSpinnerChange && ((Integer)agentNumSp.getValue()) != controller.getAgentNumber())
                    agentNumSp.setValue(controller.getAgentNumber());
            }
        });
        controller.addPropertyChangeListener("zones", new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                
            }
        });

        new MetricPlotPanel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scenarioPS = new data.propertysheet.PropertySheet();
        controller = new equidistribution.scenario.EquiController();
        plottable = new equidistribution.gui.RegionPlottable();
        zoneTM = new equidistribution.gui.ZoneTableModel();
        statusL = new javax.swing.JLabel();
        toolbar = new javax.swing.JToolBar();
        fitB = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        editRegionB = new javax.swing.JButton();
        randomizeB = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        algorithmComboBox1 = new equidistribution.gui.AlgorithmComboBox();
        iterateB = new javax.swing.JButton();
        runB = new javax.swing.JButton();
        stopB = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        propertyP = new gui.RollupPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        plot = new visometry.plane.PlanePlotComponent();
        jPanel1 = new javax.swing.JPanel();
        metricPanel1 = new equidistribution.gui.MetricPanel();
        metricPlotPanel1 = new equidistribution.gui.MetricPlotPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        agentNumSp = new javax.swing.JSpinner();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        equiDistributionTable1 = new equidistribution.gui.EquiDistributionTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        zoneTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadMI = new javax.swing.JMenuItem();
        saveMI = new javax.swing.JMenuItem();
        saveAsMI = new javax.swing.JMenuItem();
        exitMI = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        editRegionMI = new javax.swing.JMenuItem();
        randomizeMI = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        fitMI = new javax.swing.JMenuItem();
        axesMI = new javax.swing.JMenuItem();
        appearanceMI = new javax.swing.JMenuItem();

        scenarioPS.setBean(controller);

        plottable.setController(controller);

        zoneTM.setController(controller);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EquiDistribution Platform");

        statusL.setText("Status:");
        getContentPane().add(statusL, java.awt.BorderLayout.PAGE_END);

        toolbar.setRollover(true);

        fitB.setAction(fitAction);
        fitB.setText("Fit to Window");
        fitB.setFocusable(false);
        fitB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fitB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(fitB);
        toolbar.add(jSeparator1);

        editRegionB.setAction(editRegionAction);
        editRegionB.setText("Edit Region");
        editRegionB.setToolTipText("<html>\nOpens a dialog box to edit the region's boundaries and priorities.<br>\nYou can also load in an image to trace.\n");
        editRegionB.setFocusable(false);
        editRegionB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editRegionB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(editRegionB);

        randomizeB.setAction(randomizeAction);
        randomizeB.setText("Randomize Pts");
        randomizeB.setFocusable(false);
        randomizeB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        randomizeB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(randomizeB);
        toolbar.add(jSeparator3);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 12));
        jLabel3.setText("Algorithm:  ");
        toolbar.add(jLabel3);

        algorithmComboBox1.setController(controller);
        toolbar.add(algorithmComboBox1);

        iterateB.setAction(iterateAction);
        iterateB.setText("Iterate");
        iterateB.setFocusable(false);
        iterateB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        iterateB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(iterateB);

        runB.setAction(runAction);
        runB.setText("Run");
        runB.setFocusable(false);
        runB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(runB);

        stopB.setAction(stopAction);
        stopB.setText("Stop");
        stopB.setFocusable(false);
        stopB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(stopB);

        getContentPane().add(toolbar, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(8);
        jSplitPane1.setResizeWeight(0.1);
        jSplitPane1.setOneTouchExpandable(true);

        jScrollPane1.setViewportView(propertyP);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane2.setDividerLocation(400);
        jSplitPane2.setDividerSize(8);
        jSplitPane2.setResizeWeight(0.9);
        jSplitPane2.setOneTouchExpandable(true);

        plot.setBackground(new java.awt.Color(255, 252, 245));
        plot.setForeground(new java.awt.Color(255, 255, 255));
        jSplitPane2.setLeftComponent(plot);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        metricPanel1.setController(controller);
        jPanel1.add(metricPanel1);

        metricPlotPanel1.setController(controller);
        jPanel1.add(metricPlotPanel1);

        jSplitPane2.setRightComponent(jPanel1);

        jSplitPane1.setRightComponent(jSplitPane2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("# Agents = ");
        jPanel4.add(jLabel1);

        agentNumSp.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(50), Integer.valueOf(1), null, Integer.valueOf(1)));
        agentNumSp.setMinimumSize(new java.awt.Dimension(80, 22));
        agentNumSp.setPreferredSize(new java.awt.Dimension(80, 22));
        agentNumSp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                agentNumSpStateChanged(evt);
            }
        });
        jPanel4.add(agentNumSp);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jSplitPane3.setDividerLocation(400);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        equiDistributionTable1.setController(controller);
        jScrollPane2.setViewportView(equiDistributionTable1);

        jSplitPane3.setLeftComponent(jScrollPane2);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Zones"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        zoneTable.setModel(zoneTM);
        jScrollPane3.setViewportView(zoneTable);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jSplitPane3.setBottomComponent(jPanel2);

        jPanel3.add(jSplitPane3, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel3);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        loadMI.setAction(loadAction);
        loadMI.setText("Load Scenario");
        jMenu1.add(loadMI);

        saveMI.setAction(saveAction);
        saveMI.setText("Save Scenario");
        jMenu1.add(saveMI);

        saveAsMI.setAction(saveAsAction);
        saveAsMI.setText("Save Scenario As...");
        jMenu1.add(saveAsMI);

        exitMI.setAction(exitAction);
        exitMI.setText("Exit");
        jMenu1.add(exitMI);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        editRegionMI.setAction(editRegionAction);
        editRegionMI.setText("Region");
        jMenu2.add(editRegionMI);

        randomizeMI.setAction(randomizeAction);
        randomizeMI.setText("Randomize Agent Locations");
        jMenu2.add(randomizeMI);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Algorithms");

        jMenuItem2.setAction(iterateAction);
        jMenuItem2.setText("Iterate");
        jMenu4.add(jMenuItem2);

        jMenuItem5.setAction(runAction);
        jMenuItem5.setText("Run");
        jMenu4.add(jMenuItem5);

        jMenuItem3.setAction(stopAction);
        jMenuItem3.setText("Stop");
        jMenu4.add(jMenuItem3);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("View");

        fitMI.setAction(fitAction);
        fitMI.setText("Fit Window to Region");
        jMenu3.add(fitMI);

        axesMI.setAction(axesAction);
        axesMI.setText("Axes...");
        jMenu3.add(axesMI);

        appearanceMI.setAction(appearanceAction);
        appearanceMI.setText("Appearance...");
        jMenu3.add(appearanceMI);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    boolean agentNumSpinnerChange = false;

    private void agentNumSpStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_agentNumSpStateChanged
        agentNumSpinnerChange = true;
        controller.setAgentNumber((Integer) agentNumSp.getValue());
        agentNumSpinnerChange = false;
    }//GEN-LAST:event_agentNumSpStateChanged

    /** Sets text for status bar */
    void setStatus(String status) {
        statusL.setText(status);
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EquiDistributionMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner agentNumSp;
    private equidistribution.gui.AlgorithmComboBox algorithmComboBox1;
    private javax.swing.JMenuItem appearanceMI;
    private javax.swing.JMenuItem axesMI;
    private equidistribution.scenario.EquiController controller;
    private javax.swing.JButton editRegionB;
    private javax.swing.JMenuItem editRegionMI;
    private equidistribution.gui.EquiDistributionTable equiDistributionTable1;
    private javax.swing.JMenuItem exitMI;
    private javax.swing.JButton fitB;
    private javax.swing.JMenuItem fitMI;
    private javax.swing.JButton iterateB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JMenuItem loadMI;
    private equidistribution.gui.MetricPanel metricPanel1;
    private equidistribution.gui.MetricPlotPanel metricPlotPanel1;
    private visometry.plane.PlanePlotComponent plot;
    private equidistribution.gui.RegionPlottable plottable;
    private gui.RollupPanel propertyP;
    private javax.swing.JButton randomizeB;
    private javax.swing.JMenuItem randomizeMI;
    private javax.swing.JButton runB;
    private javax.swing.JMenuItem saveAsMI;
    private javax.swing.JMenuItem saveMI;
    private data.propertysheet.PropertySheet scenarioPS;
    private javax.swing.JLabel statusL;
    private javax.swing.JButton stopB;
    private javax.swing.JToolBar toolbar;
    private equidistribution.gui.ZoneTableModel zoneTM;
    private javax.swing.JTable zoneTable;
    // End of variables declaration//GEN-END:variables


    // FILE ACTIONS

    AbstractAction loadAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            EquiScenarioBean sc = efh.loadScenario();
            if (sc != null)
                sc.updateController(controller);
        }
    };

    AbstractAction saveAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            efh.saveScenario(controller, false);
        }
    };

    AbstractAction saveAsAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            efh.saveScenario(controller, true);
        }
    };

    AbstractAction exitAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };


    // EDIT ACTIONS

    AbstractAction editRegionAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            RegionEditorPanel regionEditor = new RegionEditorPanel(controller);
            regionEditor.efh = efh;
            int result = JOptionPane.showConfirmDialog(EquiDistributionMain.this, regionEditor, "Region Editor",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                regionEditor.copyRegionTo(controller);
                statusL.setText("Successfully loaded region from region editor panel.");
            }
            regionEditor = null;
        }
    };

    AbstractAction randomizeAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            controller.randomizeAgentLocations();
        }
    };

    // VIEW ACTIONS

    AbstractAction fitAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE,
                    maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
            for (Point2D.Double p : controller.getBorder()) {
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                maxX = Math.max(maxX, p.x);
                maxY = Math.max(maxY, p.y);
            }
            double rangeX = minX == maxX ? 1 : maxX - minX;
            double rangeY = minY == maxY ? 1 : maxY - minY;
            plot.setDesiredRange(minX - rangeX/10., minY - rangeY/10., maxX + rangeX/10., maxY + rangeY/10.);
        }
    };

    AbstractAction axesAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            new PropertySheetDialog(EquiDistributionMain.this, false, axes).setVisible(true);
        }
    };

    AbstractAction appearanceAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            RegionPlottableBean bean = new RegionPlottableBean(plottable);
            new PropertySheetDialog(EquiDistributionMain.this, false, bean).setVisible(true);
        }
    };

    // ALGORITHM ACTIONS
    
    BetterTimer timer;

    AbstractAction iterateAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            controller.iterate();
        }
    };

    AbstractAction runAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            timer = new BetterTimer(40);
            timer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        controller.iterate();
                    } catch (Exception ex) {
                        timer.stop();
                        statusL.setText("STATUS: timer stopped unexpectedly due to an exception in computing the Voronoi diagram");
                    }
                }
            });
            timer.start();
        }
    };

    AbstractAction stopAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (timer != null)
                    timer.stop();
        }
    };

}
