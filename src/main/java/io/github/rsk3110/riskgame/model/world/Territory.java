package io.github.rsk3110.riskgame.model.world;

import java.io.Serializable;
import java.util.Objects;

public final class Territory implements Serializable {
    private final int id;
    private String name;
    private Continent continent;

    public Territory(final int id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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
