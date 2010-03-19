/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.beans.*;

/**
 *
 * @author ae3263
 */
public class DistributionScenarioVisBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( main.DistributionScenarioVis.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_adjusting = 0;
    private static final int PROPERTY_animationOn = 1;
    private static final int PROPERTY_editable = 2;
    private static final int PROPERTY_mouseEditsPoints = 3;
    private static final int PROPERTY_name = 4;
    private static final int PROPERTY_plottableArray = 5;
    private static final int PROPERTY_plottables = 6;
    private static final int PROPERTY_points = 7;
    private static final int PROPERTY_polygon = 8;
    private static final int PROPERTY_scenario = 9;
    private static final int PROPERTY_selected = 10;
    private static final int PROPERTY_tesselationVisible = 11;
    private static final int PROPERTY_visible = 12;
    private static final int PROPERTY_visualPoints = 13;
    private static final int PROPERTY_visualPolygon = 14;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[15];
    
        try {
            properties[PROPERTY_adjusting] = new PropertyDescriptor ( "adjusting", main.DistributionScenarioVis.class, "isAdjusting", null ); // NOI18N
            properties[PROPERTY_adjusting].setHidden ( true );
            properties[PROPERTY_animationOn] = new PropertyDescriptor ( "animationOn", main.DistributionScenarioVis.class, "isAnimationOn", "setAnimationOn" ); // NOI18N
            properties[PROPERTY_animationOn].setHidden ( true );
            properties[PROPERTY_editable] = new PropertyDescriptor ( "editable", main.DistributionScenarioVis.class, "isEditable", "setEditable" ); // NOI18N
            properties[PROPERTY_editable].setExpert ( true );
            properties[PROPERTY_mouseEditsPoints] = new PropertyDescriptor ( "mouseEditsPoints", main.DistributionScenarioVis.class, "isMouseEditsPoints", "setMouseEditsPoints" ); // NOI18N
            properties[PROPERTY_mouseEditsPoints].setPreferred ( true );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", main.DistributionScenarioVis.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_name].setExpert ( true );
            properties[PROPERTY_plottableArray] = new IndexedPropertyDescriptor ( "plottableArray", main.DistributionScenarioVis.class, "getPlottableArray", "setPlottableArray", "getPlottableArray", "setPlottableArray" ); // NOI18N
            properties[PROPERTY_plottableArray].setHidden ( true );
            properties[PROPERTY_plottables] = new PropertyDescriptor ( "plottables", main.DistributionScenarioVis.class, "getPlottables", null ); // NOI18N
            properties[PROPERTY_plottables].setExpert ( true );
            properties[PROPERTY_points] = new IndexedPropertyDescriptor ( "points", main.DistributionScenarioVis.class, "getPoints", "setPoints", "getPoints", "setPoints" ); // NOI18N
            properties[PROPERTY_points].setPreferred ( true );
            properties[PROPERTY_polygon] = new IndexedPropertyDescriptor ( "polygon", main.DistributionScenarioVis.class, "getPolygon", "setPolygon", "getPolygon", "setPolygon" ); // NOI18N
            properties[PROPERTY_polygon].setPreferred ( true );
            properties[PROPERTY_scenario] = new PropertyDescriptor ( "scenario", main.DistributionScenarioVis.class, "getScenario", "setScenario" ); // NOI18N
            properties[PROPERTY_scenario].setExpert ( true );
            properties[PROPERTY_selected] = new PropertyDescriptor ( "selected", main.DistributionScenarioVis.class, "isSelected", "setSelected" ); // NOI18N
            properties[PROPERTY_selected].setHidden ( true );
            properties[PROPERTY_tesselationVisible] = new PropertyDescriptor ( "tesselationVisible", main.DistributionScenarioVis.class, "isTesselationVisible", "setTesselationVisible" ); // NOI18N
            properties[PROPERTY_tesselationVisible].setPreferred ( true );
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", main.DistributionScenarioVis.class, "isVisible", "setVisible" ); // NOI18N
            properties[PROPERTY_visible].setExpert ( true );
            properties[PROPERTY_visualPoints] = new PropertyDescriptor ( "visualPoints", main.DistributionScenarioVis.class, "getVisualPoints", "setVisualPoints" ); // NOI18N
            properties[PROPERTY_visualPoints].setExpert ( true );
            properties[PROPERTY_visualPolygon] = new PropertyDescriptor ( "visualPolygon", main.DistributionScenarioVis.class, "getVisualPolygon", "setVisualPolygon" ); // NOI18N
            properties[PROPERTY_visualPolygon].setExpert ( true );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_changeListener = 0;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];
    
        try {
            eventSets[EVENT_changeListener] = new EventSetDescriptor ( main.DistributionScenarioVis.class, "changeListener", javax.swing.event.ChangeListener.class, new String[] {"stateChanged"}, "addChangeListener", "removeChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_add0 = 0;
    private static final int METHOD_addAll1 = 1;
    private static final int METHOD_clear2 = 2;
    private static final int METHOD_draw3 = 3;
    private static final int METHOD_handleCoordinate4 = 4;
    private static final int METHOD_isClickablyCloseTo5 = 5;
    private static final int METHOD_mouseClicked6 = 6;
    private static final int METHOD_mouseDragged7 = 7;
    private static final int METHOD_mouseEntered8 = 8;
    private static final int METHOD_mouseExited9 = 9;
    private static final int METHOD_mouseMoved10 = 10;
    private static final int METHOD_mousePressed11 = 11;
    private static final int METHOD_mouseReleased12 = 12;
    private static final int METHOD_recomputeAtTime13 = 13;
    private static final int METHOD_remove14 = 14;
    private static final int METHOD_stateChanged15 = 15;
    private static final int METHOD_toString16 = 16;
    private static final int METHOD_visometryChanged17 = 17;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[18];
    
        try {
            methods[METHOD_add0] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("add", new Class[] {org.bm.blaise.specto.visometry.Plottable.class})); // NOI18N
            methods[METHOD_add0].setDisplayName ( "" );
            methods[METHOD_addAll1] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("addAll", new Class[] {java.util.Collection.class})); // NOI18N
            methods[METHOD_addAll1].setDisplayName ( "" );
            methods[METHOD_clear2] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("clear", new Class[] {})); // NOI18N
            methods[METHOD_clear2].setDisplayName ( "" );
            methods[METHOD_draw3] = new MethodDescriptor(main.DistributionScenarioVis.class.getMethod("draw", new Class[] {org.bm.blaise.specto.visometry.VisometryGraphics.class})); // NOI18N
            methods[METHOD_draw3].setDisplayName ( "" );
            methods[METHOD_handleCoordinate4] = new MethodDescriptor(main.DistributionScenarioVis.class.getMethod("handleCoordinate", new Class[] {java.awt.geom.Point2D.Double.class})); // NOI18N
            methods[METHOD_handleCoordinate4].setDisplayName ( "" );
            methods[METHOD_isClickablyCloseTo5] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("isClickablyCloseTo", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_isClickablyCloseTo5].setDisplayName ( "" );
            methods[METHOD_mouseClicked6] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseClicked", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseClicked6].setDisplayName ( "" );
            methods[METHOD_mouseDragged7] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseDragged", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseDragged7].setDisplayName ( "" );
            methods[METHOD_mouseEntered8] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseEntered", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseEntered8].setDisplayName ( "" );
            methods[METHOD_mouseExited9] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseExited", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseExited9].setDisplayName ( "" );
            methods[METHOD_mouseMoved10] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseMoved", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseMoved10].setDisplayName ( "" );
            methods[METHOD_mousePressed11] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mousePressed", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mousePressed11].setDisplayName ( "" );
            methods[METHOD_mouseReleased12] = new MethodDescriptor(org.bm.blaise.specto.visometry.DynamicPlottable.class.getMethod("mouseReleased", new Class[] {org.bm.blaise.specto.visometry.VisometryMouseEvent.class})); // NOI18N
            methods[METHOD_mouseReleased12].setDisplayName ( "" );
            methods[METHOD_recomputeAtTime13] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("recomputeAtTime", new Class[] {org.bm.blaise.specto.visometry.Visometry.class, org.bm.blaise.specto.visometry.VisometryGraphics.class, org.bm.blaise.sequor.timer.TimeClock.class})); // NOI18N
            methods[METHOD_recomputeAtTime13].setDisplayName ( "" );
            methods[METHOD_remove14] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("remove", new Class[] {org.bm.blaise.specto.visometry.Plottable.class})); // NOI18N
            methods[METHOD_remove14].setDisplayName ( "" );
            methods[METHOD_stateChanged15] = new MethodDescriptor(main.DistributionScenarioVis.class.getMethod("stateChanged", new Class[] {javax.swing.event.ChangeEvent.class})); // NOI18N
            methods[METHOD_stateChanged15].setDisplayName ( "" );
            methods[METHOD_toString16] = new MethodDescriptor(main.DistributionScenarioVis.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString16].setDisplayName ( "" );
            methods[METHOD_visometryChanged17] = new MethodDescriptor(org.bm.blaise.specto.visometry.PlottableGroup.class.getMethod("visometryChanged", new Class[] {org.bm.blaise.specto.visometry.Visometry.class, org.bm.blaise.specto.visometry.VisometryGraphics.class})); // NOI18N
            methods[METHOD_visometryChanged17].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

    // Here you can add code for customizing the methods array.
    
        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

    
//GEN-FIRST:Superclass

    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass
	
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
	return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
	return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
	return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch ( iconKind ) {
        case ICON_COLOR_16x16:
            if ( iconNameC16 == null )
                return null;
            else {
                if( iconColor16 == null )
                    iconColor16 = loadImage( iconNameC16 );
                return iconColor16;
            }
        case ICON_COLOR_32x32:
            if ( iconNameC32 == null )
                return null;
            else {
                if( iconColor32 == null )
                    iconColor32 = loadImage( iconNameC32 );
                return iconColor32;
            }
        case ICON_MONO_16x16:
            if ( iconNameM16 == null )
                return null;
            else {
                if( iconMono16 == null )
                    iconMono16 = loadImage( iconNameM16 );
                return iconMono16;
            }
        case ICON_MONO_32x32:
            if ( iconNameM32 == null )
                return null;
            else {
                if( iconMono32 == null )
                    iconMono32 = loadImage( iconNameM32 );
                return iconMono32;
            }
	default: return null;
        }
    }

}

