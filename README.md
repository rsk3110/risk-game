# risk-game
Sysc3110 Risk: Global Domination Project

Group members:
- Kaue Gomes e Sousa de Oliveira
- Mark Johnson
- Tooba Sheikh
- Gang Han

##Setup
As this is a Maven project, you must configure IntelliJ before building.

After opening the project, right-click on the project root and click 
````
Add Framework Support...
````
Then scroll down, select Maven, and click okay.

##Documentation
Documentation can be found at 'index.html' in the documentation folder.

The game board is defined in worlds/default.xml, and read and parsed into a World using WorldFileLoader.

Worlds consist of a graph of Territories and TerritoryEdges, a list of Continent objects, and a map of Territory objects to their set of TerritoryEdge objects.
The World does no more than keep track of the game board state (Continents/Territories and connections)

A Continent contains its name, color, army bonus for capturing, and contained Territories.
Continents serve as a reference to their geographical description (contained Territories) and army bonus values.

A Territory contains an id, name, Continent it's found within, and occupying Player.
Territories also serve as a reference to their geographical description (containing Continent and isNeighbor()),
as well as actors towards other Territories (moveArmy()) and Players (setOccupant() adds self to occupant Territories list)
Additionally, Territory is capable of converting into to an existing Territory through the static method idToTerritory().

A Player contains its name, occupied Territories, its unallocated armies, and a reference to the game World.
Through the World field, Player keeps reference to the physical state of the game. Players also serve as actors towards
Territories (allocateArmies()).

Commands are structured such that they implement the Command interface to standardize their execution methods.
All command types can either execute a command with no arguments or with arguments, however not all do anything useful.
(For example, AttackCommand with no arguments just complains to the user)

CommandManager tracks the initialized Commands mapped to their names, and executes specified commands if they are found in the map.
It slices the command input and executes it, passing in any arguments it splits out.

Game initializes the Players and World, and controls the game flow through the play() method. At the beginning of each Player's
turn, Game executes the Player#updateArmies method, takes in player army allocation input, and calls Player#allocateArmies with
the given Territory. Afterwards, it prompts for commands, cycling through the Players when appropriate. At the end of each
Player's turn, the game checks for any game ending conditions. 