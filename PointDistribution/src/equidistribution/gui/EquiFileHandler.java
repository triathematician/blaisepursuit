/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package equidistribution.gui;

import equidistribution.scenario.*;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

/**
 *
 * @author elisha
 */
public class EquiFileHandler {

    JFileChooser fc;
    File openFile;
    EquiDistributionMain parent = null;

    public EquiFileHandler() {
        fc = new JFileChooser();
    }

    public EquiFileHandler(EquiDistributionMain parent) {
        fc = new JFileChooser();
        this.parent = parent;
    }

    public void status(String s) {
        if (parent != null)
            parent.setStatus(s);
        else
            System.out.println(s);
    }

    /** Shows file handler, let's user select an xml file, returns the file representing a distribution */
    EquiScenarioBean loadScenario() {
        fc.setFileFilter(xmlFilter);
        if (openFile != null) {
            fc.setCurrentDirectory(openFile);
            fc.setSelectedFile(openFile);
        }
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            openFile = fc.getSelectedFile();
            return loadScenario(openFile);
        } else {
            status("Opening file action cancelled by user.");
            return null;
        }
    }

    /** Saves scenario. Uses current file if it exists, otherwise let's user specify a new one. */
    void saveScenario(EquiController sc, boolean saveAs) {
        if (openFile == null || saveAs) {
            fc.setFileFilter(xmlFilter);
            if (openFile != null) {
                fc.setCurrentDirectory(openFile);
                fc.setSelectedFile(openFile);
            }
            if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                openFile = fc.getSelectedFile();
            } else {
                status("Save command cancelled by user.");
                return;
            }
        }
        saveScenario(sc, openFile);
    }

    /** Returns scenario in specified file. */
    EquiScenarioBean loadScenario(File file) {
        try {
            status("Loading scenario from file " + file);
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            EquiScenarioBean sc = (EquiScenarioBean) decoder.readObject();
            decoder.close();
            status("... successful!");
            return sc;
        } catch (FileNotFoundException ex) {
            status(" ERROR -- FILE NOT FOUND!");
            return null;
        }
    }

    /** Saves scenario to specified file */
    void saveScenario(EquiController sc, File file) {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
            encoder.setExceptionListener(new ExceptionListener() {
                public void exceptionThrown(Exception exception) {
                    exception.printStackTrace();
                }
            });
            encoder.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
            encoder.setPersistenceDelegate(EquiTeam.Agent.class, new DefaultPersistenceDelegate(new String[]{"x","y","capacity","behavior"}));
            EquiScenarioBean esb = new EquiScenarioBean();
            esb.useController(sc);
            encoder.writeObject(esb);
            encoder.close();
            status("Save successful.");
        } catch (FileNotFoundException ex) {
            status("Save ERROR -- FILE NOT FOUND!");
        }
    }

    /** Shows file handler, let's user select an image, returns the image or null */
    Image loadImage() {
        fc.setFileFilter(imageFilter);
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                return javax.imageio.ImageIO.read(f);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /** @return extension of a file */
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) ext = s.substring(i+1).toLowerCase();
        return ext;
    }
    
    /** A filter that shows only XML file */
    final static FileFilter xmlFilter = new FileFilter(){
        public boolean accept(File pathname) { 
            return pathname != null && (pathname.isDirectory()
                    || "xml".equals(getExtension(pathname))); }
        public String getDescription() { return "XML files"; }
    };

    /** A filter that shows image files */
    final static FileFilter imageFilter = new FileFilter(){
        public boolean accept(File pathname) {
            return pathname != null && (pathname.isDirectory()
                    || "jpg".equals(getExtension(pathname))
                    || "jpeg".equals(getExtension(pathname))
                    || "png".equals(getExtension(pathname))
                    ); }
        public String getDescription() { return "Image files (.jpg, .jpeg, .png)"; }
    };
}
