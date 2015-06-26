# Definitions #

  * **Parameters** are fixed throughout a simulation. They may be changed by the user, causing a completely new simulation to be run.
  * **Algorithm Parameters** determine the methods use to adjust the control variables.
  * **State Variables** store an agent's state at a given step in the simulation, probably including position and velocity.
  * **Control Variables** are those that a player can change. These directly impact the player's state, in a manner probably determined by the parameters.

Apart from these:
  * **Visual Parameters** are parameters that do not impact the underlying mathematics, the result of the simulation.
  * **Utilities** can be used to generate initial positions for the agents, etc. They control how specific parameters are determined prior to the simulation.

### Comments ###
  * The **parameters** are ALL that is needed to _RECONSTRUCT_ a simulation. The **state variables** and **control variables** are used _DURING_ the simulation.
  * The **parameters** are what needs to be saved to disk to reload the simulation.


# Listing of Parameters and Variables #

## Agent ##

  * **Locational**: starting location, top speed, turning radius _(parameters)_ ; active status, position, velocity _(states)_ ; change in heading/direction _(control)_
  * **Sensory**: sensor radius, communications radius _(parameters)_ ; visible opponent locations, communicated opponent locations _(states)_ ; sensor fusion _(algorithm)_
  * **Behavioral**: task list _(state)_ ; task generator, task prioritizer, task implementer/behavior (_algorithms_)
  * **Visual**: color, path style, agent marker style, etc.

## Team ##

  * **Compositional**: size, agents, default agent parameters, initially active agents _(parameters)_
  * **Sensory**: team sensing capability _(parameter)_; team-visible opponent locations _(state)_
  * **Behavioral**: team task generator, capture condition, victory conditions/goals _(algorithms)_
  * **Visual**: default color, style, etc.

## Simulation ##

  * _Parameters_: maximum simulation time, time delta, obstacles, teams
  * _State variables_: current time


# Utility Algorithms #

Some additional algorithms are used to help generate parameters for teams and for agents. These can be used prior to a simulation's start to set certain parameters, but by themselves they aren't really parameters of particular players, teams, or the simulation. Here they are:

  * **Locational**: starting location generator (_algorithm_)
  * **Activator**: sets initially active agents (_algorithm_)