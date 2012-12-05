/*
 * RegionPlottableBean.java
 * Created Aug 30, 2010
 */

package equidistribution.gui;

import java.awt.geom.Point2D;
import primitive.style.PathStyleShape;
import primitive.style.PointLabeledStyle;
import primitive.style.ShapeStyle;
import visometry.plottable.VShape;

/**
 * Provides main editing features for <code>RegionPlottable</code>.
 * @author Elisha Peterson
 */
public class RegionPlottableBean {

    RegionPlottable p;

    public RegionPlottableBean(RegionPlottable p) {
        this.p = p;
    }

    public boolean isAgentsVisible() { return p.agents.isVisible(); }
    public void setAgentsVisible(boolean vis) { p.agents.setVisible(vis); }
    public PointLabeledStyle getAgentStyle() { return p.agents.getPointStyle(); }
    public void setAgentStyle(PointLabeledStyle style) { p.agents.setPointStyle(style); }

    public boolean isBorderVisible() { return p.borderShape.isVisible(); }
    public void setBorderVisible(boolean vis) { p.borderShape.setVisible(vis); }
    public ShapeStyle getBorderStyle() { return p.borderShape.getShapeStyle(); }
    public void setBorderStyle(ShapeStyle style) { p.borderShape.setShapeStyle(style); }

    public boolean isNetworkVisible() { return p.network.isVisible(); }
    public void setNetworkVisible(boolean vis) { p.network.setVisible(vis); }
    public PathStyleShape getNetworkStyle() { return p.network.getStyle(); }
    public void setNetworkStyle(PathStyleShape style) { p.network.setStyle(style); }

    public boolean isPartitionVisible() { return p.partition.isVisible(); }
    public void setPartitionVisible(boolean vis) { p.partition.setVisible(vis); }
    public ShapeStyle getPartitionStyle() { return p.partition.getStyle(); }
    public void setPartitionStyle(ShapeStyle style) { p.partition.setStyle(style); }

    public boolean isZonesVisible() { return p.zoneShapes.size() == 0 ? true : p.zoneShapes.get(0).isVisible(); }
    public void setZonesVisible(boolean vis) { for (VShape<Point2D.Double> sh : p.zoneShapes) sh.setVisible(vis); }
    public ShapeStyle getZonesStyle() { return p.zoneShapes.size() == 0 ? null : p.zoneShapes.get(0).getShapeStyle(); }
    public void setZonesStyle(ShapeStyle style) { for (VShape<Point2D.Double> sh : p.zoneShapes) sh.setShapeStyle(style); }

}
