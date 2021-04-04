import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModel implements Serializable {

    private ObservableMap<String, ObservableList<String>> foods;

    DataModel(ObservableMap<String, ObservableList<String>> data) {
        foods = data;
    }

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

    public Map<String, List<String>> export() {
        Map<String, List<String>> data = new HashMap<>();
        for (Map.Entry<String, ObservableList<String>> item: foods.entrySet()) {
            data.put(item.getKey(), new ArrayList<>(item.getValue()));
        }
        return data;
    }
 }
