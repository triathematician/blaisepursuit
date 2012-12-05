/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MetricPanel.java
 *
 * Created on Aug 20, 2010, 7:32:30 AM
 */

package equidistribution.gui;

import equidistribution.scenario.EquiMetrics;
import equidistribution.scenario.EquiController;
import simutils.SimulationEvent;
import simutils.SimulationEventListener;

/**
 *
 * @author elisha
 */
public class MetricPanel extends javax.swing.JPanel
        implements SimulationEventListener {

    EquiController sc;

    /** Creates new form MetricPanel */
    public MetricPanel() {
        initComponents();
        updateMetrics();
    }
    
    public EquiController getController() {
        return sc;
    }

    public void setController(EquiController sc) {
        if (this.sc != null)
            sc.removeSimulationEventListener(this);
        this.sc = sc;
        if (sc != null) {
            sc.addSimulationEventListener(this);
            updateMetrics();
        }
    }

    private void updateMetrics() {
        if (sc == null) {
            totalL.setText("");
            capL.setText("");
            meanL.setText("");
            maxDevL.setText("");
            meanDevL.setText("");
            meanSqrDevL.setText("");
            scoreL.setText("");
        } else {
            Double total = sc.getTotalWeightedArea();
            if (total != null)
                totalL.setText(varFormatDouble(total));
            Double cap = sc.getTotalAgentCapacity();
            if (cap != null)
                capL.setText(varFormatDouble(cap));

            Double mean = sc.getTargetArea();
            if (mean != null) {
                meanL.setText(varFormatDouble(mean));

                double[] metrics = sc.value(EquiMetrics.L_METRICS_ADJUSTED);
                if (metrics != null) {
                    maxDevL.setText(varFormatDouble(metrics[0]));
                    meanDevL.setText(varFormatDouble(metrics[1]));
                    meanSqrDevL.setText(varFormatDouble(metrics[2]));
                }

                Double score = sc.value(EquiMetrics.TEAM_DEVIATION_SCORE);
                if (score != null)
                    scoreL.setText(varFormatDouble(score));
            }
        }
    }

    private String varFormatDouble(double d) {
        if (Math.abs(d) < .001)
            return String.format("%.3e", d);
        else
            return String.format("%.3f", d);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        totalL = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        capL = new javax.swing.JLabel();
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

        setBackground(new java.awt.Color(204, 204, 255));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        setLayout(new java.awt.GridLayout(7, 2, 4, 4));

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total Area =");
        jLabel10.setToolTipText("Average of Areas");
        add(jLabel10);

        totalL.setBackground(new java.awt.Color(0, 0, 0));
        totalL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        totalL.setForeground(new java.awt.Color(255, 255, 255));
        totalL.setText("0.00");
        totalL.setToolTipText("Average of Areas");
        totalL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        totalL.setOpaque(true);
        add(totalL);

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Team Capacity =");
        jLabel11.setToolTipText("Average of Areas");
        add(jLabel11);

        capL.setBackground(new java.awt.Color(0, 0, 0));
        capL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        capL.setForeground(new java.awt.Color(255, 255, 255));
        capL.setText("0.00");
        capL.setToolTipText("Average of Areas");
        capL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        capL.setOpaque(true);
        add(capL);

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Target Area =");
        jLabel5.setToolTipText("Average of Areas");
        add(jLabel5);

        meanL.setBackground(new java.awt.Color(0, 0, 0));
        meanL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        meanL.setForeground(new java.awt.Color(255, 255, 255));
        meanL.setText("0.00");
        meanL.setToolTipText("Average of Areas");
        meanL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanL.setOpaque(true);
        add(meanL);

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Max Dev =");
        jLabel6.setToolTipText("Max Deviation  of Area from Average");
        add(jLabel6);

        maxDevL.setBackground(new java.awt.Color(0, 0, 0));
        maxDevL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        maxDevL.setForeground(new java.awt.Color(255, 255, 255));
        maxDevL.setText("0.00");
        maxDevL.setToolTipText("Max Deviation  of Area from Average");
        maxDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        maxDevL.setOpaque(true);
        add(maxDevL);

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Mean Dev =");
        jLabel7.setToolTipText("Average Deviation of Area from Average");
        add(jLabel7);

        meanDevL.setBackground(new java.awt.Color(0, 0, 0));
        meanDevL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        meanDevL.setForeground(new java.awt.Color(255, 255, 255));
        meanDevL.setText("0.00");
        meanDevL.setToolTipText("Average Deviation of Area from Average");
        meanDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanDevL.setOpaque(true);
        add(meanDevL);

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mean Sqr Dev =");
        jLabel8.setToolTipText("Mean Squared Deviation of Area from Average");
        add(jLabel8);

        meanSqrDevL.setBackground(new java.awt.Color(0, 0, 0));
        meanSqrDevL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        meanSqrDevL.setForeground(new java.awt.Color(255, 255, 255));
        meanSqrDevL.setText("0.00");
        meanSqrDevL.setToolTipText("Mean Squared Deviation of Area from Average");
        meanSqrDevL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        meanSqrDevL.setOpaque(true);
        add(meanSqrDevL);

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("SCORE =");
        jLabel9.setToolTipText("Mean Squared Deviation of Area from Average");
        add(jLabel9);

        scoreL.setBackground(new java.awt.Color(0, 0, 0));
        scoreL.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        scoreL.setForeground(new java.awt.Color(255, 255, 255));
        scoreL.setText("0.00");
        scoreL.setToolTipText("Mean Squared Deviation of Area from Average");
        scoreL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scoreL.setOpaque(true);
        add(scoreL);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel capL;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel maxDevL;
    private javax.swing.JLabel meanDevL;
    private javax.swing.JLabel meanL;
    private javax.swing.JLabel meanSqrDevL;
    private javax.swing.JLabel scoreL;
    private javax.swing.JLabel totalL;
    // End of variables declaration//GEN-END:variables

    public void handleResetEvent(SimulationEvent e) { updateMetrics(); }
    public void handleIterationEvent(SimulationEvent e) { updateMetrics(); }
    public void handleGenericEvent(SimulationEvent e) {}

}
