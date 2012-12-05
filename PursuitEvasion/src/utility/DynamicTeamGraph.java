/*
 * DynamicTeamGraph.java
 * Created on Oct 16, 2007, 2:14:06 PM
 */

package utility;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.JMenu;
import simulation.Agent;
import simulation.Team;
import specto.Animatable;
import specto.Plottable;
import sequor.component.RangeTimer;
import scio.coordinate.R2;
import sequor.style.LineStyle;
import specto.Visometry;
import specto.euclidean2.Euclidean2;
import tasking.Tasking;

/**
 * <p>
 * Displays team communication graph at a given time.
 * Does this using the pre-computed paths in conjunction with comm distances.
 * </p>
 * @author ae3263
 */
public class DynamicTeamGraph extends Plottable<Euclidean2> implements Animatable {
    Team team;
    DataLog log;
    
    public DynamicTeamGraph(Team t,DataLog l){team=t;log=l;}
    
    public int pathSize(){
        return log.size();
    }

    @Override
    public void paintComponent(Graphics2D g, Euclidean2 v) {
    }

    /** Draws graph corresponding to current step. */
    public void paintComponent(Graphics2D g, Visometry v, RangeTimer t) {
        if(pathSize()==0){return;}
        g.setStroke(LineStyle.STROKES[LineStyle.DOTTED]);
        g.setColor(team.getColorValue().brighter().brighter());
        g.draw(getEdges((Euclidean2) v, t.getCurrentIntValue()));
        for (Tasking tk : team.getTaskings()) {
            g.draw(getTargetEdges((Euclidean2) v, tk, t.getCurrentIntValue()));
        }
    }   
    
    
    /** Checks each pair of agents against team's range parameter, returns draw element. */    
    public Path2D.Double getEdges(Euclidean2 visometry, int time){
        int timeB=time<0?0:(time>=pathSize()?pathSize()-1:time);
        Path2D.Double result=new Path2D.Double();
        R2 p1;
        R2 p2;
        for(int i=0;i<team.size();i++){
            p1=log.agentAt(team.get(i),timeB);
            for(int j=i+1;j<team.size();j++){
                p2=log.agentAt(team.get(j),timeB);
                if(p1.distance(p2)<team.getCommRange()){
                    result.moveTo(visometry.toWindowX(p1.x),visometry.toWindowY(p1.y));
                    result.lineTo(visometry.toWindowX(p2.x),visometry.toWindowY(p2.y));
                }
            }
        }
        return result;
    } 
    
    /** Gets edges for each target in range. */
    public Path2D.Double getTargetEdges(Euclidean2 visometry, Tasking tk, int time){
        if(tk.getTarget()==null){return new Path2D.Double();}
        int timeB =
                time < 0 ? 0 : (
                  time >= pathSize() ? pathSize()-1 : time
                );
        Path2D.Double result=new Path2D.Double();
        R2 p1;
        R2 p2;
        for(int i=0;i<team.size();i++){
            p1=log.agentAt(team.get(i),timeB);
            for(Agent a: tk.getTarget().getAgents()){
                p2=log.agentAt(a,timeB);
                if(p1.distance(p2)<team.getSensorRange()){
                    result.moveTo(visometry.toWindowX(p1.x),visometry.toWindowY(p1.y));
                    result.lineTo(visometry.toWindowX(p2.x),visometry.toWindowY(p2.y));
                }
            }
        }
        return result;        
    }

    public JMenu getOptionsMenu() {
        return new JMenu("Network ");
    }

    public void recompute() {
    }

    @Override
    public String[] getStyleStrings() {
        String[] result = {};
        return result;
    }

    public int getAnimatingSteps() { return 0; }


    boolean animating = true;

    public void setAnimationOn(boolean newValue) {
        this.animating = newValue;
    }

    public boolean isAnimationOn() {
        return animating;
    }

}
