import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Controller con = new Controller();
        Scene scene = new Scene(root, 1280, 720);

        root.setLeft(con.getNav(root));

        primaryStage.setTitle("Meal picker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
