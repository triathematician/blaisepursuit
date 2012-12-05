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
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sim.Simulation;
import sim.component.team.Team;
import sim.metrics.Capture;

/**
 *
 * @author ae3263
 */
public class CaptureEditor extends MPropertyEditorSupport
        implements ActionListener, ChangeListener {

    /** Button to display the victory condition. */
    JButton button;
    /** Panel for layout */
    JPanel panel;
    
    JComboBox teamBox;
    KernelComboModel teamModel;
    JSpinner valueSpinner;

    public CaptureEditor() {
        this(Capture.NONE);
    }

    public CaptureEditor(Capture cap) {
        setValue(cap);
        initComponents();
    }

    private void initComponents() {
        teamModel = Simulation.ACTIVE_SIM == null
                ? new KernelComboModel(Team.class)
                : new KernelComboModel(Team.class, Simulation.ACTIVE_SIM.getComponentsByType(Team.class));
        
        panel = new JPanel(new FlowLayout());
        teamBox = new JComboBox(teamModel);
          teamBox.setToolTipText("Select team to capture");
          teamBox.setPreferredSize(new Dimension(100, 24));
          teamBox.setSelectedItem(null);
          teamBox.addActionListener(this);
        valueSpinner = new JSpinner(
            new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.01));
          valueSpinner.setPreferredSize(new Dimension(50, valueSpinner.getPreferredSize().height));
          valueSpinner.setToolTipText("Threshold for capture");
          valueSpinner.setPreferredSize(new Dimension(70, 24));
          valueSpinner.addChangeListener(this);
        panel.add(teamBox);
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
        Capture cap = (Capture) getValue();
        if (button != null) {
            button.setText(cap.toString());
            if (dialog != null)
                dialog.setTitle("Edit a Capture Condition [" + ( cap.getTeam1() == null ? "" : cap.getTeam1() ) + "]");
            teamBox.setSelectedItem(cap.getTeam2());
            valueSpinner.setValue(cap.getThreshold());
        }
    }

    JDialog dialog;
    JButton okButton, cancelButton;

    void initDialog() {
        dialog = new JDialog((Frame) null, "Edit a Capture Condition", true);
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
        else if (e.getSource().equals(teamBox))
            ((Capture)getNewValue()).setTeam2((Team) teamBox.getSelectedItem());
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(valueSpinner))
            ((Capture)getNewValue()).setThreshold((Double) valueSpinner.getValue());
    }
}
