package io.github.rsk3110.riskgame.model.world.state;

import io.github.rsk3110.riskgame.model.Player;

import java.io.Serializable;
import java.util.Optional;

public final class TerritoryState implements Serializable {
    private int occupyingArmies;
    private Player occupyingPlayer;

    public TerritoryState() {
        this.occupyingArmies = 0;
    }

    public int getOccupyingArmies() {
        return this.occupyingArmies;
    }

    public void addOccupyingArmies(final int n) {
        if (n < 0)
            throw new IllegalArgumentException("cannot add negative number of armies to territory");
        else
            this.occupyingArmies += n;
    }

    public void removeOccupyingArmies(final int n) {
        if (n < 0)
            throw new IllegalArgumentException("cannot remove negative number of armies from territory");
        else if (this.occupyingArmies - n < 0)
            throw new IllegalArgumentException(String.format("tried to remove %d armies, but only have %d", n, this.occupyingArmies));
        else
            this.occupyingArmies -= n;
}

    public void moveOccupyingArmies(final int n, final TerritoryState otherTerritory) {
        this.removeOccupyingArmies(n);
        otherTerritory.addOccupyingArmies(n);
    }

    public Optional<Player> getOccupyingPlayer() {
        return Optional.ofNullable(this.occupyingPlayer);
    }

    public void setOccupyingPlayer(final Player occupyingPlayer) {
        this.occupyingPlayer = occupyingPlayer;
    }
}
