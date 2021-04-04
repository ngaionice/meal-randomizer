import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;

public class DataModel implements Serializable {

    private final ObservableMap<String, ObservableList<String>> foods = FXCollections.observableHashMap();

    void addFoodType(String type) {
        foods.put(type, FXCollections.observableArrayList());
    }

    void deleteFoodType(String type) {
        foods.remove(type);
    }

    void addFoodToType(String type, String food) {
        if (foods.containsKey(type)) {
            foods.get(type).add(food);
        } else {
            System.out.println("Bad type");
        }
    }

    void deleteFoodFromType(String type, String food) {
        if (foods.containsKey(type)) {
            foods.get(type).remove(food);
        } else {
            System.out.println("Bad type");
        }
    }

    ObservableList<String> getFoodTypes() {
        return FXCollections.observableArrayList(foods.keySet());
    }

    ObservableList<String> getFoodOfType(String type) {
        return foods.get(type);
    }

    String getFoodAt(String type, int index) {
        if (foods.containsKey(type) && foods.get(type).size() > index) {
            return foods.get(type).get(index);
        }
        return "Bad input";
    }

    int getNumberOfChoices(String type) {
        if (foods.containsKey(type)) {
            return foods.get(type).size();
        }
        return 0;
    }


 }
