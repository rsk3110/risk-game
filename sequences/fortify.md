title Fortify Command


participant p [Player]
participant fc [FortifyCommand]
participant w [World]
participant origin [Territory]

p [Player]->fc [FortifyCommand]: execute(this, args)

activate fc [FortifyCommand]

fc [FortifyCommand]->w [World]: idToTerritory(args[0])
fc [FortifyCommand]<<--w [World]: origin [Territory]

fc [FortifyCommand]->w [World]: idToTerritory(args[1])
fc [FortifyCommand]<<--w [World]: target [Territory]

fc [FortifyCommand]->origin [Territory]: moveArmy(numArmies, target)
deactivate fc [FortifyCommand]
