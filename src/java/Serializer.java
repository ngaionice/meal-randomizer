import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Serializer {
    String path = "foods.ser";

    public DataModel loadData() {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            Map<String, List<String>> raw = (Map<String, List<String>>) input.readObject();
            input.close();
            ObservableMap<String, ObservableList<String>> converted = FXCollections.observableHashMap();
            for (Map.Entry<String, List<String>> item: raw.entrySet()) {
                converted.put(item.getKey(), FXCollections.observableArrayList(item.getValue()));
            }
            return new DataModel(converted);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new DataModel(FXCollections.observableHashMap());
        }
    }

    public void saveData(Map<String, List<String>> foods) {
        try {
            OutputStream file = new FileOutputStream(path);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(foods);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
