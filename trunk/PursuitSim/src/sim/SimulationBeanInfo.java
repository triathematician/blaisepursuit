/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim;

import java.beans.*;

/**
 *
 * @author ae3263
 */
public class SimulationBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( sim.Simulation.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_allAgents = 0;
    private static final int PROPERTY_component = 1;
    private static final int PROPERTY_finished = 2;
    private static final int PROPERTY_maxTime = 3;
    private static final int PROPERTY_name = 4;
    private static final int PROPERTY_timePerStep = 5;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[6];
    
        try {
            properties[PROPERTY_allAgents] = new PropertyDescriptor ( "allAgents", sim.Simulation.class, "getAllAgents", null ); // NOI18N
            properties[PROPERTY_allAgents].setHidden ( true );
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", sim.Simulation.class, "getComponent", "setComponent", "getComponent", "setComponent" ); // NOI18N
            properties[PROPERTY_component].setExpert ( true );
            properties[PROPERTY_finished] = new PropertyDescriptor ( "finished", sim.Simulation.class, "isFinished", null ); // NOI18N
            properties[PROPERTY_finished].setHidden ( true );
            properties[PROPERTY_maxTime] = new PropertyDescriptor ( "maxTime", sim.Simulation.class, "getMaxTime", "setMaxTime" ); // NOI18N
            properties[PROPERTY_maxTime].setPreferred ( true );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", sim.Simulation.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_name].setPreferred ( true );
            properties[PROPERTY_timePerStep] = new PropertyDescriptor ( "timePerStep", sim.Simulation.class, "getTimePerStep", "setTimePerStep" ); // NOI18N
            properties[PROPERTY_timePerStep].setPreferred ( true );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_changeListener = 0;
    private static final int EVENT_simulationEventListener = 1;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[2];
    
        try {
            eventSets[EVENT_changeListener] = new EventSetDescriptor ( sim.Simulation.class, "changeListener", javax.swing.event.ChangeListener.class, new String[] {"stateChanged"}, "addChangeListener", "removeChangeListener" ); // NOI18N
            eventSets[EVENT_simulationEventListener] = new EventSetDescriptor ( sim.Simulation.class, "simulationEventListener", sim.SimulationEventListener.class, new String[] {"handleSimulationEvent"}, "addSimulationEventListener", "removeSimulationEventListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_acceptEvent0 = 0;
    private static final int METHOD_addComponent1 = 1;
    private static final int METHOD_addComponents2 = 2;
    private static final int METHOD_adjustState3 = 3;
    private static final int METHOD_checkVictory4 = 4;
    private static final int METHOD_clearComponents5 = 5;
    private static final int METHOD_containsComponent6 = 6;
    private static final int METHOD_developPointOfView7 = 7;
    private static final int METHOD_fireStateChanged8 = 8;
    private static final int METHOD_gatherSensoryData9 = 9;
    private static final int METHOD_generateTasks10 = 10;
    private static final int METHOD_getComponentsByType11 = 11;
    private static final int METHOD_handleMajorEvents12 = 12;
    private static final int METHOD_initStateVariables13 = 13;
    private static final int METHOD_iterate14 = 14;
    private static final int METHOD_postRun15 = 15;
    private static final int METHOD_preRun16 = 16;
    private static final int METHOD_processIncomingCommEvents17 = 17;
    private static final int METHOD_removeAllChangeListeners18 = 18;
    private static final int METHOD_removeAllSimulationEventListeners19 = 19;
    private static final int METHOD_removeComponent20 = 20;
    private static final int METHOD_run21 = 21;
    private static final int METHOD_runInBackground22 = 22;
    private static final int METHOD_sendAllCommEvents23 = 23;
    private static final int METHOD_setControlVariables24 = 24;
    private static final int METHOD_toString25 = 25;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[26];
    
        try {
            methods[METHOD_acceptEvent0] = new MethodDescriptor(sim.SimComposite.class.getMethod("acceptEvent", new Class[] {sim.comms.Comm.class})); // NOI18N
            methods[METHOD_acceptEvent0].setDisplayName ( "" );
            methods[METHOD_addComponent1] = new MethodDescriptor(sim.Simulation.class.getMethod("addComponent", new Class[] {sim.SimComponent.class})); // NOI18N
            methods[METHOD_addComponent1].setDisplayName ( "" );
            methods[METHOD_addComponents2] = new MethodDescriptor(sim.Simulation.class.getMethod("addComponents", new Class[] {java.util.Collection.class})); // NOI18N
            methods[METHOD_addComponents2].setDisplayName ( "" );
            methods[METHOD_adjustState3] = new MethodDescriptor(sim.SimComposite.class.getMethod("adjustState", new Class[] {double.class})); // NOI18N
            methods[METHOD_adjustState3].setDisplayName ( "" );
            methods[METHOD_checkVictory4] = new MethodDescriptor(sim.SimComposite.class.getMethod("checkVictory", new Class[] {sim.DistanceCache.class, double.class})); // NOI18N
            methods[METHOD_checkVictory4].setDisplayName ( "" );
            methods[METHOD_clearComponents5] = new MethodDescriptor(sim.SimComposite.class.getMethod("clearComponents", new Class[] {})); // NOI18N
            methods[METHOD_clearComponents5].setDisplayName ( "" );
            methods[METHOD_containsComponent6] = new MethodDescriptor(sim.SimComposite.class.getMethod("containsComponent", new Class[] {sim.SimComponent.class})); // NOI18N
            methods[METHOD_containsComponent6].setDisplayName ( "" );
            methods[METHOD_developPointOfView7] = new MethodDescriptor(sim.SimComposite.class.getMethod("developPointOfView", new Class[] {})); // NOI18N
            methods[METHOD_developPointOfView7].setDisplayName ( "" );
            methods[METHOD_fireStateChanged8] = new MethodDescriptor(sim.Simulation.class.getMethod("fireStateChanged", new Class[] {})); // NOI18N
            methods[METHOD_fireStateChanged8].setDisplayName ( "" );
            methods[METHOD_gatherSensoryData9] = new MethodDescriptor(sim.SimComposite.class.getMethod("gatherSensoryData", new Class[] {sim.DistanceCache.class})); // NOI18N
            methods[METHOD_gatherSensoryData9].setDisplayName ( "" );
            methods[METHOD_generateTasks10] = new MethodDescriptor(sim.SimComposite.class.getMethod("generateTasks", new Class[] {sim.DistanceCache.class})); // NOI18N
            methods[METHOD_generateTasks10].setDisplayName ( "" );
            methods[METHOD_getComponentsByType11] = new MethodDescriptor(sim.SimComposite.class.getMethod("getComponentsByType", new Class[] {java.lang.Class.class})); // NOI18N
            methods[METHOD_getComponentsByType11].setDisplayName ( "" );
            methods[METHOD_handleMajorEvents12] = new MethodDescriptor(sim.SimComposite.class.getMethod("handleMajorEvents", new Class[] {sim.DistanceCache.class, double.class})); // NOI18N
            methods[METHOD_handleMajorEvents12].setDisplayName ( "" );
            methods[METHOD_initStateVariables13] = new MethodDescriptor(sim.Simulation.class.getMethod("initStateVariables", new Class[] {})); // NOI18N
            methods[METHOD_initStateVariables13].setDisplayName ( "" );
            methods[METHOD_iterate14] = new MethodDescriptor(sim.Simulation.class.getMethod("iterate", new Class[] {})); // NOI18N
            methods[METHOD_iterate14].setDisplayName ( "" );
            methods[METHOD_postRun15] = new MethodDescriptor(sim.Simulation.class.getMethod("postRun", new Class[] {})); // NOI18N
            methods[METHOD_postRun15].setDisplayName ( "" );
            methods[METHOD_preRun16] = new MethodDescriptor(sim.Simulation.class.getMethod("preRun", new Class[] {})); // NOI18N
            methods[METHOD_preRun16].setDisplayName ( "" );
            methods[METHOD_processIncomingCommEvents17] = new MethodDescriptor(sim.SimComposite.class.getMethod("processIncomingCommEvents", new Class[] {double.class})); // NOI18N
            methods[METHOD_processIncomingCommEvents17].setDisplayName ( "" );
            methods[METHOD_removeAllChangeListeners18] = new MethodDescriptor(sim.Simulation.class.getMethod("removeAllChangeListeners", new Class[] {})); // NOI18N
            methods[METHOD_removeAllChangeListeners18].setDisplayName ( "" );
            methods[METHOD_removeAllSimulationEventListeners19] = new MethodDescriptor(sim.Simulation.class.getMethod("removeAllSimulationEventListeners", new Class[] {})); // NOI18N
            methods[METHOD_removeAllSimulationEventListeners19].setDisplayName ( "" );
            methods[METHOD_removeComponent20] = new MethodDescriptor(sim.Simulation.class.getMethod("removeComponent", new Class[] {sim.SimComponent.class})); // NOI18N
            methods[METHOD_removeComponent20].setDisplayName ( "" );
            methods[METHOD_run21] = new MethodDescriptor(sim.Simulation.class.getMethod("run", new Class[] {})); // NOI18N
            methods[METHOD_run21].setDisplayName ( "" );
            methods[METHOD_runInBackground22] = new MethodDescriptor(sim.Simulation.class.getMethod("runInBackground", new Class[] {})); // NOI18N
            methods[METHOD_runInBackground22].setDisplayName ( "" );
            methods[METHOD_sendAllCommEvents23] = new MethodDescriptor(sim.SimComposite.class.getMethod("sendAllCommEvents", new Class[] {double.class})); // NOI18N
            methods[METHOD_sendAllCommEvents23].setDisplayName ( "" );
            methods[METHOD_setControlVariables24] = new MethodDescriptor(sim.SimComposite.class.getMethod("setControlVariables", new Class[] {double.class, double.class})); // NOI18N
            methods[METHOD_setControlVariables24].setDisplayName ( "" );
            methods[METHOD_toString25] = new MethodDescriptor(sim.Simulation.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString25].setDisplayName ( "" );
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

