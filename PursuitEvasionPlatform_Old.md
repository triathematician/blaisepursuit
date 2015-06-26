In the old version, these were implemented in the steps of the main iteration of the pursuit simulation, the `Simulation.iterate(double time)` method.

  1. **DISTANCE COMPUTATION**.
    * Recompute distances between all players (stored in **`DistanceTable`** class)
  1. **CAPTURE CHECK**.
    * Check for any captures that may have occurred (as registered by any team's **`CaptureCondition`** distance threshold criterion). Depending on the `removal` setting, remove one or both of the pursuer and evader. The closest players are removed first.
      * _`CaptureCondition.check(DistanceTable dt, SimulationLog log, CaptureMap cap, double time)`_
    * Create a **`SignificantEvent`** and mark the capture point on the playing field.
      * _`SimulationLog.logCaptureEvent(Team owner, Agent first, Team target, Agent second, String string, DistanceTable dt, double time)`_
    * Store the capturing players and the capture time, for later use in computing metrics for the teams or partial teams.
      * _`CaptureMap.logCapture(Agent agent, Agent target, double time)`_
  1. **VICTORY CHECK**.
    * Check for any "victory" that has occurred (as registered by a team's **`VictoryCondition`** class).
      * _`int VictoryCondition.check(DistanceTable dt, SimulationLog log, CaptureMap cap, double time)`_
    * The check may return "victory", "defeat", or "neither", obtained by comparing the valuation (from the parent class) to the given threshold.
      * _`Valuation.getValue(DistanceTable dt, CaptureMap cap)`_
    * If the check shows that the game should end, whether in victory or defeat, log an event to note this.
      * _`SimulationLog.logEvent(Team owner, Agent first, Team target, Agent second, String string, double time)`._
  1. **MULTIPLE TARGET TRACKING (MTT)**.
    * Active players _sense_ their environment, by adding all agents in their sensory radius to their `Vector<Agent> pov`.
      * _`Team.gatherSensoryData(DistanceTable dt)`_
      * _`Agent.gatherSensoryData(DistanceTable dt)`_
    * Active players _communicate_ with teammates in their communications radius about what they see, by adding all sensed agents to their teammates `Vector<Agent> commpov`.
      * _`Team.communicateSensoryData(DistanceTable dt)`_
      * _`Agent.generateSensoryEvents(Team team,DistanceTable dist)`_
      * _`Agent.acceptSensoryEvent(Collection<Agent> agents)`_
    * Active players _fuse_ their own perceptions with those communicated by others to form a belief about what's really out there, currently by simply adding all agents in `commpov` to those in `pov`.
      * _`Team.fuseAgentPOV()`_
      * _`Agent.fusePOV()`_
  1. **TASKING / PURSUER-EVADER ASSIGNMENT (PEA)**.
    * Each team handles the assignment problem by generating _control tasks_ using the "control agent" `taskings` and having each active player also generate _autonomous tasks_. Players have collections of **`Tasking`** objects, each of which wraps up a **`TaskGenerator`** object that is responsible for assigning agents **`Task`**s based on the current field of play. Each **`Task`** contains a `priority` that is later used by the agent to decide the ultimate action.
      * _`Team.assignTasks(DistanceTable dt)`_
      * _`Agent.generateTasks(Team team, DistanceTable dt, double priority)`_
      * _`TaskGenerator.generate(Collection<Agent> team, DistanceTable dt, double priority)`_
      * _`new Task(TaskGenerator source, V2 target, int type, double priority)`_
      * _`Agent.assign(Task t)`_
  1. **TASK FUSION / PATH PLANNING (PP)**.
    * The active players plan their paths based on the assigned tasks. Each **`Task`** leads to a desired direction of travel (a **`V2`**), with the conversion happening within a **`Behavior`** class. So _behaviors_ correspond to leading strategies, search strategies, etc.
      * _`Team.planPaths(double time, double stepTime)`_
      * _`Agent.planPath(double time, double stepTime)`_
      * _`R2 TaskFusion.getVector(Agent agent, Vector<Task> tasks, double time)`_
      * _`R2 Behavior.direction(Agent self, V2 target, double time)`_
  1. **MOVEMENT**.
    * Active agents change their locations based on previously assigned directions.
      * _`Team.move(double stepTime)`_
      * _`Agent.move(double stepTime)`_