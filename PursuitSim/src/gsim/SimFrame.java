/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimFrame.java
 *
 * Created on Aug 17, 2009, 1:44:54 PM
 */

package gsim;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyEditorManager;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.propertysheet.IndexedPropertySheet;
import data.propertysheet.PropertySheet;
import data.propertysheet.PropertySheetDialog;
import data.propertysheet.editor.EnumObjectEditor;
import javax.swing.event.ListSelectionEvent;

import org.bm.blaise.specto.plane.PlaneAxes;
import org.bm.blaise.specto.plane.PlaneGrid;
import org.bm.blaise.specto.visometry.Plottable;
import org.bm.blaise.sequor.timer.BetterTimeClock;

import gsim.customizers.*;
import gsim.logger.*;
import gsim.plottables.*;
import gsim.samples.SampleSims;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import sim.Simulation;
import sim.SimComponent;
import sim.component.team.LocationGenerator;
import sim.comms.Sensor;
import sim.metrics.*;
import sim.tasks.*;

/**
 *
 * @author ae3263
 */
public class SimFrame extends javax.swing.JFrame 
        implements ActionListener, ChangeListener, ListSelectionListener {

    /** The simulation */
    Simulation sim;
    /** Stores the currently "selected" component. */
    SimComponent selected;
    /** The timer used to animate plots in both windows. */
    BetterTimeClock timer;
    /** This object handles input/output to files. */
    SimFileActions sfa;

    //===============================================================================
    //
    //                      CONSTRUCTOR
    //
    
    /** Creates new form SimFrame */
    public SimFrame() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { }

        data.propertysheet.editor.EditorRegistration.registerEditors();

        PropertyEditorManager.registerEditor(Sensor.class, EnumObjectEditor.class);
        PropertyEditorManager.registerEditor(LocationGenerator.class, EnumObjectEditor.class);
        PropertyEditorManager.registerEditor(Router.class, EnumObjectEditor.class);
        PropertyEditorManager.registerEditor(TaskChooser.class, EnumObjectEditor.class);
        PropertyEditorManager.registerEditor(Tasker.class, EnumObjectEditor.class);

        PropertyEditorManager.registerEditor(VictoryCondition.class, VictoryEditor.class);
        PropertyEditorManager.registerEditor(Capture.class, CaptureEditor.class);


        timer = new BetterTimeClock();
        timer.addActionListener(this);

        sfa = new SimFileActions(this);
        initComponents();
        cleanupToolbar();
        simComponentList.addListSelectionListener(this);

        loadPresetMenu();
    }

    void cleanupToolbar() {
        for (Component c : toolbar.getComponents())
            if (c instanceof JButton && ((JButton)c).getIcon() != null)
                ((JButton)c).setText("");
    }

    void loadPresetMenu() {
        int presetNumber = 0;
        for (final SampleSims sample : SampleSims.values()) {
            JMenuItem mi = new JMenuItem(sample.toString());
            mi.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    logs = new ArrayList<SimulationLogger>();
                    initSimulation(sample.getSimulation(logs));
                }
            });
            mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0+presetNumber, InputEvent.CTRL_MASK));
            presetNumber++;
            presetMenu.add(mi);
        }
        logs = new ArrayList<SimulationLogger>();
        initSimulation(SampleSims.values()[0].getSimulation(logs));
    }

    //
    // INITIALIZERS
    //
    
    ArrayList<SimulationLogger> logs;

    PlaneGrid grid1,grid2;
    PlaneAxes axes1,axes2;

    private void initPlots() {
        if (grid1 == null)
            grid1 = new PlaneGrid();
        if (axes1 == null)
            axes1 = new PlaneAxes("x", "y", PlaneAxes.Style.BOX);
        if (grid2 == null)
            grid2 = new PlaneGrid();
        if (axes2 == null)
            axes2 = new PlaneAxes();
        
        logPlot.setTimeClock(timer);
        mainPlot.setTimeClock(timer);

        mainPlot.removeAllPlottables();
        mainPlot.addPlottable(grid1);
        mainPlot.addPlottable(axes1);
        mainPlot.setDesiredRange(-70, -70, 70, 70);

        logPlot.removeAllPlottables();
        logPlot.addPlottable(grid2);
        logPlot.addPlottable(axes2);
        logPlot.setDesiredRange(0,-50,100,50);
    }

    void initSimulation(Simulation sim) {
        if (sim == null)
            throw new IllegalArgumentException("initSimulation called with null");

        initPlots();

        if (this.sim != null)
            this.sim.removeChangeListener(this);
        this.sim = sim;
        sim.addChangeListener(this);
        List<SimulationLogger> el = EssentialLogger.getEssentialLoggersFor(sim);
        for (SimulationLogger asl : logs)
            sim.addSimulationEventListener(asl);
        sim.run();

        for (SimulationLogger abs : el) {
            if (abs instanceof TeamLogger) {
                TeamPlottableGroup tpg = new TeamPlottableGroup((TeamLogger) abs);
                mainPlot.addPlottable(tpg);
                tpg.addChangeListener(this);
            } else if (abs instanceof AgentLogger) {
                AgentPlottable ap = new AgentPlottable((AgentLogger) abs);
                mainPlot.addPlottable(ap);
                ap.addChangeListener(this);
            } else if (abs instanceof MetricLogger) {
                MetricPlottable mpg = new MetricPlottable((MetricLogger) abs);
                logPlot.addPlottable(mpg);
                mpg.addChangeListener(this);
            }
        }
        
        for (SimulationLogger asl : logs) {
            if (asl instanceof MetricLogger) {
                MetricPlottable mpg = new MetricPlottable((MetricLogger) asl);
                logPlot.addPlottable(mpg);
                mpg.addChangeListener(this);
            }
        }

        PropertySheet ps;
        propPanel.removeAll();
        ps = new PropertySheet(sim);
        propPanel.add("Simulation", ps);

        for (SimComponent sc : sim.getComponent()) {
            ps = new PropertySheet(sc);
            propPanel.add(sc.toString(), ps);
        }

        propPanel.add("Main Plot Elements", new IndexedPropertySheet(mainPlot, "plottableArray"));
        propPanel.add("Metric Plot Elements", new IndexedPropertySheet(logPlot, "plottableArray"));
        propPanelSP.validate();

        simComponentList.setSim(sim);
        Simulation.ACTIVE_SIM = sim;
        sfa.enableSaveActions();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbar = new javax.swing.JToolBar();
        newTB = new javax.swing.JButton();
        editTB = new javax.swing.JButton();
        loadTB = new javax.swing.JButton();
        saveTB = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        runAgainButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        restartTB = new javax.swing.JButton();
        playTB = new javax.swing.JButton();
        pauseTB = new javax.swing.JButton();
        stopTB = new javax.swing.JButton();
        slowTB = new javax.swing.JButton();
        fastTB = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        mainSP = new javax.swing.JSplitPane();
        plotSP = new javax.swing.JSplitPane();
        mainPlot = new org.bm.blaise.specto.plane.PlanePlotComponent();
        logPlot = new org.bm.blaise.specto.plane.PlanePlotComponent();
        settingsTP = new javax.swing.JTabbedPane();
        componentSP = new javax.swing.JScrollPane();
        simComponentList = new gsim.editor.SimulationComponentList();
        propPanelSP = new javax.swing.JScrollPane();
        propPanel = new gui.RollupPanel();
        statusLabel = new javax.swing.JLabel();
        mainMenu = new javax.swing.JMenuBar();
        simMenu = new javax.swing.JMenu();
        newMI = new javax.swing.JMenuItem();
        editMI = new javax.swing.JMenuItem();
        loadMI = new javax.swing.JMenuItem();
        saveMI = new javax.swing.JMenuItem();
        saveAsMI = new javax.swing.JMenuItem();
        exitMI = new javax.swing.JMenuItem();
        presetMenu = new javax.swing.JMenu();
        optionsMenu = new javax.swing.JMenu();
        timerSettingsMI = new javax.swing.JMenuItem();
        mainPlotMI = new javax.swing.JMenu();
        boundsMI1 = new javax.swing.JMenuItem();
        axesVisMI1 = new javax.swing.JCheckBoxMenuItem();
        axesMI1 = new javax.swing.JMenuItem();
        gridVisMI1 = new javax.swing.JCheckBoxMenuItem();
        gridMI1 = new javax.swing.JMenuItem();
        metricPlotMI = new javax.swing.JMenu();
        boundsMI2 = new javax.swing.JMenuItem();
        axesVisMI2 = new javax.swing.JCheckBoxMenuItem();
        axesMI2 = new javax.swing.JMenuItem();
        gridVisMI2 = new javax.swing.JCheckBoxMenuItem();
        gridMI2 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMI = new javax.swing.JMenuItem();
        aboutMI = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pursuit & Evasion Simulations");

        toolbar.setRollover(true);

        newTB.setAction(sfa.newAction);
        newTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/document_32.png"))); // NOI18N
        newTB.setToolTipText("");
        newTB.setFocusable(false);
        newTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(newTB);

        editTB.setAction(sfa.editAction);
        editTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/pencil_32.png"))); // NOI18N
        editTB.setFocusable(false);
        editTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(editTB);

        loadTB.setAction(sfa.loadAction);
        loadTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/folder_32.png"))); // NOI18N
        loadTB.setFocusable(false);
        loadTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loadTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(loadTB);

        saveTB.setAction(sfa.saveAction);
        saveTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/save_32.png"))); // NOI18N
        saveTB.setFocusable(false);
        saveTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(saveTB);
        toolbar.add(jSeparator1);

        runAgainButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/green_button.png"))); // NOI18N
        runAgainButton.setFocusable(false);
        runAgainButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runAgainButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runAgainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAgainButtonActionPerformed(evt);
            }
        });
        toolbar.add(runAgainButton);
        toolbar.add(jSeparator2);

        restartTB.setAction(timer.getRestartAction());
        restartTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/skip_backward.png"))); // NOI18N
        restartTB.setFocusable(false);
        restartTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        restartTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(restartTB);

        playTB.setAction(timer.getPlayAction());
        playTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/play.png"))); // NOI18N
        playTB.setFocusable(false);
        playTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(playTB);

        pauseTB.setAction(timer.getPauseAction());
        pauseTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/pause.png"))); // NOI18N
        pauseTB.setFocusable(false);
        pauseTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pauseTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(pauseTB);

        stopTB.setAction(timer.getStopAction());
        stopTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/stop.png"))); // NOI18N
        stopTB.setFocusable(false);
        stopTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(stopTB);

        slowTB.setAction(timer.getSlowDownAction());
        slowTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/skip_forward.png"))); // NOI18N
        slowTB.setFocusable(false);
        slowTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        slowTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(slowTB);

        fastTB.setAction(timer.getSpeedUpAction());
        fastTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/fast_forward.png"))); // NOI18N
        fastTB.setFocusable(false);
        fastTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fastTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(fastTB);

        timeLabel.setText("Time = ");
        timeLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        toolbar.add(timeLabel);

        getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

        mainSP.setDividerLocation(300);
        mainSP.setDividerSize(8);
        mainSP.setOneTouchExpandable(true);
        mainSP.setPreferredSize(new java.awt.Dimension(1000, 700));

        plotSP.setDividerLocation(500);
        plotSP.setDividerSize(8);
        plotSP.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        plotSP.setResizeWeight(0.75);
        plotSP.setOneTouchExpandable(true);

        mainPlot.setMinimumSize(new java.awt.Dimension(200, 200));
        mainPlot.setPreferredSize(new java.awt.Dimension(500, 500));

        org.jdesktop.layout.GroupLayout mainPlotLayout = new org.jdesktop.layout.GroupLayout(mainPlot);
        mainPlot.setLayout(mainPlotLayout);
        mainPlotLayout.setHorizontalGroup(
            mainPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 615, Short.MAX_VALUE)
        );
        mainPlotLayout.setVerticalGroup(
            mainPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        plotSP.setLeftComponent(mainPlot);

        logPlot.setMinimumSize(new java.awt.Dimension(200, 150));
        logPlot.setPreferredSize(new java.awt.Dimension(400, 200));

        org.jdesktop.layout.GroupLayout logPlotLayout = new org.jdesktop.layout.GroupLayout(logPlot);
        logPlot.setLayout(logPlotLayout);
        logPlotLayout.setHorizontalGroup(
            logPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 615, Short.MAX_VALUE)
        );
        logPlotLayout.setVerticalGroup(
            logPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 171, Short.MAX_VALUE)
        );

        plotSP.setRightComponent(logPlot);

        mainSP.setRightComponent(plotSP);

        settingsTP.setPreferredSize(new java.awt.Dimension(300, 400));

        componentSP.setViewportView(simComponentList);

        settingsTP.addTab("Basic Settings", componentSP);

        propPanelSP.setViewportView(propPanel);

        settingsTP.addTab("Advanced Settings", propPanelSP);

        mainSP.setLeftComponent(settingsTP);

        getContentPane().add(mainSP, java.awt.BorderLayout.CENTER);

        statusLabel.setText("Status");
        getContentPane().add(statusLabel, java.awt.BorderLayout.PAGE_END);

        simMenu.setText("Simulation");

        newMI.setAction(sfa.newAction);
        newMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/document_16.png"))); // NOI18N
        simMenu.add(newMI);

        editMI.setAction(sfa.editAction);
        editMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/pencil_16.png"))); // NOI18N
        simMenu.add(editMI);

        loadMI.setAction(sfa.loadAction);
        loadMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/folder_16.png"))); // NOI18N
        simMenu.add(loadMI);

        saveMI.setAction(sfa.saveAction);
        saveMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/save_16.png"))); // NOI18N
        simMenu.add(saveMI);

        saveAsMI.setAction(sfa.saveAsAction);
        simMenu.add(saveAsMI);

        exitMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMI.setText("Exit");
        exitMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMIActionPerformed(evt);
            }
        });
        simMenu.add(exitMI);

        mainMenu.add(simMenu);

        presetMenu.setText("Preset");
        mainMenu.add(presetMenu);

        optionsMenu.setText("Options");

        timerSettingsMI.setText("Animation");
        timerSettingsMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timerSettingsMIActionPerformed(evt);
            }
        });
        optionsMenu.add(timerSettingsMI);

        mainPlotMI.setText("Main Plot");

        boundsMI1.setText("View bounds");
        boundsMI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boundsMI1ActionPerformed(evt);
            }
        });
        mainPlotMI.add(boundsMI1);

        axesVisMI1.setSelected(true);
        axesVisMI1.setText("Axes visible");
        axesVisMI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axesVisMI1ActionPerformed(evt);
            }
        });
        mainPlotMI.add(axesVisMI1);

        axesMI1.setText("Axes settings");
        axesMI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axesMI1ActionPerformed(evt);
            }
        });
        mainPlotMI.add(axesMI1);

        gridVisMI1.setSelected(true);
        gridVisMI1.setText("Grid visible");
        gridVisMI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridVisMI1ActionPerformed(evt);
            }
        });
        mainPlotMI.add(gridVisMI1);

        gridMI1.setText("Grid settings");
        gridMI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridMI1ActionPerformed(evt);
            }
        });
        mainPlotMI.add(gridMI1);

        optionsMenu.add(mainPlotMI);

        metricPlotMI.setText("Metrics Plot");

        boundsMI2.setText("View bounds");
        boundsMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boundsMI2ActionPerformed(evt);
            }
        });
        metricPlotMI.add(boundsMI2);

        axesVisMI2.setSelected(true);
        axesVisMI2.setText("Axes visible");
        axesVisMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axesVisMI2ActionPerformed(evt);
            }
        });
        metricPlotMI.add(axesVisMI2);

        axesMI2.setText("Axes settings");
        axesMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axesMI2ActionPerformed(evt);
            }
        });
        metricPlotMI.add(axesMI2);

        gridVisMI2.setSelected(true);
        gridVisMI2.setText("Grid visible");
        gridVisMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridVisMI2ActionPerformed(evt);
            }
        });
        metricPlotMI.add(gridVisMI2);

        gridMI2.setText("Grid settings");
        gridMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridMI2ActionPerformed(evt);
            }
        });
        metricPlotMI.add(gridMI2);

        optionsMenu.add(metricPlotMI);

        mainMenu.add(optionsMenu);

        helpMenu.setText("Help");

        helpMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/help.png"))); // NOI18N
        helpMI.setText("Help Contents");
        helpMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMIActionPerformed(evt);
            }
        });
        helpMenu.add(helpMI);

        aboutMI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gsim/icons/info.png"))); // NOI18N
        aboutMI.setText("About");
        aboutMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMIActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMI);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void runAgainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAgainButtonActionPerformed
        stateChanged(null);
    }//GEN-LAST:event_runAgainButtonActionPerformed

    private void exitMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMIActionPerformed
        int result = JOptionPane.showOptionDialog(this, "Are you sure you want to exit?", "Confirm exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Yes", "No"}, "Yes");
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_exitMIActionPerformed

    private void axesMI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_axesMI1ActionPerformed
        new PropertySheetDialog(this, false, axes1).setVisible(true);
    }//GEN-LAST:event_axesMI1ActionPerformed

    private void gridMI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridMI1ActionPerformed
        new PropertySheetDialog(this, false, grid1).setVisible(true);
    }//GEN-LAST:event_gridMI1ActionPerformed

    private void axesMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_axesMI2ActionPerformed
        new PropertySheetDialog(this, false, axes2).setVisible(true);
    }//GEN-LAST:event_axesMI2ActionPerformed

    private void gridMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridMI2ActionPerformed
        new PropertySheetDialog(this, false, grid2).setVisible(true);
    }//GEN-LAST:event_gridMI2ActionPerformed

    private void axesVisMI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_axesVisMI1ActionPerformed
        axes1.setVisible(axesVisMI1.isSelected());
    }//GEN-LAST:event_axesVisMI1ActionPerformed

    private void gridVisMI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridVisMI1ActionPerformed
        grid1.setVisible(gridVisMI1.isSelected());
    }//GEN-LAST:event_gridVisMI1ActionPerformed

    private void axesVisMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_axesVisMI2ActionPerformed
        axes2.setVisible(axesVisMI2.isSelected());
    }//GEN-LAST:event_axesVisMI2ActionPerformed

    private void gridVisMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridVisMI2ActionPerformed
        grid2.setVisible(gridVisMI2.isSelected());
    }//GEN-LAST:event_gridVisMI2ActionPerformed

    private void boundsMI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boundsMI1ActionPerformed
        new PropertySheetDialog(this, false, mainPlot.getVisometry()).setVisible(true);
    }//GEN-LAST:event_boundsMI1ActionPerformed

    private void boundsMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boundsMI2ActionPerformed
        new PropertySheetDialog(this, false, logPlot.getVisometry()).setVisible(true);
    }//GEN-LAST:event_boundsMI2ActionPerformed


    private void helpMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMIActionPerformed
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        java.net.URL helpURL = SimFrame.class.getResource("HelpFile.html");
        if (helpURL != null)
            try {
                editorPane.setPage(helpURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        else
            System.err.println("Couldn't find file: TextSamplerDemoHelp.html");

        //Put the editor pane in a scroll pane.
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(480, 640));
        
        JOptionPane.showMessageDialog(this,
                editorScrollPane,
                "Pursuit Simulator Help", JOptionPane.QUESTION_MESSAGE);
    }//GEN-LAST:event_helpMIActionPerformed

    private void timerSettingsMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timerSettingsMIActionPerformed
        new PropertySheetDialog(this, false, timer).setVisible(true);
    }//GEN-LAST:event_timerSettingsMIActionPerformed

    private void aboutMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMIActionPerformed
        JOptionPane.showMessageDialog(this,
                "<html><b>Pursuit-Evasion Simulation</b><br>" +
                "Alpha Pre-Release<br>" +
                "<i>by Elisha Peterson</i><br><br>" +
                "with icons by <a href=\"http://dryicons.com\">http://dryicons.com</a>",
                "About", JOptionPane.PLAIN_MESSAGE);
}//GEN-LAST:event_aboutMIActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMI;
    private javax.swing.JMenuItem axesMI1;
    private javax.swing.JMenuItem axesMI2;
    private javax.swing.JCheckBoxMenuItem axesVisMI1;
    private javax.swing.JCheckBoxMenuItem axesVisMI2;
    private javax.swing.JMenuItem boundsMI1;
    private javax.swing.JMenuItem boundsMI2;
    private javax.swing.JScrollPane componentSP;
    private javax.swing.JMenuItem editMI;
    private javax.swing.JButton editTB;
    private javax.swing.JMenuItem exitMI;
    private javax.swing.JButton fastTB;
    private javax.swing.JMenuItem gridMI1;
    private javax.swing.JMenuItem gridMI2;
    private javax.swing.JCheckBoxMenuItem gridVisMI1;
    private javax.swing.JCheckBoxMenuItem gridVisMI2;
    private javax.swing.JMenuItem helpMI;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JMenuItem loadMI;
    private javax.swing.JButton loadTB;
    private org.bm.blaise.specto.plane.PlanePlotComponent logPlot;
    private javax.swing.JMenuBar mainMenu;
    private org.bm.blaise.specto.plane.PlanePlotComponent mainPlot;
    private javax.swing.JMenu mainPlotMI;
    private javax.swing.JSplitPane mainSP;
    private javax.swing.JMenu metricPlotMI;
    private javax.swing.JMenuItem newMI;
    private javax.swing.JButton newTB;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JButton pauseTB;
    private javax.swing.JButton playTB;
    private javax.swing.JSplitPane plotSP;
    private javax.swing.JMenu presetMenu;
    private gui.RollupPanel propPanel;
    private javax.swing.JScrollPane propPanelSP;
    private javax.swing.JButton restartTB;
    private javax.swing.JButton runAgainButton;
    private javax.swing.JMenuItem saveAsMI;
    private javax.swing.JMenuItem saveMI;
    private javax.swing.JButton saveTB;
    private javax.swing.JTabbedPane settingsTP;
    private gsim.editor.SimulationComponentList simComponentList;
    private javax.swing.JMenu simMenu;
    private javax.swing.JButton slowTB;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopTB;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JMenuItem timerSettingsMI;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

    public Simulation getSimulation() {
        return sim;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer)
            timeLabel.setText("Time = " + timer.getTime());
    }

    int i = 0;

    /** This is typically called when the simulation needs to run again, e.g. when the underlying sim changes or a parameter changes. */
    public void stateChanged(ChangeEvent e) {
        if (e != null && e.getSource() != null && e.getSource().equals(sim)) {
            initSimulation(sim);
        } else {
            sim.run();

            for (Plottable abs : mainPlot.getPlottables())
                if (abs instanceof TeamPlottableGroup)
                    ((TeamPlottableGroup) abs).updatePaths();
                else if (abs instanceof AgentPlottable)
                    ((AgentPlottable) abs).updatePath();

            for (Plottable p : logPlot.getPlottables())
                if (p instanceof MetricPlottable)
                    ((MetricPlottable) p).updatePaths();
        }
        
        mainPlot.repaint();
        logPlot.repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == simComponentList) {
            selected = (SimComponent) simComponentList.getSelectedValue();
        }
    }
}
