/**
 * SampleSims.java
 * Created on Jul 23, 2009
 */
package gsim.samples;

import sim.component.ParametricPath;
import sim.component.Obstacle;
import sim.component.agent.AgentParameters;
import sim.component.agent.Agent;
import sim.component.RandomPath;
import sim.comms.Sensor;
import sim.component.team.TeamParameters;
import sim.component.team.LocationGenerator;
import sim.component.team.Team;
import gsim.logger.SimulationLogger;
import gsim.logger.AgentMetricLogger;
import gsim.logger.TeamMetricLogger;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import scio.coordinate.utils.PlanarMathUtils;
import sim.*;
import sim.metrics.*;
import sim.tasks.*;
import sim.tasks.Router.RouterEnum;

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
        public Simulation getSimulation(List<SimulationLogger> logs) {
            ParametricPath path = new ParametricPath("15*cos(t/2)","15*sin(t/2)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent1 = new Agent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent2 = new Agent(agent2Par);

            Simulation result = new Simulation(name, path, agent1, agent2);
              logs.add(new AgentMetricLogger("Pursuer 1 - Path", result, AgentMetrics.DISTANCE, agent1, path));
              logs.add(new AgentMetricLogger("Pursuer 1 - Pursuer 2", result, AgentMetrics.DISTANCE, agent1, agent2));
              logs.add(new AgentMetricLogger("Pursuer 2 - Path", result, AgentMetrics.DISTANCE, agent2, path));
            return result;
        }
    },

    PURSUIT_CURVES_2("Pursuit Curve (Line)") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            ParametricPath path = new ParametricPath("5*t-30","3.5*t");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent1 = new Agent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent2 = new Agent(agent2Par);

            Simulation result = new Simulation(name, path, agent1, agent2);
              logs.add(new AgentMetricLogger("Pursuer 1 - Path", result, AgentMetrics.DISTANCE, agent1, path));
              logs.add(new AgentMetricLogger("Pursuer 1 - Pursuer 2", result, AgentMetrics.DISTANCE, agent1, agent2));
              logs.add(new AgentMetricLogger("Pursuer 2 - Path", result, AgentMetrics.DISTANCE, agent2, path));
            return result;
        }
    },

    PURSUIT_CURVES_3("Pursuit Curve (Sinusoid)") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            ParametricPath path = new ParametricPath("5*t-20","10*sin(t/2)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 5.0).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent1 = new Agent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 6.0).location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(agent1, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent2 = new Agent(agent2Par);

            Simulation result = new Simulation(name, path, agent1, agent2);
              logs.add(new AgentMetricLogger("Pursuer 1 - Path", result, AgentMetrics.DISTANCE, agent1, path));
              logs.add(new AgentMetricLogger("Pursuer 1 - Pursuer 2", result, AgentMetrics.DISTANCE, agent1, agent2));
              logs.add(new AgentMetricLogger("Pursuer 2 - Path", result, AgentMetrics.DISTANCE, agent2, path));
            return result;
        }
    },

    RANDOM_PURSUIT("Random-Path Pursuit") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            RandomPath path = new RandomPath(2*Math.PI, 2.0);

            AgentParameters agent1Par = new AgentParameters("Pursuer", Color.GREEN, 1.2).location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).tasker(new Tasker.Follow(path, Task.Type.SEEK))
                    .router(RouterEnum.LEADING);
            Agent agent1 = new Agent(agent1Par);

            Simulation result = new Simulation(name, path, agent1);
              logs.add(new AgentMetricLogger("Pursuer 1 - Path", result, AgentMetrics.DISTANCE, agent1, path));
            return result;
        }
    },

    CONSTRAINED_ANGLE("Constrained-Angle Pursuit") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            ParametricPath path = new ParametricPath("15*cos(t/5)","15*sin(t/5)");

            AgentParameters agent1Par = new AgentParameters("Pursuer 1", Color.GREEN, 2.0).turnRadius(1.0)
                    .location(new Point2D.Double(10, 0))
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER);
            Agent agent1 = new Agent(agent1Par);

            AgentParameters agent2Par = new AgentParameters("Pursuer 2", Color.RED, 3.0).turnRadius(5.0)
                    .location(new Point2D.Double(0, 10))
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .tasker(new Tasker.Follow(path, Task.Type.FLEE))
                    .tasker(new Tasker.Follow(agent1, Task.Type.SEEK));
            Agent agent2 = new Agent(agent2Par);

            agent1Par.tasker(new Tasker.Follow(path, Task.Type.SEEK))
                    .tasker(new Tasker.Follow(agent2, Task.Type.FLEE));

            Simulation result = new Simulation(name, path, agent1, agent2);
              logs.add(new AgentMetricLogger("Pursuer 1 - Path", result, AgentMetrics.DISTANCE, agent1, path));
              logs.add(new AgentMetricLogger("Pursuer 1 - Pursuer 2", result, AgentMetrics.DISTANCE, agent1, agent2));
              logs.add(new AgentMetricLogger("Pursuer 2 - Path", result, AgentMetrics.DISTANCE, agent2, path));
            return result;
        }
    },

    CYCLIC_PURSUIT("Cyclic Pursuit") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            int n = 32;
            AgentParameters[] pars = new AgentParameters[n];
            Agent[] ags = new Agent[n];
            for (int i=0; i<n; i++) {
                pars[i] = new AgentParameters("Pursuer " + i, new Color((255*i)/n, 255-(255*i)/n, 32), 2.0+i/2.0).turnRadius(1.0+i/2.0)
                    .sensor(Sensor.GLOBAL_SENSOR).taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .location(PlanarMathUtils.toCartesianFromPolar(20.0, 1.5*i/(double)n*Math.PI));
                ags[i] = new Agent(pars[i]);
            }
            for (int i = 0; i < n; i++)
                pars[i].tasker(new Tasker.Follow(ags[(i+n-1)%n], Task.Type.FLEE))
                        .tasker(new Tasker.Follow(ags[(i+1)%n], Task.Type.CAPTURE));
            return new Simulation(name, ags);
        }
    },

    JURASSIC("Jurassic (lead factor)") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            ParametricPath approach = new ParametricPath("20cos(t/4)", "20sin(t/2)");
            Agent mathematician = new Agent(
                    new AgentParameters("Mathematician", Color.DARK_GRAY, 5.0)
                    .sensor(Sensor.GLOBAL_SENSOR)
                    .tasker(new Tasker.Follow(approach, Task.Type.SEEK)));
            Team mathematicians = new Team("Mathematicians");
            mathematicians.addComponent(mathematician);

            AgentParameters raptorPar = new AgentParameters()
                    .topSpeed(5.5)
                    .router(new Router.Leading())
                    .sensor(Sensor.GLOBAL_SENSOR);
            Team raptors = new Team(7,
                    new TeamParameters("Raptors", Color.RED, raptorPar)
                    .sensor(Sensor.GLOBAL_SENSOR)
                    .tasker(new Tasker.Follow(mathematician, Task.Type.SEEK)));
            float lf = 0.0f;
            for (SimComponent sc : raptors.getComponent()) {
                ((Agent)sc).getParameters().router(new Router.Leading(lf));
                ((Agent)sc).getParameters().setColor(Color.getHSBColor(0.2f + lf/2, 1.0f, 1.0f));
                lf += 0.2;
            }

            Simulation sim = new Simulation(name, approach, mathematicians, raptors);
              logs.add(new TeamMetricLogger("Min Distance", sim, TeamMetrics.MINIMUM_DISTANCE, raptors, mathematicians));
              logs.add(new TeamMetricLogger("Min Distance", sim, TeamMetrics.AVERAGE_DISTANCE, raptors, mathematicians));
              logs.add(new TeamMetricLogger("Max Distance", sim, TeamMetrics.MAXIMUM_DISTANCE, raptors, mathematicians));
            return sim;
        }
    },

    BUG_LIGHT("Bug-Zapper") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            AgentParameters bugPar = new AgentParameters()
                    .topSpeed(1.2)
                    .router(RouterEnum.LEADING);
            Team bugs = new Team( 4,
                    new TeamParameters("Bugs", Color.DARK_GRAY, bugPar)
                    .sensor(Sensor.GLOBAL_SENSOR) );
            bugs.setStartingLocations(new Point2D.Double[]{new Point2D.Double(-30, 30), new Point2D.Double(-20, -20), new Point2D.Double(50, 10), new Point2D.Double(10, 10)});

            AgentParameters lightPar = new AgentParameters()
                    .topSpeed(1.0)
                    .sensor(new Sensor.Radial(15.0));
            Team lights = new Team( 1,
                    new TeamParameters("Light", new Color(0, 96, 0), lightPar)
                    .locations(new LocationGenerator.RandomBox(50.0)) );

            bugs.addAgentTasker( new Tasker.Closest(lights.getAllAgents(), Task.Type.SEEK) );
            bugs.getParameters()
                    .capture( new Capture(bugs, lights, 1.0, Capture.Action.DEACTIVATE_OWNER) )
                    .victory( new VictoryCondition(bugs, lights, TeamMetrics.NUMBER_OF_OPPONENTS_ACTIVE, ComparisonType.EQUAL, 0.0) );

            lights.getParameters()
                    .victory( new VictoryCondition(lights, null, TeamMetrics.SIMULATION_TIME, ComparisonType.GREATER_EQUAL, 50.0) );
            lights.addAgentTasker( new Tasker.Gradient(bugs.getAllAgents(), Task.Type.FLEE) );

            Simulation sim = new Simulation(name, bugs, lights);
              logs.add(new TeamMetricLogger("Average distance", sim, TeamMetrics.AVERAGE_DISTANCE, bugs, lights));
            return sim;
        }
    },

    COPS_ROBBERS("Cops-and-Robbers") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            AgentParameters copPar = new AgentParameters()
                    .topSpeed(6.0)
                    .router(RouterEnum.LEADING);
            Team cops = new Team(5,
                    new TeamParameters("Cops", Color.BLUE, copPar)
                    .locations(new LocationGenerator.RandomBox(50.0))
                    .sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters robberPar = new AgentParameters()
                    .topSpeed(5.0)
                    .sensor(Sensor.GLOBAL_SENSOR);
            Team robbers = new Team(4,
                    new TeamParameters("Robbers", Color.ORANGE, robberPar)
                    .locations(new LocationGenerator.RandomBox(50.0)) );

            cops.getParameters()
                    .tasker( new Tasker.ControlClosest(robbers.getAllAgents(), Task.Type.CAPTURE) )
                    .capture( new Capture(cops, robbers, 5.0) )
                    .victory( new VictoryCondition(cops, robbers, TeamMetrics.NUMBER_OF_OPPONENTS_ACTIVE, ComparisonType.LESS_EQUAL, 2) );

            robbers.addAgentTasker( new Tasker.Gradient(cops.getAllAgents(), Task.Type.FLEE) );

            Simulation sim = new Simulation(name, cops, robbers);
              logs.add(new TeamMetricLogger("Average distance", sim, TeamMetrics.AVERAGE_DISTANCE, cops, robbers));
            return sim;
        }

    },

    ANTARCTICA("Antarctica") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            AgentParameters sealPar = new AgentParameters()
                    .taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .router(RouterEnum.LEADING)
                    .turnRadius(2.0);
            Team seals = new Team(2,
                    new TeamParameters("Seals", Color.BLUE, sealPar)
                    .locations(new LocationGenerator.RandomBox(30.0))
                    .sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters penguinPar = new AgentParameters()
                    .taskChooser(TaskChooser.GRADIENT_CHOOSER)
                    .router(RouterEnum.LEADING)
                    .turnRadius(1.0);
            Team penguins = new Team(3,
                    new TeamParameters("Penguins", Color.BLACK, penguinPar)
                    .locations(new LocationGenerator.RandomBox(30.0))
                    .sensor(Sensor.GLOBAL_SENSOR) );

            AgentParameters fishPar = new AgentParameters()
                    .taskChooser(TaskChooser.GRADIENT_CHOOSER);
            Team fish = new Team(4, new
                    TeamParameters("Fish", Color.GREEN, fishPar)
                    .locations(new LocationGenerator.RandomBox(30.0))
                    .sensor(Sensor.GLOBAL_SENSOR) );

            seals.getParameters()
                    .tasker( new Tasker.ControlClosest(penguins.getAllAgents(), Task.Type.CAPTURE, 1.0));
              seals.addAgentTasker( new Tasker.Gradient(fish.getAllAgents(), Task.Type.FLEE, 0.8) );

            penguins.getParameters()
                    .tasker( new Tasker.ControlClosest(fish.getAllAgents(), Task.Type.CAPTURE, 1.0));
              penguins.addAgentTasker( new Tasker.Gradient(seals.getAllAgents(), Task.Type.FLEE, 0.8) );

            fish.getParameters()
                    .tasker( new Tasker.ControlClosest(seals.getAllAgents(), Task.Type.CAPTURE, 1.0));
              fish.addAgentTasker( new Tasker.Gradient(penguins.getAllAgents(), Task.Type.FLEE, 0.8) );

            Simulation sim = new Simulation(name, seals, penguins, fish);
              logs.add(new TeamMetricLogger("Average distance (seals, penguins)", sim, TeamMetrics.AVERAGE_DISTANCE, seals, penguins));
              logs.add(new TeamMetricLogger("Average distance (seals, fish)", sim, TeamMetrics.AVERAGE_DISTANCE, seals, fish));
              logs.add(new TeamMetricLogger("Average distance (fish, penguins)", sim, TeamMetrics.AVERAGE_DISTANCE, fish, penguins));
            return sim;
        }
    },

    SAHARA("Sahara") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            AgentParameters lionPar = new AgentParameters()
                    .topSpeed(6.5)
                    .tasker(new Tasker.SpiralSearch());
            Team lions = new Team(4, new TeamParameters("Lions", Color.ORANGE, lionPar)
                    .locations(new LocationGenerator.RandomBox(50.0)));
            AgentParameters wildPar = new AgentParameters();
            Team wilds = new Team(4, new TeamParameters("Wildebeest", Color.GRAY, wildPar)
                    .locations(new LocationGenerator.RandomBox(50.0)));
            Obstacle target = new Obstacle("Watering Hole", Color.BLUE, 10, 10);

            lions.getParameters()
                    .capture(new Capture(lions, wilds, 1.0))
                    .victory(new VictoryCondition(lions, wilds, TeamMetrics.PERCENT_OPPONENTS_CAPTURED, ComparisonType.GREATER_EQUAL, 0.5));
            lions.addAgentTasker(new Tasker.Closest(wilds.getAllAgents(), Task.Type.CAPTURE, 1.0));
            lions.addAgentTasker(new Tasker.Follow(target, Task.Type.SEEK, 0.01));
            wilds.getParameters()
                    .capture(new Capture(wilds, target, 1.0, Capture.Action.SAFETY_OWNER))
                    .victory(new VictoryCondition(wilds, null, TeamMetrics.NUMBER_OF_AGENTS_SAFE, ComparisonType.GREATER_EQUAL, 2.0));
            wilds.addAgentTasker(new Tasker.Follow(target, Task.Type.SEEK, 1.0));
            wilds.addAgentTasker(new Tasker.Gradient(lions.getAllAgents(), Task.Type.FLEE, 1.0));

            return new Simulation(name, lions, wilds, target);
        }
    },

    FLOCKING("Flocking") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            AgentParameters flockPar = new AgentParameters()
                    .topSpeed(5.0)
                    .turnRadius(1.0)
                    .sensor(new Sensor.Radial(10.0))
                    .taskChooser(TaskChooser.GRADIENT_CHOOSER);
            Team flock = new Team(25,
                    new TeamParameters("Flock", Color.BLUE, flockPar)
                    .locations(new LocationGenerator.RandomBox(50.0)));

            AgentParameters predPar = new AgentParameters()
                    .topSpeed(6.0)
                    .turnRadius(2.0)
                    .commRadius(50.0)
                    .sensor(new Sensor.Radial(30.0))
                    .router(new Router.Leading())
                    .taskChooser(TaskChooser.GRADIENT_CHOOSER);
            Team predators = new Team(4,
                    new TeamParameters("Predators", Color.RED.darker(), predPar)
                    .locations(new LocationGenerator.RandomBox(50.0)));

            Team obstacles = new Team(0,
                    new TeamParameters("Obstacles", Color.DARK_GRAY, new AgentParameters())
                    .locations(new LocationGenerator.RandomBox(50.0)));
            for (int i = 0; i < 4; i++)
                obstacles.addComponent(new Obstacle());
            
            Obstacle origin = new Obstacle();

            flock.addAgentTasker(new Tasker.Closest(flock.getAllAgents(), Task.Type.FLEE, .5));
            flock.addAgentTasker(new Tasker.CenterOfMass(flock.getAllAgents(), Task.Type.SEEK, .7));
            flock.addAgentTasker(new Tasker.AverageHeading(flock.getAllAgents(), Task.Type.SEEK, .4));
            flock.addAgentTasker(new Tasker.Gradient(predators.getAllAgents(), Task.Type.FLEE, 1));
            flock.addAgentTasker(new Tasker.Closest(obstacles.getAllAgents(), Task.Type.FLEE, .2));
            flock.addAgentTasker(new Tasker.Follow(origin, Task.Type.SEEK, .9));

            predators.addAgentTasker(new Tasker.Closest(predators.getAllAgents(), Task.Type.FLEE, .5));
            predators.addAgentTasker(new Tasker.Closest(flock.getAllAgents(), Task.Type.CAPTURE, 1.0));
            predators.addAgentTasker(new Tasker.Closest(obstacles.getAllAgents(), Task.Type.FLEE, 0.2));
            
            predators.par.capture(new Capture(predators, flock, 1.0, Capture.Action.DEACTIVATE_TARGET));

            Simulation sim = new Simulation(name, flock, predators, obstacles);
              logs.add(new TeamMetricLogger("# Captures", sim , TeamMetrics.NUMBER_OPPONENTS_CAPTURED, predators, flock));
            return sim;
        }
    },
    
    SWALLOWED("The Big One") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            SimComponent[] teams = new SimComponent[10];
            String[] names = new String[]{ "Old Lady", "Horses", "Cows", "Goats", "Dogs", "Cats", "Birds", "Spiders", "Flies", "Why" };
            Color[] colors = new Color[]{ Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.GRAY, Color.BLACK };
            int[] sizes = new int[]{ 1, 4, 3, 5, 6, 3, 2, 4, 2, 2 };
            for (int i = 0; i < 10; i++) {
                AgentParameters par = new AgentParameters()
                        .sensor(Sensor.GLOBAL_SENSOR)
                        .taskChooser(TaskChooser.GRADIENT_CHOOSER)
                        .router(new Router.Leading());
                teams[i] = new Team(sizes[i],
                        new TeamParameters(names[i], colors[i], par)
                        .locations(new LocationGenerator.RandomBox(50))
                        .sensor(Sensor.GLOBAL_SENSOR)
                        );
            }
            for (int i = 0; i < 9; i++) {
                ((Team)teams[i]).par.tasker( new Tasker.ControlClosest(((Team)teams[i+1]).getAllAgents(), Task.Type.CAPTURE) );
                if (i != 8)
                    ((Team)teams[i+1]).addAgentTasker( new Tasker.Gradient(((Team)teams[i]).getAllAgents(), Task.Type.FLEE, 0.5) );
            }

            Simulation sim = new Simulation(name, teams);
            return sim;
        }
    },

    FOOTBALL("Football Simulation") {
        public Simulation getSimulation(List<SimulationLogger> logs) {
            Team endzone = new Team(new TeamParameters("Endzone A", Color.GREEN, new AgentParameters())
                    .locations(new LocationGenerator.Segment(new Point2D.Double(-50, 10), new Point2D.Double(-50, 90))) ) ;
            for (int i = 0; i < 10; i++) endzone.addComponent(new Obstacle());
            Team sideline1 = new Team(new TeamParameters("Sideline A", Color.ORANGE, new AgentParameters())
                    .locations(new LocationGenerator.Segment(new Point2D.Double(-45, 0), new Point2D.Double(45, 0))) );
            for (int i = 0; i < 10; i++) sideline1.addComponent(new Obstacle());
            Team sideline2 = new Team(new TeamParameters("Sideline B", Color.ORANGE, new AgentParameters())
                    .locations(new LocationGenerator.Segment(new Point2D.Double(-45, 100), new Point2D.Double(45, 100))) );
            for (int i = 0; i < 10; i++) sideline2.addComponent(new Obstacle());

            Team army = new Team(6,
                    new TeamParameters("Army", Color.BLACK, 
                    new AgentParameters().taskChooser(TaskChooser.GRADIENT_CHOOSER))
                    .locations(new LocationGenerator.Segment(new Point2D.Double(40, 15), new Point2D.Double(40, 85)))
                    .sensor(Sensor.GLOBAL_SENSOR) );
            army.getParameters()
                    .capture(new Capture(army, endzone, 1.0, Capture.Action.SAFETY_OWNER))
                    .victory(new VictoryCondition(army, null, TeamMetrics.NUMBER_OF_AGENTS_SAFE, ComparisonType.GREATER_EQUAL, 2));

            Team navy = new Team(6,
                    new TeamParameters("Navy", Color.BLUE, new AgentParameters().router(new Router.PluckerLeading()) )
                    .locations(new LocationGenerator.Segment(new Point2D.Double(-20, 30), new Point2D.Double(-20, 70)))
                    .sensor(Sensor.GLOBAL_SENSOR) );
            navy.getParameters()
                    .capture(new Capture(navy, army, 1.0))
                    .victory(new VictoryCondition(navy, army, TeamMetrics.NUMBER_OF_OPPONENTS_UNCAPTURED, ComparisonType.EQUAL, 0));

            army.addAgentTasker( new Tasker.Closest(sideline1.getAllAgents(), Task.Type.FLEE, .3) );
            army.addAgentTasker( new Tasker.Closest(sideline2.getAllAgents(), Task.Type.FLEE, .3) );
            army.addAgentTasker( new Tasker.Closest(navy.getAllAgents(), Task.Type.FLEE, .2) );
            army.getParameters().tasker( new Tasker.ControlClosest(endzone.getAllAgents(), Task.Type.CAPTURE, 1.0) );
            
            navy.getParameters().tasker( new Tasker.ControlClosest(army.getAllAgents(), Task.Type.CAPTURE, 1.0) );

            Simulation sim = new Simulation(name, army, navy, endzone, sideline1, sideline2);
            return sim;
        }
    };

    String name;
    SampleSims(String name) { this.name = name; }
    @Override public String toString() { return name; }

    /**
     * Sets up a simulation and returns the simulation.
     * @param logs a pointer to a collection of logs that may be updated w/ desired metrics
     * @return the simulation
     */
    public abstract Simulation getSimulation(List<SimulationLogger> logs);

    public static void main(String[] args) {            
        XMLEncoder en = new XMLEncoder(System.out);
        //en.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
        en.writeObject(COPS_ROBBERS.getSimulation(new ArrayList<SimulationLogger>()));
        en.flush();
    }
}
