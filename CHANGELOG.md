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