import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public final class Continent implements Serializable {
    private final String name;
    private final Color color;
    private final int bonusArmies;

    public Continent(final String name, final Color color, final int bonusArmies) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonusArmies;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getBonusArmies() {
        return this.bonusArmies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Continent continent = (Continent) o;
        return Objects.equals(name, continent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Continent{" +
                "name='" + name + '\'' +
                ", bonusArmies=" + bonusArmies +
                '}';
    }
}
