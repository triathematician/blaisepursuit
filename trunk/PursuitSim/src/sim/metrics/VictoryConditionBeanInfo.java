/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.metrics;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 *
 * @author ae3263
 */
public class VictoryConditionBeanInfo extends SimpleBeanInfo {
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[7];

        try {
            properties[0] = new PropertyDescriptor ( "team1", sim.metrics.VictoryCondition.class );
            properties[0].setPreferred( true );
            properties[1] = new PropertyDescriptor ( "metric", sim.metrics.VictoryCondition.class );
            properties[1].setPreferred( true );
            properties[2] = new PropertyDescriptor ( "metricByName", sim.metrics.VictoryCondition.class );
            properties[2].setHidden ( true );
            properties[3] = new PropertyDescriptor ( "team2", sim.metrics.VictoryCondition.class );
            properties[3].setPreferred( true );
            properties[4] = new PropertyDescriptor ( "type", sim.metrics.VictoryCondition.class );
            properties[4].setPreferred( true );
            properties[5] = new PropertyDescriptor ( "typeByName", sim.metrics.VictoryCondition.class );
            properties[5].setHidden ( true );
            properties[6] = new PropertyDescriptor ( "threshold", sim.metrics.VictoryCondition.class );
            properties[6].setPreferred( true );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }
        return properties; 
    };

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }


}
