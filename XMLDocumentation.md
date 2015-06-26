# Introduction #
This space describes the XML format for PursuitEvasion simulation files. Since the program is always changing, this may change somewhat over time.

# Overview #
The basic structure of the XML file follows. This should be saved in a `.xml` file.
```
<simulation OPTIONS>
  <team OPTIONS>
    <agent OPTIONS />
    ...
    <capture OPTIONS />
    ...
    <victory OPTIONS />
    ...
    <metric OPTIONS />
    ...
    <tasking OPTIONS />
    ...
  </team>
  ...
</simulation>
```

The `simulation` and `team` tags are full blocks, while the `agent`, `capture`, `victory`, `tasking`, and `metric` tags have options only. All tags may be used multiple times, except for the `simulation` tag. The full descriptions of the options are in the next section.

# Tag attributes #
## Simulation ##
```
<simulation stepTime="0.1" pitchSize="60.0" numSteps="250" name="Football Game" maxSteps="1000">
```

  * `name`: a name for the simulation (required; no default)
  * `numSteps`: the default number of steps in the simulation (default 250)
  * `maxSteps`: the maximum number of steps allowed if the simulation runs until a goal is achieved (default 1000)
  * `stepTime`: the time elapsed per step (default .1)
  * `pitchSize`: the area used to generate random players (default 60)

## Team ##

```
<team topSpeed="5.0" sensorRange="20.0" name="Sidelines" leadFactor="0.0" commRange="50.0" color="ff0000" behaviorCode="0" agentNumber="20">
```

  * `name`: a name for the team (required; no default)
  * `size`: number of agents on the team (default 0)
  * `color`: color used to represent the team, in hex (default gray, eg. `color="ff0000"`)
  * `topSpeed`: the maximum movement per unit time (default 5.0)
  * `sensorRange`: the range of sensing opponents (default 20.0)
  * `commRange`: the range of communications (default 50.0)
  * `behaviorCode`: integer code representing behavior (default 0; see table below)
  * `leadFactor`: the lead factor to use as required by the specific behavior (default 0.5)

_UNSUPPORTED ATTRIBUTES: `startCode`, `parametricPath`_

##### Team/Agent Behavior Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | Stationary  |
| 1         | Head straight to/from target |
| 2         | Head straight from/to target (direction swapped) |
| 3         | Leading; aim in front of target |
| 4         | Plucker leading; aim in front of target (slightly different algorithm) |
| 5         | Large circle search |
| 6         | Small circle search |
| 7         | Large quadrant search |
| 8         | Small quadrant search |
| 9         | Approach a path |
| 10        | _Follow a randomly generated path (UNSUPPORTED)_ |
_Behaviors 5-8 follow a search pattern if there is no opponent in sight; and otherwise follow a leading behavior._


## Agent ##
  * `name`: a name for the player (required; no default)
  * `color`, `topSpeed`, `sensorRange`, `commRange`, `behaviorCode`, `leadFactor`: specific values for the player to override the team settings (defaults same as for team)
  * `x` and `y`: the coordinates of the player (default 0,0)

## Metric ##
Defines a metric used for simulation statistics.
  * `target`: the target team, given by the team `name` attribute (required; no default)
  * `type`: code for the particular metric algorithm (default 0; see table below)
  * `threshold`: threshold for the capture, e.g. within 2 units implies captures (default 0)
  * `cooperationOn`: boolean used to determine whether statistics should include the cooperative value of this particular metric (default false)

##### Metric Type Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | Minimum distance to targets |
| 1         | Maximum distance to targets |
| 2         | Average distance to targets |
| 3         | Opposing agent which is closest to capture |
| 4         | Number of active agents on own team |
| 5         | Number of active opponents |
| 6         | Advantage in numbers over the opponent |
| 7         | Number on team that are "safe" |
| 8         | Number of opponents that are "safe" |
| 9         | Number of opponents that have been captured |
| 10        | Percentage of opponents that have been captured |
| 11        | Number of opponents that have not been captured |
| 12        | Number of potential captures that have not been made |
| 13        | Total time of the simulation |
| 14        | Time elapsed since the last capture |

### Capture ###
Represents a metric used to define when to remove a player from the game. Has all the properties of a `metric` plus:
  * `removalCode`: code describing whom to remove from the game after a capture (default 0; see table below)

##### Capture Action Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | Remove both agents involved in capture |
| 1         | Remove only the target (agent that is captured) |
| 2         | Remove only the agent that captures |

### Victory ###
Represents a metric used to assess victory or progress toward victory. Has all the attributes of `Metric` plus:
  * `eventLessCode` and `eventGreaterCode`: determine whether victory is achieved or lost depending on the relative value of the metric versus the threshold (default 0,0; see table below)
  * `gameEnding`: boolean determining whether the game should end when this metric is tripped (default false)

##### Victory Status Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | Indicates neither a win nor a loss |
| 1         | Indicates a win |
| 2         | Indicates a loss |

## Tasking ##
Gives an algorithm that is used to generate movement for the team. Multiple taskings are supported and integrate using a weighted gradient technique.
  * `type`: code for the particular tasking algorithm (default 0; see table below)
  * `target`: the target team, given by the team `name` attribute (required; no default)
  * `algorithm`: code for the precise algorithm (default 0)
  * `taskType`: basically determines whether minimizing the distance is a good thing, generally pursuit, or a bad thing, generally evasion (default 0)
  * `weight`: used to weight multiple taskings (default 1.0)

_UNSUPPORTED ATTRIBUTES: `threshold`_

##### Tasking Algorithm Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | No tasking done |
| 1         | _Autonomous;_ assigns closest target |
| 2         | _Autonomous;_ assigns point along line between the two closest targets |
| 3         | _Autonomous;_ assigns farthest target |
| 4         | _Autonomous;_ assigns average position (center-of-mass) of targets |
| 5         | _Autonomous;_ assigns average direction of targets |
| 6         | _Autonomous;_ assigns direction of maximum increase of distance to targets (via gradient) |
| 7         | _Control;_ assigns targets using a greedy algorithm |
| 8         | _Control;_ assigns targets using linear program to optimize sum of assignment distance |

##### Tasking Type Codes #####
| **Value** | **Meaning** |
|:----------|:------------|
| 0         | Seek        |
| 1         | Capture     |
| 2         | Flee        |

# Sample XML Files #

## A Two-Team Scenario ##

```
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?> 
<simulation stepTime="0.1" pitchSize="60.0" numSteps="250" name="2 Team (Cops & Robbers)" maxSteps="1000">
 <team topSpeed="6.0" sensorRange="20.0" name="Cops" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" size="5">
  <capture removalCode="0" type="0" threshold="5.0" target="Robbers" cooperationOn="false" /> 
  <victory eventGreaterCode="0" eventLessCode="1" gameEnding="false" type="5" threshold="2.0" target="Robbers" cooperationOn="false" /> 
  <metric type="2" threshold="0.0" target="Robbers" cooperationOn="false" /> 
  <agent y="17.41848056320532" x="7.012919933133404" topSpeed="6.0" sensorRange="20.0" name="C1" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" /> 
  <agent y="-41.624558669561694" x="-55.800242065001655" topSpeed="6.0" sensorRange="20.0" name="C2" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" /> 
  <agent y="-29.92705306911088" x="59.2842175539607" topSpeed="6.0" sensorRange="20.0" name="C3" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" /> 
  <agent y="-42.594803320797546" x="-50.59954274192023" topSpeed="6.0" sensorRange="20.0" name="C4" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" /> 
  <agent y="35.42678444582447" x="-54.17007051943155" topSpeed="6.0" sensorRange="20.0" name="C5" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" /> 
  <tasking weight="1.0" threshhold="1.0" taskType="1" target="Robbers" algorithm="7" /> 
 </team>
 <team topSpeed="5.0" sensorRange="20.0" name="Robbers" leadFactor="0.5" commRange="50.0" color="ffc800" behaviorCode="1" size="4">
  <agent y="-6.344147075652636" x="-21.935229115632225" topSpeed="5.0" sensorRange="20.0" name="R1" leadFactor="0.5" commRange="50.0" color="ffc800" behaviorCode="1" /> 
  <agent y="-5.111052925336395" x="-2.2607997892695337" topSpeed="5.0" sensorRange="20.0" name="R2" leadFactor="0.5" commRange="50.0" color="ffc800" behaviorCode="1" /> 
  <agent y="18.517656529054676" x="-27.51103319412136" topSpeed="5.0" sensorRange="20.0" name="R3" leadFactor="0.5" commRange="50.0" color="ffc800" behaviorCode="1" /> 
  <agent y="49.23535607937602" x="-6.718613918274649" topSpeed="5.0" sensorRange="20.0" name="R4" leadFactor="0.5" commRange="50.0" color="ffc800" behaviorCode="1" /> 
  <tasking weight="1.0" threshhold="1.0" taskType="2" target="Cops" algorithm="6" /> 
 </team>
</simulation>
```

## A Three-Team Scenario ##
```
<simulation stepTime="0.1" pitchSize="60.0" numSteps="250" name="3 Team (Antarctica)" maxSteps="1000">

<team topSpeed="5.0" sensorRange="20.0" name="Seals" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3" size="3">
<agent y="-50.733432751507834" x="-42.41517300965897" topSpeed="5.0" sensorRange="20.0" name="S1" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3"/>
<agent y="21.291463189614575" x="42.07915861845581" topSpeed="5.0" sensorRange="20.0" name="S2" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3"/>
<agent y="-14.843661781200701" x="17.569507761819295" topSpeed="5.0" sensorRange="20.0" name="S3" leadFactor="0.5" commRange="50.0" color="0000ff" behaviorCode="3"/>
<tasking weight="1.0" threshhold="1.0" taskType="1" target="Penguins" algorithm="7"/>
<tasking weight="0.95" threshhold="1.0" taskType="2" target="Fish" algorithm="6"/>
</team>

<team topSpeed="5.0" sensorRange="20.0" name="Penguins" leadFactor="0.5" commRange="50.0" color="000000" behaviorCode="3" size="4">
<agent y="-54.69505273781245" x="31.548768245056806" topSpeed="5.0" sensorRange="20.0" name="P1" leadFactor="0.5" commRange="50.0" color="000000" behaviorCode="3"/>
<agent y="42.299659619074646" x="7.978848039817919" topSpeed="5.0" sensorRange="20.0" name="P2" leadFactor="0.5" commRange="50.0" color="000000" behaviorCode="3"/>
<agent y="51.84815248987161" x="20.035938206391407" topSpeed="5.0" sensorRange="20.0" name="P3" leadFactor="0.5" commRange="50.0" color="000000" behaviorCode="3"/>
<agent y="-17.504052330406296" x="29.76474564972115" topSpeed="5.0" sensorRange="20.0" name="P4" leadFactor="0.5" commRange="50.0" color="000000" behaviorCode="3"/>
<tasking weight="0.95" threshhold="1.0" taskType="2" target="Seals" algorithm="6"/>
<tasking weight="1.0" threshhold="1.0" taskType="1" target="Fish" algorithm="7"/>
</team>

<team topSpeed="5.0" sensorRange="20.0" name="Fish" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1" size="5">
<agent y="32.32308482769969" x="-51.53450988532716" topSpeed="5.0" sensorRange="20.0" name="F1" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1"/>
<agent y="-50.1878695200805" x="59.32398883186707" topSpeed="5.0" sensorRange="20.0" name="F2" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1"/>
<agent y="46.18597519489856" x="-47.05570361112271" topSpeed="5.0" sensorRange="20.0" name="F3" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1"/>
<agent y="53.40819946493224" x="-33.92189253500973" topSpeed="5.0" sensorRange="20.0" name="F4" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1"/>
<agent y="30.532306650894228" x="-52.63568325335225" topSpeed="5.0" sensorRange="20.0" name="F5" leadFactor="0.5" commRange="50.0" color="00ff00" behaviorCode="1"/>
<tasking weight="0.95" threshhold="1.0" taskType="2" target="Penguins" algorithm="6"/>
<tasking weight="1.0" threshhold="1.0" taskType="1" target="Seals" algorithm="7"/>
</team>
</simulation>
```