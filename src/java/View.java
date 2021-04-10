import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View extends Application {

    Controller con;
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 540, 540);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        con = new Controller(scene);
        root.setLeft(con.getNav(root));
        root.setCenter(con.getPickPane());

        primaryStage.setTitle("Meal picker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop() {
        con.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
