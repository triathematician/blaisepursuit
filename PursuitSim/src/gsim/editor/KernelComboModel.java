/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.editor;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * A combo box model based upon which objects show up in an underlying table.
 * @author ae3263
 */
public class KernelComboModel extends AbstractListModel implements ComboBoxModel {

    /** Class type to look for */
    Class cls;
    /** Objects to look through */
    Collection<Object> objects;
    /** Objects found of given class type */
    transient ArrayList<Object> items;
    /** Selected item */
    transient Object selectedItem;

    public KernelComboModel(Class cls) {
        this.cls = cls;
        objects = new ArrayList<Object>();
    }

    public KernelComboModel(Class cls, Collection<Object> objects) {
        this.cls = cls;
        this.objects = objects;
        findObjects();
    }

    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
        findObjects();
    }

    private void findObjects() {
        items = new ArrayList<Object>();
        items.add(null);
        for (Object o : objects)
            if (cls.isAssignableFrom(o.getClass()))
                items.add(o);
    }

    public void setSelectedItem(Object anItem) {
        if (items.contains(anItem))
            selectedItem = anItem;
        else
            System.out.println("setSelectedItem: Item not found; " + anItem);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public int getSize() {
        return items.size();
    }

    public Object getElementAt(int index) {
        return items.get(index);
    }
}
