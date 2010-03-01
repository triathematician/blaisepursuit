/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.customizers;

import data.propertysheet.editor.MPropertyEditorSupport;
import gsim.editor.KernelComboModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import sim.agent.SimulationTeam;
import sim.metrics.ComparisonType;
import sim.metrics.TeamMetrics;
import sim.metrics.VictoryCondition;

/**
 *
 * @author ae3263
 */
public class VictoryEditor extends MPropertyEditorSupport implements ActionListener {

    VictoryCondition vic;

    /** Button to display the victory condition. */
    JButton button;
    /** Panel for layout */
    JPanel panel;
    
    JLabel ownerLabel;
    JComboBox metricBox;
    JComboBox teamBox;
    KernelComboModel teamModel;
    JComboBox compareBox;
    JSpinner valueSpinner;

    public VictoryEditor() {
        this(VictoryCondition.NONE);
    }

    public VictoryEditor(VictoryCondition vic) {
        this.vic = vic;
        teamModel = new KernelComboModel(SimulationTeam.class);
        initComponents();
    }

    private void initComponents() {
        panel = new JPanel(new FlowLayout());
        ownerLabel = new JLabel(vic.getTeam1()==null?"NONE":vic.getTeam1().toString());
          ownerLabel.setToolTipText("Team looking for victory");
          ownerLabel.setPreferredSize(new Dimension(80, 24));
        metricBox = new JComboBox(TeamMetrics.values());
          metricBox.setToolTipText("Choice of metric");
        teamBox = new JComboBox();
          teamBox.setToolTipText("Select team for comparison");
          teamBox.setPreferredSize(new Dimension(100, 24));
        compareBox = new JComboBox(ComparisonType.values());
          compareBox.setToolTipText("Type of comparison");
        valueSpinner = new JSpinner(
            new SpinnerNumberModel((Number) vic.getThreshold(), -Double.MAX_VALUE, Double.MAX_VALUE, 0.01));
          valueSpinner.setPreferredSize(new Dimension(50, valueSpinner.getPreferredSize().height));
          valueSpinner.setToolTipText("Threshold for victory");
          valueSpinner.setPreferredSize(new Dimension(70, 24));
        panel.add(ownerLabel);
        panel.add(metricBox);
        panel.add(teamBox);
        panel.add(compareBox);
        panel.add(valueSpinner);

        panel.validate();

        // set up listener to track for customization... this happens when the button is pressed
        button = new JButton(vic.toString());
        button.addActionListener(this);
        
        initEditorValue();
    }

    public void initObjects(Collection<Object> objects) {
        teamModel.setObjects(objects);
    }

    @Override
    public Component getCustomEditor() {
        return button;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    protected void initEditorValue() {
        button.setText(vic.toString());
        ownerLabel.setText(vic.getTeam1()==null?"NONE":vic.getTeam1().toString());
        metricBox.setSelectedItem(vic.getMetric());
        teamBox.setSelectedItem(vic.getTeam2()==null?"NONE":vic.getTeam2().toString());
        compareBox.setSelectedItem(vic.getType());
        valueSpinner.setValue(vic.getThreshold());
    }

    public void actionPerformed(ActionEvent e) {
        // called when the button is pressed... show dialog with the custom editor
        JDialog dialog = new JDialog((Frame) null, "Edit a Victory Condition", true);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        JPanel p = new JPanel(new FlowLayout());
        p.add(new JButton("OK"));
        p.add(new JButton("Cancel"));
        dialog.add(p, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
    }
}
