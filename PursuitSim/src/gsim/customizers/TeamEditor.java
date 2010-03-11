/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.customizers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sim.component.team.Team;
import sim.component.team.TeamParameters;

/**
 *
 * @author ae3263
 */
public class TeamEditor extends JPanel implements ActionListener {

    //
    // STATIC METHODS
    //

    public static TeamParameters showDialog(Frame parent, Collection<Object> objectTable, TeamParameters par) {
        createDialog(parent, objectTable, par).setVisible(true);
        // reaches here after box is closed
        return par;
    }

    public static JDialog createDialog(Frame parent, Collection<Object> objectTable, TeamParameters par) {
        final JDialog result = new JDialog(parent, "Team Editor", true);
        JPanel panel = new JPanel(new BorderLayout());
        final TeamEditor editor = new TeamEditor(objectTable, par);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
            editor.updateTeam();
            result.setVisible(false);
        }});
        panel.add(editor, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);
        panel.validate();
        result.setContentPane(panel);
        result.pack();
        return result;
    }

    //
    // PROPERTIES
    //

    TeamParameters par;
    private JTextField nameField;
    private JButton colorButton;
    Collection<Object> objects;


    public TeamEditor(Collection<Object> objects) {
        this(objects, new TeamParameters(""));
    }

    public TeamEditor(Collection<Object> objects, TeamParameters par) {
        this.objects = objects;
        this.par = par;
//        kcmEnemy = new KernelComboModel(SimulationTeam.class, this.objects);
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout());
        nameField = new JTextField(par.getName());
          nameField.setToolTipText("Team name");
          nameField.addActionListener(this);
          add(nameField);
        colorButton = new JButton("color");
          colorButton.setBackground(par.getColor());
          colorButton.setToolTipText("Color of the pack");
          colorButton.addActionListener(this);
          add(colorButton);
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
        } else if (e.getSource() == colorButton) {
            par.setColor(colorButton.getBackground());
        } else {
            assert false;
            System.out.println("unknown source (TeamEditor)");
        }
    }

    private void updateTeam() {
        par.setName(nameField.getText());
        par.setColor(colorButton.getBackground());
    }

}
