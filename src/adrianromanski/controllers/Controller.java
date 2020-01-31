package adrianromanski.controllers;

import adrianromanski.datamodel.Item;
import adrianromanski.datamodel.ItemData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 *
 */
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
    @FXML
    private ContextMenu listContextMenu;


    /**
     * Initialize ContextMenu, todoListView, itemDetailsTextArea
     */
    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Item item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        listContextMenu.getItems().add(deleteMenuItem);
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
        // While user enter the application selecting first item in the list
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            /**
             * Set Color to red if the user passed deadline
             * Set Color for blue if deadline is the next day
             */
            @Override
            public ListCell<Item> call(ListView<Item> itemListView) {
                ListCell<Item> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if(item.getDeadline().isBefore(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if(item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BLUE);
                            }
                        }
                    }
                };

                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                                if(isNowEmpty) {
                                    cell.setContextMenu(null);
                                } else {
                                    cell.setContextMenu(listContextMenu);
                                }
                        });
                return cell;
            }
        });
    }


    /**
     * Showing dialog for creating new item
     * Allow user to create a new item and save it to file
     * @see DialogController
     * User may quit without saving item using cancel button
     */
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


    /**
     * Allow user to delete item by using del button in keyboard
     */
    @FXML public void handleKeyPressed(KeyEvent keyEvent) {
        Item selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
    }


    @FXML
    public void handleClickListView() {
        Item item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadLineLabel.setText(item.getDeadline().toString());
    }


    /**
     * Allow user to delete item by taking action from context menu
     * It's asking before deleting for more safety
     */
    @FXML
    public void deleteItem(Item item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get().equals(ButtonType.OK)) {
            ItemData.getInstance().deleteItem(item);
        }
    }
}
