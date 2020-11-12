title Player Game Loop

participant g [Game]
participant p [Player]
participant cm [CommandManager]

opt player.getTerritories().size() == 0
note over g [Game]: run next player's turn

end

g [Game]->p [Player]: updateArmies()
g [Game]->g [Game]: runArmyAllocation(player)

loop command did not end turn

g [Game]->cm [CommandManager]: handleInput(player)
g [Game]<--cm [CommandManager]: did command end turn
g [Game]->g [Game]: checkIfOver()
end