#### July 2009 ####
  * _Created starting position interface and created several implementations as separate classes._ --EP

#### May 2009 ####
  * _Adjusted main window to allow for multiple simulation panels._ --EP
  * _Added option to display agents as moving triangles._ --EP
  * _Made significant update to wiki entry on the iteration process._ --EP

#### Mar 2009 ####
  * _Added ability to add/remove items from the simulation via the tree (almost complete)._ --EP
  * _Added detailed description of XML format to this wiki._ --EP
  * _Added options for display style of solution paths, and point displays._ --EP
  * _Agent labels now use the first letter of the team name plus a number._ --EP
  * _Added victory win/loss settings into the settings panel._ --EP

#### Feb 2009 ####
  * _Added new simulation that implements a flocking algorithm._ --EP

#### Jan 2009 ####

  * _Fixed valuations for subset perceptions for various metrics. A few still need some changes, as annotated in code._ --EP

#### Nov 2008 ####

  * _Added in 3D visualization of pursuit games (with time on the vertical axis)._ --EP
  * _Adjusted leading algorithm; added in a "Plucker Lead" algorithm. Each predicts the "future point" in a slightly different way._ --EP
  * _Adjusted Jurassic simulation to bind all the points to the same underlying model, allowing them all to be used at once._ --EP

#### Oct 2008 ####

  * _Now have the ability to save and load simulations via menu options. The simulations are stored as XML files and can be easily edited without the simulation._ --EP

#### Sept 2008 ####

  * _Data log now includes starting positions of each team._ --EP
  * _One can now output an XML file that contains all simulation settings (using JAXB). This feature will be later extended to allow saving and loading of simulations._ --EP

#### June 2008 ####

  * _Moved labels from the plots themselves to a legend box which can be moved around the screen._ --EP
  * _Added labels to metrics window display & synchronized timers between the two windows. Also added display of points to animating paths. The slider at the bottom of the page can be used to control the animation when the animation is playing/paused._ --EP
  * _Can now output starting locations with new button in the application window titled **GET START POSITIONS**. The locations are given in the **Log** window and the **Code** window. The custom starting locations may be saved by three steps shown below._ --EP
    1. Register a new `static int` for the simulation in the `SimulationFactory` file
```
    public static final int CUSTOM_START_SAHARA = 6;
```
    1. Add a `String` in the proper location to the `GAME_STRINGS` array in the same file
```
    public static final String[] GAME_STRINGS = {
        "Follow the Light", "Cops & Robbers", "Antarctica",
        "Sahara", "Swallowed", "Jurassic",
        "Custom Start Sahara", "Custom"};
```
    1. Copy the code snippet under the **Code** window in the application, and paste it under a case statement in the `getTeams` method, still in the `SimulatonFactory` file:
```
    case CUSTOM_START_:
         Vector<Team> teams = appropriateSimulation();
         --paste code here--
         return teams;
```

  * _Updated the taskings settings window to permit the opponent to be changed._ --EP