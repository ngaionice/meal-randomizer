import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Controller {

    private final DataModel model;
    private final Serializer dataLoader = new Serializer();
    private final Scene sc;

    Controller(Scene scene) {
        model = dataLoader.loadData();
        sc = scene;
    }

    Pane getPickPane() {

        StackPane root = new StackPane();
        root.setId("back-pane");

        BorderPane border = new BorderPane();
        border.setId("pick-pane");
        StackPane outputPane = new StackPane();
        ObservableList<String> choices = model.getFoodTypes();
        ObservableList<BooleanProperty> selected = FXCollections.observableArrayList();

        Text outputText = new Text("");
        outputText.setId("output-text");

        VBox checkboxes = new VBox();
        checkboxes.prefHeightProperty().bind(border.heightProperty());
        checkboxes.setId("check-bar");
        Text checkHeader = new Text("Pick");
        checkHeader.setId("check-header");
        checkboxes.getChildren().add(checkHeader);
        choices.forEach(item -> {
            BooleanProperty checked = new SimpleBooleanProperty(false);
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            Label checkBoxText = new Label(item);
            checkBoxText.setId("checkbox-text");
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().bindBidirectional(checked);
            selected.add(checked);
            row.getChildren().addAll(checkBox, checkBoxText);
            checkboxes.getChildren().add(row);
        });

        JFXButton pick = new JFXButton("Go");
        pick.setId("pick-go");
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
        });

        checkboxes.getChildren().add(pick);

        outputPane.getChildren().add(outputText);
        root.getChildren().add(border);

        border.setLeft(checkboxes);
        border.setCenter(outputPane);

        JFXDepthManager.setDepth(border, 1);

        return root;
    }

    Pane getEditPane() {

        StackPane root = new StackPane();
        root.setId("back-pane");

        GridPane grid = new GridPane();
        root.getChildren().add(grid);
        grid.setId("edit-pane");
        grid.setVgap(10);

        HBox tools = new HBox();
        tools.setSpacing(12);

        ObservableList<String> foodTypes = model.getFoodTypes();
        JFXComboBox<String> dropdown = new JFXComboBox<>(foodTypes);
        if (foodTypes.size() > 0) {
            dropdown.getSelectionModel().selectFirst();
        }

        JFXButton addFood = new JFXButton();
        JFXButton addType = new JFXButton();
        addFood.setId("inline-button-plus");
        addType.setId("inline-button-plus");
        dropdown.setId("inline-dropdown");

        addFood.setGraphic(new FontIcon());
        addType.setGraphic(new FontIcon());

        Region spacer = new Region();
        spacer.setPrefWidth(40);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        tools.getChildren().addAll(addType, dropdown, spacer, addFood);

        ListView<String> items = new ListView<>();
        if (foodTypes.size() > 0) {
            items.setItems(model.getFoodOfType(foodTypes.get(0)));
        }
        items.setId("item-list");
        items.setEditable(true);
        items.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setId("context-menu");

//            MenuItem editItem = new MenuItem();
//            editItem.setText("Edit");
//            editItem.setOnAction(e -> {
//                String item = cell.getItem();
//                // ???
//            });

            MenuItem deleteItem = new MenuItem();
            deleteItem.setText("Delete");
            deleteItem.setOnAction(e -> items.getItems().remove(cell.getItem()));
            contextMenu.getItems().addAll(deleteItem);

//            editItem.setId("context-item");
            deleteItem.setId("context-item");

            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });

            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("Right clicked");
                if (e.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(cell, e.getScreenX(), e.getScreenY());
                }
            });

            return cell;
        });

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

        grid.add(tools, 0, 0);
        grid.add(items, 0, 1);

        items.prefWidthProperty().bind(grid.widthProperty());

        JFXDepthManager.setDepth(grid, 1);

        return root;
    }

    VBox getNav(BorderPane root) {
        VBox nav = new VBox();
        nav.setId("nav-box");

        JFXButton pick = new JFXButton("Get meal!");
        JFXButton edit = new JFXButton("Edit choices");

        pick.setId("nav-pick");
        edit.setId("nav-edit");

        pick.setGraphic(new FontIcon());
        edit.setGraphic(new FontIcon());

        Region spacer = new Region();
        spacer.prefHeightProperty().bind(sc.heightProperty().multiply(0.125));

        pick.setOnAction(e -> root.setCenter(getPickPane()));
        edit.setOnAction(e -> root.setCenter(getEditPane()));

        nav.getChildren().addAll(spacer, pick, edit);
        return nav;
    }

    void exit() {
        dataLoader.saveData(model.export());
    }
}
