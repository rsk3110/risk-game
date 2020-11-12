title Attack Command

participant p [Player]
participant ac [AttackCommand]
participant w [World]
participant origin [Territory]
participant target [Territory]

p [Player]->ac [AttackCommand]: execute(this, args)

activate ac [AttackCommand]

ac [AttackCommand]->w [World]: idToTerritory(args[0])
ac [AttackCommand]<<--w [World]: origin [Territory]

ac [AttackCommand]->w [World]: idToTerritory(args[1])
ac [AttackCommand]<<--w [World]: target [Territory]

ac [AttackCommand]->ac [AttackCommand]: getDiceValues(player)
ac [AttackCommand]->ac [AttackCommand]: getDiceValues(target.getOccupant())

loop for sorted die pairs

alt attack <= defence
ac [AttackCommand]->origin [Territory]: decrementArmies();
else attack > defence
ac [AttackCommand]->target [Territory]: decrementArmies();

opt target.getArmies() == 0
ac [AttackCommand]->target [Territory]: setOccupant(player)
ac [AttackCommand]->otigin [Territory]: moveArmies(this.getNumArmyToMove())
end

end

end

deactivate ac [AttackCommand]
