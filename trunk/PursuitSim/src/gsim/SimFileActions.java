/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim;

import gsim.editor.EditorToolFrame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import sim.Simulation;

/**
 * This class contains all the actions for saving/reloading simulation files.
 * 
 * @author ae3263
 */
public class SimFileActions {

    SimFrame frame;
    final JFileChooser fc;
    File openFile = null;

    public SimFileActions(SimFrame frame) {
        this.frame = frame;
        fc = new JFileChooser();
    }

    void enableSaveActions() {
        editAction.setEnabled(true);
        saveAction.setEnabled(true);
        saveAsAction.setEnabled(true);
    }

    //
    // FILE HANDLING
    //


    /** @return extension of a file */
    static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) ext = s.substring(i+1).toLowerCase();
        return ext;
    }
    /** A filter that shows only XML file */
    final static FileFilter xmlFilter = new FileFilter(){
        public boolean accept(File pathname) {
            return pathname != null && (pathname.isDirectory() || "xml".equals(getExtension(pathname)));
        }
        public String getDescription() { return "XML files"; }
    };

    private void load(File file) {
        System.out.print("Opening: " + file.getName() + "...");
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            frame.initSimulation((Simulation) decoder.readObject());
            decoder.close();
            System.out.println(" successful.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void save(File file) {
        System.out.print("Saving: " + file.getName() + "...");
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
            encoder.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
            encoder.writeObject((Simulation) frame.getSimulation());
            encoder.close();
            System.out.println(" successful.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //
    // ACTIONS
    //

    public Action newAction = new AbstractAction("New Simulation...") {
        {
            putValue(SHORT_DESCRIPTION, "Create a new simulation.");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            frame.initSimulation(new Simulation());
        }
    };

    public Action loadAction = new AbstractAction("Open Simulation") {
        {
            putValue(SHORT_DESCRIPTION, "Load a simulation from a file");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            fc.setFileFilter(xmlFilter);
            if (openFile != null) {
                fc.setCurrentDirectory(openFile);
                fc.setSelectedFile(openFile);
            }
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                openFile = fc.getSelectedFile();
                load(openFile);
            } else {
                System.out.println(" open command cancelled by user.");
            }
        }
    };

    public Action editAction = new AbstractAction("Edit Simulation") {
        {
            putValue(SHORT_DESCRIPTION, "Edit the simulation settings");
            putValue(MNEMONIC_KEY, KeyEvent.VK_E);
            setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            new EditorToolFrame(frame, frame.getSimulation(), false).setVisible(true);
//            Simulation newSim = CreateASim.showDialog(SimFrame.this, sim);
//            if (newSim != null)
//                initSimulation(newSim);
        }
    };

    public Action saveAction = new AbstractAction("Save Simulation") {
        {
            putValue(SHORT_DESCRIPTION, "Save the simulation to the current file");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            if (openFile != null)
                save(openFile);
            else
                saveAsAction.actionPerformed(e);
        }
    };

    public Action saveAsAction = new AbstractAction("Save Simulation As...") {
        {
            putValue(SHORT_DESCRIPTION, "Save the simulation to a new file");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            fc.setFileFilter(xmlFilter);
            if (openFile != null) {
                fc.setCurrentDirectory(openFile);
                fc.setSelectedFile(openFile);
            }
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                openFile = fc.getSelectedFile();
                save(openFile);
            } else
                System.out.println(" save command cancelled by user.");
        }
    };

}
