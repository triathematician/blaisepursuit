/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.customizers;

import data.propertysheet.PropertySheetDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sim.component.agent.AgentParameters;

/**
 *
 * @author ae3263
 */
public class AgentEditor extends JPanel implements ActionListener {

    AgentParameters par;
    private JTextField nameField;
//    private JButton colorButton;
    Collection<Object> objects;

    public static AgentParameters showDialog(Frame parent, Collection<Object> objectTable, AgentParameters agent) {
        createDialog(parent, objectTable, agent).setVisible(true);
        // reaches here after box is closed
        return agent;
    }

    public static JDialog createDialog(Frame parent, Collection<Object> objectTable, AgentParameters agent) {
        return new PropertySheetDialog(parent, true, agent);

//        final JDialog result = new JDialog(parent, "Team Editor", true);
//        JPanel panel = new JPanel(new BorderLayout());
//        final AgentEditor editor = new AgentEditor(objectTable, agent);
//        JButton okButton = new JButton("OK");
//        okButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
//            editor.updatePack();
//            result.setVisible(false);
//        }});
//        panel.add(editor, BorderLayout.CENTER);
//        panel.add(okButton, BorderLayout.SOUTH);
//        panel.validate();
//        result.setContentPane(panel);
//        result.pack();
//        return result;
    }

    public AgentEditor(Collection<Object> objects) {
        this(objects, new AgentParameters(""));
    }

    public AgentEditor(Collection<Object> objects, AgentParameters par) {
        this.objects = objects;
        this.par = par;
//        kcmEnemy = new KernelComboModel(SimulationTeam.class, this.objects);
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout());
        nameField = new JTextField(par.getName());
          nameField.setToolTipText("Agent name");
          nameField.addActionListener(this);
          add(nameField);
//        colorButton = new JButton("color");
//          colorButton.setBackground(team.getColor());
//          colorButton.setToolTipText("Color of the pack");
//          colorButton.addActionListener(this);
//          add(colorButton);
//        enemyBox = new JComboBox(kcmEnemy);
//          enemyBox.setToolTipText("Enemy of pack");
////          enemyBox.setSelectedItem(team.getEnemy());
//          enemyBox.addActionListener(this);
//          add(enemyBox);
        validate();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nameField) {
            par.setName(nameField.getText());
//        } else if (e.getSource() == colorButton) {
//            team.setColor(colorButton.getBackground());
        } else {
            assert false;
            System.out.println("unknown source (AgentEditor)");
        }
    }

    private void updateAgent() {
        par.setName(nameField.getText());
//        team.setColor(colorButton.getBackground());
    }

}
