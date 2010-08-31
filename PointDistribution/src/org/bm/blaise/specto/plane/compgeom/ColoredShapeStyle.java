/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.HashMap;
import primitive.style.ShapeStyle;

/**
 *
 * @author ae3263
 */
public class ColoredShapeStyle extends ShapeStyle {

    static HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();

    public static Color getColor(int i) {
        if (!colorMap.containsKey(i))
            colorMap.put(i, new Color((17+19*i)%256, (23+53*i)%256, (217*i)%256));
        return colorMap.get(i);
    }

    @Override
    public void drawArray(Graphics2D canvas, Shape[] shapes) {
        int i = 0;
        if (stroke != null && strokeColor != null) {
            canvas.setStroke(stroke);
            for (Shape sh: shapes) {
                canvas.setColor(getColor(i));
                canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                canvas.fill(sh);
                canvas.setComposite(AlphaComposite.SrcOver);
                canvas.setStroke(stroke);
                canvas.setColor(strokeColor);
                canvas.draw(sh);
                i++;
            }
        } else if (fillColor != null) {
            // only fill shapes
            canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            for (Shape sh: shapes) {
                canvas.setColor(getColor(i));
                canvas.fill(sh);
                i++;
            }
            canvas.setComposite(AlphaComposite.SrcOver);
        }
    }



}
