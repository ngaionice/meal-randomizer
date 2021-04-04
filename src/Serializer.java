import java.io.*;

public class Serializer {
    String path = "foods.ser";

    public DataModel loadData() {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            DataModel foods = (DataModel) input.readObject();
            input.close();
            return foods;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new DataModel();
        }
    }

    public void saveData(DataModel foods) {
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
