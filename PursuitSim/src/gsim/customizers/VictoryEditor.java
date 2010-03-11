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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sim.Simulation;
import sim.component.team.Team;
import sim.metrics.ComparisonType;
import sim.metrics.TeamMetrics;
import sim.metrics.VictoryCondition;

/**
 *
 * @author ae3263
 */
public class VictoryEditor extends MPropertyEditorSupport
        implements ActionListener, ChangeListener {

    /** Button to display the victory condition. */
    JButton button;
    /** Panel for layout */
    JPanel panel;
    
    JComboBox metricBox;
    JComboBox teamBox;
    KernelComboModel teamModel;
    JComboBox compareBox;
    JSpinner valueSpinner;

    public VictoryEditor() {
        this(VictoryCondition.NONE);
    }

    public VictoryEditor(VictoryCondition vic) {
        setValue(vic);
        initComponents();
    }

    private void initComponents() {
        teamModel = Simulation.ACTIVE_SIM == null
                ? new KernelComboModel(Team.class)
                : new KernelComboModel(Team.class, Simulation.ACTIVE_SIM.getComponentsByType(Team.class));
        
        panel = new JPanel(new FlowLayout());
        metricBox = new JComboBox(TeamMetrics.values());
          metricBox.setToolTipText("Choice of metric");
          metricBox.addActionListener(this);
        teamBox = new JComboBox(teamModel);
          teamBox.setToolTipText("Select team for comparison");
          teamBox.setPreferredSize(new Dimension(100, 24));
          teamBox.setSelectedItem(null);
          teamBox.addActionListener(this);
        compareBox = new JComboBox(ComparisonType.values());
          compareBox.setToolTipText("Type of comparison");
          compareBox.addActionListener(this);
        valueSpinner = new JSpinner(
            new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.01));
          valueSpinner.setPreferredSize(new Dimension(50, valueSpinner.getPreferredSize().height));
          valueSpinner.setToolTipText("Threshold for victory");
          valueSpinner.setPreferredSize(new Dimension(70, 24));
          valueSpinner.addChangeListener(this);
        panel.add(metricBox);
        panel.add(teamBox);
        panel.add(compareBox);
        panel.add(valueSpinner);

        panel.validate();

        // set up listener to track for customization... this happens when the button is pressed
        button = new JButton(getValue() == null ? "" : getValue().toString());
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
        VictoryCondition vic = (VictoryCondition) getValue();
        if (button != null) {
            button.setText(vic.toString());
            if (dialog != null)
                dialog.setTitle("Edit a Victory Condition [" + ( vic.getTeam1() == null ? "" : vic.getTeam1() ) + "]");
            metricBox.setSelectedItem(vic.getMetric());
            teamBox.setSelectedItem(vic.getTeam2());
            compareBox.setSelectedItem(vic.getType());
            valueSpinner.setValue(vic.getThreshold());
        }
    }

    JDialog dialog;
    JButton okButton, cancelButton;

    void initDialog() {
        dialog = new JDialog((Frame) null, "Edit a Victory Condition", true);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);

        okButton = new JButton("Okay");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);
        buttons.add(cancelButton);
        dialog.add(buttons, BorderLayout.SOUTH);

        dialog.pack();
    }



    public void actionPerformed(ActionEvent e) {
        // lazy-load dialog
        if (dialog == null)
            initDialog();

        // ok, cancel, show actions
        if (e.getSource().equals(button)) {
            dialog.setVisible(true);
        } else if (e.getSource().equals(okButton)) {
            dialog.setVisible(false);
            stopEditAction();
        } else if (e.getSource().equals(cancelButton)) {
            dialog.setVisible(false);
            cancelEditAction();
        }

        // component-wise actions
        else if (e.getSource().equals(metricBox))
            ((VictoryCondition)getNewValue()).setMetric((TeamMetrics) metricBox.getSelectedItem());
        else if (e.getSource().equals(teamBox))
            ((VictoryCondition)getNewValue()).setTeam2((Team) teamBox.getSelectedItem());
        else if (e.getSource().equals(compareBox))
            ((VictoryCondition)getNewValue()).setType((ComparisonType) compareBox.getSelectedItem());
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(valueSpinner))
            ((VictoryCondition)getNewValue()).setThreshold((Double) valueSpinner.getValue());
    }
}
