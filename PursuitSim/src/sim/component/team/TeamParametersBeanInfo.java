/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.component.team;

import java.beans.*;

/**
 *
 * @author ae3263
 */
public class TeamParametersBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( sim.component.team.TeamParameters.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_captureCondition = 0;
    private static final int PROPERTY_color = 1;
    private static final int PROPERTY_defaultAgentParameters = 2;
    private static final int PROPERTY_locationGenerator = 3;
    private static final int PROPERTY_name = 4;
    private static final int PROPERTY_sensor = 5;
    private static final int PROPERTY_tasker = 6;
    private static final int PROPERTY_victoryCondition = 7;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[8];
    
        try {
            properties[PROPERTY_captureCondition] = new IndexedPropertyDescriptor ( "captureCondition", sim.component.team.TeamParameters.class, "getCaptureCondition", "setCaptureCondition", "getCaptureCondition", "setCaptureCondition" ); // NOI18N
            properties[PROPERTY_color] = new PropertyDescriptor ( "color", sim.component.team.TeamParameters.class, "getColor", "setColor" ); // NOI18N
            properties[PROPERTY_color].setPreferred ( true );
            properties[PROPERTY_defaultAgentParameters] = new PropertyDescriptor ( "defaultAgentParameters", sim.component.team.TeamParameters.class, "getDefaultAgentParameters", "setDefaultAgentParameters" ); // NOI18N
            properties[PROPERTY_defaultAgentParameters].setPreferred ( true );
            properties[PROPERTY_locationGenerator] = new PropertyDescriptor ( "locationGenerator", sim.component.team.TeamParameters.class, "getLocationGenerator", "setLocationGenerator" ); // NOI18N
            properties[PROPERTY_locationGenerator].setPreferred ( true );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", sim.component.team.TeamParameters.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_name].setPreferred ( true );
            properties[PROPERTY_sensor] = new PropertyDescriptor ( "sensor", sim.component.team.TeamParameters.class, "getSensor", "setSensor" ); // NOI18N
            properties[PROPERTY_tasker] = new IndexedPropertyDescriptor ( "tasker", sim.component.team.TeamParameters.class, "getTasker", "setTasker", "getTasker", "setTasker" ); // NOI18N
            properties[PROPERTY_victoryCondition] = new PropertyDescriptor ( "victoryCondition", sim.component.team.TeamParameters.class, "getVictoryCondition", "setVictoryCondition" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_capture0 = 0;
    private static final int METHOD_color1 = 1;
    private static final int METHOD_copyParametersFrom2 = 2;
    private static final int METHOD_locations3 = 3;
    private static final int METHOD_sensor4 = 4;
    private static final int METHOD_tasker5 = 5;
    private static final int METHOD_toString6 = 6;
    private static final int METHOD_victory7 = 7;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[8];
    
        try {
            methods[METHOD_capture0] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("capture", new Class[] {sim.metrics.Capture.class})); // NOI18N
            methods[METHOD_capture0].setDisplayName ( "" );
            methods[METHOD_color1] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("color", new Class[] {java.awt.Color.class})); // NOI18N
            methods[METHOD_color1].setDisplayName ( "" );
            methods[METHOD_copyParametersFrom2] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("copyParametersFrom", new Class[] {sim.component.team.TeamParameters.class})); // NOI18N
            methods[METHOD_copyParametersFrom2].setDisplayName ( "" );
            methods[METHOD_locations3] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("locations", new Class[] {sim.component.team.LocationGenerator.class})); // NOI18N
            methods[METHOD_locations3].setDisplayName ( "" );
            methods[METHOD_sensor4] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("sensor", new Class[] {sim.comms.Sensor.class})); // NOI18N
            methods[METHOD_sensor4].setDisplayName ( "" );
            methods[METHOD_tasker5] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("tasker", new Class[] {sim.tasks.Tasker.class})); // NOI18N
            methods[METHOD_tasker5].setDisplayName ( "" );
            methods[METHOD_toString6] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString6].setDisplayName ( "" );
            methods[METHOD_victory7] = new MethodDescriptor(sim.component.team.TeamParameters.class.getMethod("victory", new Class[] {sim.metrics.VictoryCondition.class})); // NOI18N
            methods[METHOD_victory7].setDisplayName ( "" );
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

