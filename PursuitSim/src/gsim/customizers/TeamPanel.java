/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.customizers;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 *
 * @author ae3263
 */
public class TeamPanel extends JPanel {
    public TeamPanel() {
        super(new FlowLayout());
        setBackground(Color.BLACK);
        setTransferHandler(new TeamImportHandler());
    }
    public void addAgent(String s) {
        add(new JButton(s));
    }

    /** Set up transfer handler to support string data flavors. */
    class TeamImportHandler extends TransferHandler {
        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            for (DataFlavor df : transferFlavors) {
                if (df.equals(DataFlavor.stringFlavor))
                    return true;
            }
            return false;
        }
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            String s = null;
            try {
                s = (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
            } catch (IOException ex) {
            }
            if (s.equals("Agent")) {
                addAgent(s);
            }
            return true;
        }
    }
}
