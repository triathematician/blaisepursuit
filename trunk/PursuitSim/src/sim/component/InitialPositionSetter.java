/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.component;

import java.awt.geom.Point2D;

/**
 * This interface is used for objects that have initial positions that can be set externally.
 * 
 * @author ae3263
 */
public interface InitialPositionSetter {

    public Point2D.Double getInitialPosition();
    public void setInitialPosition(Point2D.Double p);

}
