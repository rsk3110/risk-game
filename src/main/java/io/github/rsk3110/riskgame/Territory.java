package io.github.rsk3110.riskgame;

import java.io.Serializable;
import java.util.Objects;

public final class Territory implements Serializable {

    private final int id;
    private String name;
    private Continent continent;
    private int armies; // holds how many armies each team has

    public Territory(final int id) {
        this.id = id;
        this.armies = 0;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    //set the number of armies
    public void setArmies(int armies){
        this.armies = armies;
    }

    //get the number of armies
    public int getArmies(){
        return this.armies;
    }

    public void setContinent(final Continent continent) {
        this.continent = continent;
    }

    public Continent getContinent() {
        return this.continent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Territory that = (Territory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Territory{" +
                "name='" + name + '\'' +
                ", continent=" + continent +
                '}';
    }
}
