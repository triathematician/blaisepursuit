# Running a simulation #
The class `Simulation` handles the primary task of running a simulation. The primary method to call is the `run()` method, which has three main steps: a `preRun()` step, an `iterate()` step (called multiple times), and a `postRun()` step. A special method `isFinished()` is used to determine when the simulation concludes. I also have built in a special exception, `SimulationTerminatedException` Here is the source code for the `run()` method:

```
public final void run() {
    preRun();
    try {
        while (!isFinished()) {
            iterate();
        }
    } catch (SimulationTerminatedException ex) {
        System.out.println("Victory achieved by one of the teams! ("+ex.explanation+")");
    }
    postRun();
}
```

Some comments on this:
  * The `preRun()` method currently calls the `initStateVariables()` method, which sets up the simulation for running.
  * The `isFinished()` method describes the default end-of-time for the simulation... currently set up to terminate after a fixed period of time.
  * The `iterate()` method encodes the main iteration structure, and is described further below.
  * The `postRun()` method is called after the simulation has ended. Currently, nothing of interest is done here.
  * The `SimulationTerminatedException` may be called within the `iterate()` method, and allows intermediate methods to also terminate the simulation, escaping out of the iteration phase (e.g. a special victory condition).

# Composite Structure of Simulations #

Simulations are comprised of `SimulationComponent`s, which might be agents, obstacles, or teams of agents.

# Iteration Structure Steps (new version) #

Generally speaking, pursuit/evasion algorithms between multiple players contain three steps: MTT (multiple target tracking), PEA (pursuer-evader assignment), and PP (path planning).

In the new version, these are implemented within the `Simulation.iterate()` method. Here is the detailed description of that method:
  1. **DISTANCE CALCULATION**
    * Recalculate current time `curTime` and distance cache table `dt`. The distance cache is used for purposes of speed to store the true distances between each pair of agents.
  1. **CAPTURE CHECKING**
    * Call `handleMajorEvents(dt, curTime)`. There should handle major events like one team capturing another.
  1. **VICTORY CHECKING**
    * Check to see if any of the simulation's components have achieved victory, by calling `checkVictory(dt, curTime)`. This method may throw a `SimulationTerminationException`, causing the simulation to finish early.
      * Teams call `VictoryCondition.hasBeenMet(dt,curTime)` (if they have a victory condition) to see whether victory has been achieved.
  1. **DATA LOGGING**
    * Notify any loggers to store the current status of the simulation... the method is `fireIterationEvent(curTime)`, and interested listeners will be passed both the distance table and the time.
  1. **MULTIPLE TARGET TRACKING (MTT)**: a combination of communicated locations and sensing locations
    * Components process any incoming communications events, `processIncomingCommEents(curTime)`.
    * Components gather data using their sensors, `gatherSensoryData(dt)`. The sensors are responsible for using the information in the distance table to return the agents that are "visible" to them.
      * Agents and teams call the `Sensor.findAgents(DistanceCache, position, velocity)` method to populate their table of visible opponents.
    * Components consolidate comm information and sensory information in the `developPointOfView()` method.
      * Agents populate their point-of-view table describing their beliefs about opponent locations.
  1. **PURSUER-EVADER ASSIGNMENT (PEA)**: using a tasking system whereby teams or agents create tasks for one or more other agents
    * Components generate tasks, which may be for themselves or for fellow players, in the `generateTasks(dt)` method.
      * Calls the `TaskGenerator.generateGiven(DistanceCache, owner, opponents, teammates)` method to generate tasks based upon the current point-of-view.
  1. **PATH-PLANNING (PP)**
    * Components set their _control variables_ in the `setControlVariables(curTime, timePerStep)` method.
      * Selection of desired task to follow in the `TaskPrioritizer.chooseTask(tasks)` method... this may return a task that represents a hybrid of multiple tasks.
      * Implementation of that task in the `TaskImplementer.getDirectionFor(task)` method. This is where different strategies, such as leading algorithms, may occur.
    * Components move their positions using the `adjustState(timePerStep)` method, which alters their current position/velocity based upon the control variables.
  1. **FINAL COMMUNICATIONS**
    * Components send out communications events in the `sendAllCommEvents(curTime)` method. The intention here is e.g. for them to communicate about the locations of agents they see to their teammates.