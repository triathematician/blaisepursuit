/**
 * SampleSims.java
 * Created on Jul 23, 2009
 */
package gsim.samples;

import java.awt.Color;
import java.awt.geom.Point2D;
import sim.*;
import java.beans.XMLEncoder;
import scio.coordinate.utils.PlanarMathUtils;
import sim.agent.*;
import sim.metrics.CaptureCondition;
import sim.metrics.ComparisonType;
import sim.metrics.TeamMetrics;
import sim.metrics.VictoryCondition;
import sim.agent.Sensor;
import sim.tasks.Taskers;
import sim.tasks.Router.RouterEnum;
import sim.tasks.Task;
import sim.tasks.TaskChooser;

/**
 * <p>
 *   <code>SampleSims</code> is a factory class for generating particular kinds of simulations.
 *   Each enum constant provides a method to retrieve a simulation of specified type.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum SampleSims {

    PURSUIT_CURVES("Pursuit Curve (Circle)") {
        public Simulation getSimulation() {
            SimulationPath path = new SimulationPath("15*cos(t)","15*sin(t)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent1 = new SimulationAgent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent2 = new SimulationAgent(agent2Par);

            return new Simulation(name, path, agent1, agent2);
        }
    },

    PURSUIT_CURVES_2("Pursuit Curve (Line)") {
        public Simulation getSimulation() {
            SimulationPath path = new SimulationPath("5*t-30","3.5*t");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent1 = new SimulationAgent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent2 = new SimulationAgent(agent2Par);

            return new Simulation(name, path, agent1, agent2);
        }
    },

    PURSUIT_CURVES_3("Pursuit Curve (Sinusoid)") {
        public Simulation getSimulation() {
            SimulationPath path = new SimulationPath("5*t-20","10*sin(t/2)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent1 = new SimulationAgent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent2 = new SimulationAgent(agent2Par);

            return new Simulation(name, path, agent1, agent2);
        }
    },

    RANDOM_PURSUIT("Random-Path Pursuit") {
        public Simulation getSimulation() {
            SimulationRandomPath path = new SimulationRandomPath();

            AgentParameters agent1Par = new AgentParameters("Pursuer", Color.GREEN, 1.2).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(Taskers.followInstance(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            SimulationAgent agent1 = new SimulationAgent(agent1Par);

            return new Simulation(name, path, agent1);
        }
    },

    CONSTRAINED_ANGLE("Constrained-Angle Pursuit") {
        public Simulation getSimulation() {
            SimulationPath path = new SimulationPath("15*cos(t)","15*sin(t)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 2.0).turnRadius(1.0)
                    .location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER);
            SimulationAgent agent1 = new SimulationAgent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 3.0).turnRadius(5.0)
                    .location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .tasker(Taskers.multipleTaskerInstance(
                    Taskers.followInstance(path, Task.Type.FLEE),
                    Taskers.followInstance(agent1, Task.Type.SEEK)));
            SimulationAgent agent2 = new SimulationAgent(agent2Par);
            agent1Par.tasker(Taskers.multipleTaskerInstance(
                    Taskers.followInstance(path, Task.Type.SEEK),
                    Taskers.followInstance(agent2, Task.Type.FLEE)));

            return new Simulation(name, path, agent1, agent2);
        }
    },

    CYCLIC_PURSUIT("Cyclic Pursuit") {
        public Simulation getSimulation() {
            int n = 32;
            AgentParameters[] pars = new AgentParameters[n];
            SimulationAgent[] ags = new SimulationAgent[n];
            for (int i=0; i<n; i++) {
                pars[i] = new AgentParameters("Pursuer " + i, new Color((255*i)/n, 255-(255*i)/n, 32), 2.0+i/2.0).turnRadius(1.0+i/2.0)
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .location(PlanarMathUtils.toCartesianFromPolar(20.0, 1.5*i/(double)n*Math.PI));
                ags[i] = new SimulationAgent(pars[i]);
            }
            for (int i = 0; i < n; i++) {
                pars[i].tasker(Taskers.multipleTaskerInstance(
                        Taskers.followInstance(ags[(i+n-1)%n], Task.Type.FLEE),
                        Taskers.followInstance(ags[(i+1)%n], Task.Type.CAPTURE))
                        );
            }
            return new Simulation(name, ags);
        }
    },

    BUG_LIGHT("Bug-Zapper") {
        public Simulation getSimulation() {
            AgentParameters bugPar = new AgentParameters().topSpeed(1.2).router(RouterEnum.LEADING);
            SimulationTeam bugs = new SimulationTeam( 4,
                    new TeamParameters("Bugs", Color.DARK_GRAY, bugPar).sensor(Sensor.GLOBAL_SENSOR) );
            bugs.setStartingLocations(new Point2D.Double[]{new Point2D.Double(-30, 30), new Point2D.Double(-20, -20), new Point2D.Double(50, 10), new Point2D.Double(10, 10)});

            AgentParameters lightPar = new AgentParameters().topSpeed(1.0).sensor(new Sensor.Radial(15.0));
            SimulationTeam lights = new SimulationTeam( 1,
                    new TeamParameters("Light", new Color(0, 96, 0), lightPar).locations(new LocationGenerator.RandomBox(50.0)) );

            bugs.setAgentTaskGenerator( Taskers.findClosestInstance(lights.getInitiallyActiveAgents(), Task.Type.SEEK) );
              bugs.getParameters().setCaptureCondition( new CaptureCondition(bugs, lights, 1.0) );
              bugs.getParameters().setVictoryCondition( new VictoryCondition(bugs, lights, TeamMetrics.MINIMUM_DISTANCE, ComparisonType.LESS_EQUAL, 5.0) );

            lights.getParameters().setVictoryCondition( new VictoryCondition(lights, null, TeamMetrics.SIMULATION_TIME, ComparisonType.GREATER_EQUAL, 50.0) );
              lights.setAgentTaskGenerator( Taskers.gradientInstance(bugs.getInitiallyActiveAgents(), Task.Type.FLEE) );

            // TODO - add metrics
//            new Valuation(teams, bugs, lights, TeamMetrics.PERCENTAGE_OF_OPPONENTS_ACTIVE);
//            new Valuation(teams, lights, bugs, TeamMetrics.SIMULATION_TIME);

            return new Simulation(name, bugs, lights);
        }
    },

    COPS_ROBBERS("Cops-and-Robbers") {
        public Simulation getSimulation() {
            AgentParameters copPar = new AgentParameters().topSpeed(6.0).router(RouterEnum.LEADING);
            SimulationTeam cops = new SimulationTeam(5,
                    new TeamParameters("Cops", Color.BLUE, copPar).locations(new LocationGenerator.RandomBox(50.0)).sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters robberPar = new AgentParameters().topSpeed(5.0).sensor(Sensor.GLOBAL_SENSOR);
            SimulationTeam robbers = new SimulationTeam(4,
                    new TeamParameters("Robbers", Color.ORANGE, robberPar).locations(new LocationGenerator.RandomBox(50.0)) );

            cops.getParameters().setTasker( Taskers.controlClosestInstance(robbers.getInitiallyActiveAgents(), Task.Type.CAPTURE) );
              cops.getParameters().setCaptureCondition( new CaptureCondition(cops, robbers, 5.0) );
              cops.getParameters().setVictoryCondition( new VictoryCondition(cops, robbers, TeamMetrics.NUMBER_OF_OPPONENTS, ComparisonType.LESS_EQUAL, 2) );

            robbers.setAgentTaskGenerator( Taskers.gradientInstance(cops.getInitiallyActiveAgents(), Task.Type.FLEE) );

            // TODO - add metrics
//            new Valuation(teams, cops, robbers, TeamMetrics.AVERAGE_DISTANCE);

            return new Simulation(name, cops, robbers);
        }
    },

    ANTARCTICA("Antarctica") {
        public Simulation getSimulation() {
            AgentParameters sealPar = new AgentParameters().router(RouterEnum.LEADING);
            SimulationTeam seals = new SimulationTeam(3,
                    new TeamParameters("Seals", Color.BLUE, sealPar).locations(new LocationGenerator.RandomBox(50.0)).sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters penguinPar = new AgentParameters().router(RouterEnum.LEADING);
            SimulationTeam penguins = new SimulationTeam(4,
                    new TeamParameters("Penguins", Color.BLACK, penguinPar).locations(new LocationGenerator.RandomBox(50.0)).sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters fishPar = new AgentParameters();
            SimulationTeam fish = new SimulationTeam(5,
                    new TeamParameters("Fish", Color.GREEN, fishPar).locations(new LocationGenerator.RandomBox(50.0)).sensor(Sensor.GLOBAL_SENSOR) );

            seals.getParameters().setTasker(Taskers.controlClosestInstance(penguins.getInitiallyActiveAgents(), Task.Type.CAPTURE));
              seals.setAgentTaskGenerator( Taskers.gradientInstance(fish.getInitiallyActiveAgents(), Task.Type.FLEE) );

            penguins.getParameters().setTasker(Taskers.controlClosestInstance(fish.getInitiallyActiveAgents(), Task.Type.CAPTURE));
              penguins.setAgentTaskGenerator( Taskers.gradientInstance(seals.getInitiallyActiveAgents(), Task.Type.FLEE) );

            fish.getParameters().setTasker(Taskers.controlClosestInstance(seals.getInitiallyActiveAgents(), Task.Type.CAPTURE));
              fish.setAgentTaskGenerator( Taskers.gradientInstance(penguins.getInitiallyActiveAgents(), Task.Type.FLEE) );

            return new Simulation(name, seals, penguins, fish);
        }
    };

//    SAHARA() {
//        public Simulation getSimulation() {
//            AgentParameters lionPar = new AgentParameters().topSpeed(6.5).taskImplementer(TaskImplementers.getSprialSearchPattern())
//            SimulationTeam lions = new SimulationTeam("Lion", 4,
//                    new TeamParameters(lionPar, Color.ORANGE).locationGenerator(new LGRandom(50.0)) );
//
//            return new Simulation("Sahara", lions, wildebeest, water);
//        }
//    }

    String name;
    SampleSims(String name) { this.name = name; }
    @Override public String toString() { return name; }

    public abstract Simulation getSimulation();

    public static void main(String[] args) {            
        XMLEncoder en = new XMLEncoder(System.out);
        //en.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
        en.writeObject(COPS_ROBBERS.getSimulation());
        en.flush();
    }
}
