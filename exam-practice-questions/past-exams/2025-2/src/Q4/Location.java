import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


public class Location {
    String type;
    String name;
    Integer population = null;
    public final Set<Location> children = new LinkedHashSet<>();

    public Location(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Location(String type, String name, Set<Location> children) {
        this.type = type;
        this.name = name;
        this.children.addAll(children);
    }

    public void addChild(Location child) { children.add(child); }


    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location that)) return false;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }
    @Override public int hashCode() {
        return Objects.hash(type, name); }

    @Override public String toString() {
        return type + ":" + name + (population != null ? " (" + population + ")" : "");
    }
}
