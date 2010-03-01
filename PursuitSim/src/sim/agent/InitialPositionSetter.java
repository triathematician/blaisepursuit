/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.agent;

import java.awt.geom.Point2D;

/**
 *
 * @author ae3263
 */
public interface InitialPositionSetter {

    public Point2D.Double getInitialPosition();
    public void setInitialPosition(Point2D.Double p);

}
