/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package equidistribution.gui;

import equidistribution.scenario.EquiController;
import equidistribution.scenario.behavior.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;

/**
 *
 * @author elisha
 */
public class AlgorithmComboBox extends JComboBox
        implements PropertyChangeListener {
    
    EquiController sc;

    boolean adjusting = false;

    public AlgorithmComboBox() {
        super(Choice.values());
        setToolTipText("Selecting a behavior here will change the behavior of ALL agents to that selected.");
        addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                adjusting = true;
                sc.setBehavior(getSelectedBehavior());
                adjusting = false;
            }
        });
    }

    public EquiController getController() {
        return sc;
    }

    public void setController(EquiController sc) {
        if (this.sc != null)
            sc.removePropertyChangeListener(this);
        this.sc = sc;
        if (sc != null) {
            sc.addPropertyChangeListener(this);
            AgentBehavior b = sc.getAgent(0).getBehavior();
            setSelectedItem(getChoice(b));
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (!adjusting && evt.getPropertyName().equalsIgnoreCase("behavior")) {
            AgentBehavior b = sc.getAgent(0).getBehavior();
            setSelectedItem(getChoice(b));
        }
    }

    static enum Choice {
        NONE ("No movement"),
        RANDOM ("Random movement"),
        SMALLEST ("Move away from smallest area"),
        LARGEST ("Move toward largest area"),
        WEIGHTED ("Weighted movement by difference in areas"),
        COPY ("Copy neighbor movements");

        String description;
        Choice(String description) { this.description = description; }
        @Override public String toString() { return description; }
    }

    Choice getChoice(AgentBehavior b) {
        if (b == null)
            return Choice.NONE;
        else if (b instanceof RandomMovement)
            return Choice.RANDOM;
        else if (b instanceof TowardLargestArea)
            return Choice.LARGEST;
        else if (b instanceof FromSmallestArea)
            return Choice.SMALLEST;
        else if (b instanceof WeightedAreaMovement)
            return Choice.WEIGHTED;
        else if (b instanceof CopyMovementsCloseToTargetArea)
            return Choice.COPY;
        return null;
    }

    AgentBehavior getBehavior(Choice c) {
        switch (c) {
            case NONE: return null;
            case RANDOM: return RandomMovement.INSTANCE;
            case LARGEST: return TowardLargestArea.INSTANCE;
            case SMALLEST: return FromSmallestArea.INSTANCE;
            case WEIGHTED: return new WeightedAreaMovement();
            case COPY: return new CopyMovementsCloseToTargetArea();
        }
        return null;
    }

    public AgentBehavior getSelectedBehavior() {
        return getBehavior((Choice)getSelectedItem());
    }

}
