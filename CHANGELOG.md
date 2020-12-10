# Milestone 1 -> Milestone 2
## Notes
 - Moved the user-facing UI code out of the game model and into its own set of view classes
   - UI is now Swing-based instead of textual, with the map represented visually as a graph
   - Game model classes now have listener attachment methods to allow the new view classes to be decoupled
   from the model
   - Controller class was implemented as a layer between the view and the model when propagating changes
 
 - Implemented a set of unit tests for the game models
   - Player, Territory, Help, Map, Skip, Quit, Continent are comprehensively-covered
   - Fortify and Attack have some known test failure caused by NPEs, which are being looked into
   
- Added documentation to the new Swing UI code

## Design Details
The first milestone already used a fairly sophisticated graph datastructure when it came to representing
the state of the RISK world, so when it came to migrating from the CLI to a GUI, few internal changes needed
to be made. The UI code itself is radically different from the previous milestone though, with much of the
command-parsing (and arguably the command system in general) becoming unnecessary.

Since the game model is not directly coupled or modified by the UI code anymore, the application architecture has been
redesigned from pull-style (asking objects for data) to push-style (being told by objects when there is data), allowing updates
to the UI to be quick and efficient.

# Milestone 2 -> 3
## Notes
- Implemented GameController and moved CommandManager logic inside.
    - Deleted CommandManager entirely as the uniform string argument model became obsolete.

- Added Bonus Armies Phase and Troupe Movement Phase
    - Bonus armies are assigned at start of turn, user is prompted to allocate them.
    - Troupe Movement Phase consists of Fortify, terminating after being executed.

- Fixed Attack input not being prompted

- Updated unit tests to reflect model changes
    - AttackCommand, FortifyCommand

## Design Details
The Bonus Armies Phase takes place at the beginning of the turn as long as one round has passed in its entirety,
requiring the tracking of elapsed rounds in Game. As mentioned in `Milestone 1 -> 2`, the Command system became
obsolete as no manual user parameter intake and parsing is required anymore.

# Milestone 3 -> 4
## Notes
- Move AI logic into AI class that extends Player.
- Add Save/Load classes for saving and loading functionality.

## Design Details
"Used serializable to save and load the game state.

Created a return gameState method in the main game class. 
It adds all the serializable variable (states of the game) into a map then returns the map.
The reason to store all objects in a map before serializing the map is avoid having to serialize each variable on its own. 
This method was added because it was needed to record the game state and send it if other classes need it.

Created a Save class.
It contains a method called saveGame(gameState, fileName). Basically serializes the gameState Map into a file with name "fileName"
It also contains execute method which gets the gameState from main and then calls saveGame to serialize it.
The reason method saveGame has parameter for fileName is if at anypoint we want to add feature for multiple save files. 

Created a Load Class
It contains a method called loadGame(fileName). Basically deserializes the gameState from file with name "fileName" into an object that is returned.
The reason method loadGame has parameter for fileName is if at anypoint we want to add feature for multiple loadable files.

Created a loadState method in the main game class.
It basically sends the filename that is to be deserialized to Load class then using the returned object it sets all the states in the game from the object.
The returned object is cast as an Arraylist holding other objects.

Added GUI buttons in the same format as other buttons. There are try and catch statements to check if the load/save file doesn't exsit/is unsuccesfull. 
The reason to to create seperate classes for save and load rather than putting them in main class is to minimize the impact of them on other classes and make the program more modular. 
Not only easy to edit and understand, but also easy to merge." - Tooba

"Moved AI logic into an AI class to make initial player assignment simple, and to shift the AI name from being the only identifier. AI play logic was moved
into its play() method to isolate it from Game logic. Refactored save/load logic to use a Map instead of an Arraylist for better explicit tracking of serialized
variables when importing save." - Kaue