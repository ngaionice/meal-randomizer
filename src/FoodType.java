import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class FoodType implements Serializable {

    private final StringProperty name;
    private final ListProperty<String> foods;

    FoodType(String name) {
        this.name = new SimpleStringProperty(name);
        this.foods = new SimpleListProperty<>();
    }

    public final StringProperty NameProperty() {
        return name;
    }

    public final ListProperty<String> FoodsProperty() {
        return foods;
    }

    public final String getName() {
        return name.toString();
    }

    public final String getFoodAtIndex(int index) {
        if (foods.size() > index) {
            return foods.get(index);
        }
        return "Bad input";
    }

    public final int getSize() {
        return foods.size();
    }

    void addFood(String name) {
        foods.add(name);
    }

    void deleteFood(String name) {
        foods.remove(name);
    }
}
