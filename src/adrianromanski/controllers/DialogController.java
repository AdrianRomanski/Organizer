package adrianromanski.controllers;

import adrianromanski.datamodel.Item;
import adrianromanski.datamodel.ItemData;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadlinePicker;


    /**
     * Is completed is always false when creating new item
     */
    public Item processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();
        String isCompleted = "false";

        Item newItem = new Item(shortDescription, details, deadlineValue, isCompleted);
        ItemData.getInstance().addItem(newItem);
        return newItem;
    }
}
