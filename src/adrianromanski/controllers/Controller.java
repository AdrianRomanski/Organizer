package adrianromanski.controllers;

import adrianromanski.datamodel.Item;
import adrianromanski.datamodel.ItemData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<Item> items;

    @FXML
    private ListView<Item> todoListView;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadLineLabel;
    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {

        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observableValue, Item oldValue, Item newValue) {
                if(newValue != null) {
                    Item item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    deadLineLabel.setText(df.format(item.getDeadline()));
            }
        }
    });
        todoListView.setItems(ItemData.getInstance().getItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new Item");
        dialog.setHeaderText("Use this dialog to create a new item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("itemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            Item newItem = controller.processResults();
            todoListView.getSelectionModel().select(newItem);
        }
    }

    @FXML
    public void handleClickListView() {
        Item item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadLineLabel.setText(item.getDeadline().toString());
    }
}
