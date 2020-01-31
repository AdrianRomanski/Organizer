package adrianromanski.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ItemData {
    private static ItemData instance = new ItemData();
    private static String fileName = "ListItems.txt";

    private ObservableList<Item> items;
    private DateTimeFormatter formatter;


    public static ItemData getInstance() {
        return instance;
    }

    private ItemData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void loadItems() throws IOException {
        items = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                Item item = new Item(shortDescription, details, date);
                items.add(item);
            }
        }

    }

    public void storeItems() throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Item item : items) {
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();
            }
        }
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }



}

