/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsim.editor;

import gsim.editor.EditorToolPanel.ToolList;
import gsim.editor.SimActions.GenericSetAction;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import sim.SimComponent;
import sim.SimComposite;
import sim.component.agent.AgentParameters;
import sim.component.VisiblePlayer;
import sim.comms.Sensor;
import sim.component.agent.Agent;
import sim.tasks.Task;
import sim.tasks.Tasker;

/**
 *
 * @author ae3263
 */
public class SimComponentTransferHandler extends TransferHandler {

    public static DataFlavor actionFlavor = null;
    public static DataFlavor compFlavor = null;

    static {
        try {
            actionFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=javax.swing.Action");
            compFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=sim.SimComponent");
        } catch (ClassNotFoundException ex) {
        }
    }

    public static final SimComponentTransferHandler INSTANCE = new SimComponentTransferHandler();

    //
    // EXPORT METHODS
    //
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof ToolList) {
            return new ActionTransferable(((ToolList) c).getSelectedAction());
        } else if (c instanceof SimulationComponentList) {
            SimComponent selected = (SimComponent) ((SimulationComponentList) c).getSelectedValue();
            return new SimulationComponentTransferable(selected);
        } else {
            return null;
        }
    }

    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
        // *** - not supported
        //        System.out.println("SimComponentTransferHandler.exportToClipboard");
        //        super.exportToClipboard(comp, clip, action);
    }

    @Override
    public int getSourceActions(JComponent c) {
        //        System.out.println("SimComponentTransferHandler.getSourceActions");
        return COPY_OR_MOVE;
    }

    //
    // IMPORT METHODS
    //
    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        if (comp instanceof SimulationComponentList) {
            SimComponent selected = (SimComponent) ((SimulationComponentList) comp).getSelectedValue();
            for (DataFlavor df : transferFlavors) {
                if (df.match(actionFlavor)
                        || df.match(compFlavor) && (selected instanceof VisiblePlayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        if (!(comp instanceof SimulationComponentList)) {
            return false;
        }

        SimComponent receiver = (SimComponent) ((SimulationComponentList) comp).getSelectedValue();

        if (t.isDataFlavorSupported(actionFlavor)) {
            try {
                importActionData(receiver, (GenericSetAction) t.getTransferData(actionFlavor));
            } catch (Exception ex) {
                return false;
            }
        } else if (t.isDataFlavorSupported(compFlavor)) {
            try {
                importComponentData(receiver, (SimComponent) t.getTransferData(compFlavor));
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }

        ((SimulationComponentList) comp).updateModel();
        return true;
    }

    public void importActionData(SimComponent rec, GenericSetAction ac) {
        ac.receiver = rec;
        ac.actionPerformed(null);
        ac.receiver = null;
    }

    public void importComponentData(SimComponent rec, SimComponent obj) {
        if (rec instanceof VisiblePlayer && obj instanceof Agent) {
            // create a follow behavior
            Object[] options = {"Yes, seek", "Yes, flee", "No"};
            int n = JOptionPane.showOptionDialog(
                    null,
                    "Should " + obj + " follow " + rec + "?",
                    "Confirm Follow Behavior",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
            AgentParameters par = ((Agent) obj).getParameters();
            par.setTasker(new Tasker[]{
                new Tasker.Follow((VisiblePlayer) rec, n == JOptionPane.YES_OPTION ? Task.Type.SEEK : Task.Type.FLEE)
            });
            if (par.getSensor() == Sensor.NO_SENSOR) {
                par.setSensor(Sensor.GLOBAL_SENSOR);
            }
        } else if (rec instanceof SimComposite && rec != obj.getParent()) {
            // move the component to this location
            obj.getParent().removeComponent(obj);
            ((SimComposite) rec).addComponent(obj);
        }
    }


    

    /** Transfers an action */
    public static class ActionTransferable
            implements Transferable {

        Action ac;
        DataFlavor[] flavors;


        public ActionTransferable(Action ac) {
            this.ac = ac;
            flavors = new DataFlavor[]{actionFlavor, DataFlavor.stringFlavor};
        }

        @Override
        public String toString() {
            return "ActionTransferable[" + ac + "]";
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.match(DataFlavor.stringFlavor) || flavor.match(actionFlavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.match(actionFlavor)) {
                return ac;
            } else if (flavor.match(DataFlavor.stringFlavor)) {
                return ac.getValue(Action.NAME);
            } else {
                return null;
            }
        }
    }

    /** Transfers a component */
    static class SimulationComponentTransferable
            implements Transferable {

        SimComponent comp;
        DataFlavor[] flavors;

        public SimulationComponentTransferable(SimComponent comp) {
            this.comp = comp;
            flavors = new DataFlavor[]{compFlavor, DataFlavor.stringFlavor};
        }

        @Override
        public String toString() {
            return "ComponentTransferable[" + comp + "]";
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.match(DataFlavor.stringFlavor) || flavor.match(compFlavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.match(compFlavor)) {
                return comp;
            } else if (flavor.match(DataFlavor.stringFlavor)) {
                return comp.toString();
            } else {
                return null;
            }
        }
    }
}
