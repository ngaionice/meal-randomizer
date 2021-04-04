import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Controller {

    private final DataModel model;
    private final Serializer dataLoader = new Serializer();

    Controller() {
        model = dataLoader.loadData();
    }

    Pane getPickPane() {

        BorderPane root = new BorderPane();
        StackPane outputPane = new StackPane();
        ObservableList<String> choices = model.getFoodTypes();
        ObservableList<BooleanProperty> selected = FXCollections.observableArrayList();

        Text outputText = new Text("");

        VBox checkboxes = new VBox();
        checkboxes.getChildren().add(new Text("Types to pick from"));
        choices.forEach(item -> {
            BooleanProperty checked = new SimpleBooleanProperty(false);
            HBox row = new HBox();
            Label checkBoxText = new Label(item);
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(checked);
            selected.add(checked);
            row.getChildren().addAll(checkBoxText, checkBox);
            checkboxes.getChildren().add(row);
        });

        Button pick = new Button("Go!");
        pick.setOnAction(e -> {
            List<String> picked = new ArrayList<>();
            Random picker = new Random();
            for (int i = 0; i < choices.size(); i++) {
                if (selected.get(i).get()) {
                    String type = choices.get(i);
                    int noOfChoices = model.getNumberOfChoices(type);
                    if (noOfChoices > 0) {
                        picked.add(model.getFoodAt(type, picker.nextInt(noOfChoices)));
                    }
                }
            }
            outputText.setText(String.join(" + ", picked));
            System.out.println("Text set.");    // debug code
        });

        checkboxes.getChildren().add(pick);

        outputPane.getChildren().add(outputText);

        root.setLeft(checkboxes);
        root.setCenter(outputPane);

        return root;
    }

    Pane getEditPane() {

        GridPane root = new GridPane();

        ObservableList<String> foodTypes = model.getFoodTypes();
        ComboBox<String> dropdown = new ComboBox<>(foodTypes);
        if (foodTypes.size() > 0) {
            dropdown.getSelectionModel().selectFirst();
        }

        Button addType = new Button("Add food type");
        Button addFood = new Button("Add food to current food type");

        ListView<String> items = new ListView<>();
        if (foodTypes.size() > 0) {
            items.setItems(model.getFoodOfType(foodTypes.get(0)));
        }

        addFood.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add food");
            dialog.setHeaderText("Food? :D");
            dialog.setContentText("Food?");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> model.addFoodToType(dropdown.getValue(), s));
        });
        addType.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add food type");
            dialog.setHeaderText("Food type? :D");
            dialog.setContentText("Food type?");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                model.addFoodType(s);
                foodTypes.add(s);
            });

        });
        dropdown.setOnAction(e -> items.setItems(model.getFoodOfType(dropdown.getValue())));

        root.add(dropdown, 0, 0);
        root.add(addFood, 1, 0);
        root.add(addType, 2, 0);
        root.add(items, 0, 1, 3, 1);

        return root;
    }

    VBox getNav(BorderPane root) {
        Button pick = new Button("Get meal!");
        Button edit = new Button("Edit food choices");

        VBox nav = new VBox();
        nav.getChildren().addAll(pick, edit);

        pick.setOnAction(e -> root.setCenter(getPickPane()));
        edit.setOnAction(e -> root.setCenter(getEditPane()));

        return nav;
    }

    void exit() {

    }
}
