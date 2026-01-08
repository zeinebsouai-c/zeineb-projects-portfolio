
package GUI;

import contract.Tag;
import contract.Uploader;
import javafx.application.Application;
import domainLogic.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminGUI extends Application {

    // observable lists for data binding
    private ObservableList<String> producerList;
    private ObservableList<MediaContentImpl> mediaList;

    // TableViews for displaying data
    private TableView<String> producerTableView;
    private TableView<MediaContentImpl> mediaTableView;

    private Admin admin;

    @Override
    public void start(Stage primaryStage){
        admin = new Admin(100);
        producerList = FXCollections.observableArrayList();
        mediaList = FXCollections.observableArrayList();

        //loading initial data
        loadProducers();
        loadMediaFiles();

        // creating the layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // producer and  media file sections
        VBox producerSection = createProducerSection();
        VBox mediaSection = createMediaSection();

        // adding sections to the layout
        root.setLeft(producerSection);
        root.setRight(mediaSection);

        // buttons for the crud operations

        HBox buttonsSection = createButtons();
        root.setBottom(buttonsSection);



        // setting up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Admin Management GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createProducerSection(){
        VBox producerSection = new VBox(10);
        producerSection.setPadding(new Insets(10));
        Label producerLabel = new Label("Producers");
        producerTableView = new TableView<>(producerList);
        TableColumn<String, String> producerNameCol = new TableColumn<>("Producer Name");
        producerNameCol.setCellValueFactory(data -> Bindings.createObjectBinding(() -> data.getValue()));
        producerTableView.getColumns().add(producerNameCol);

        producerSection.getChildren().addAll(producerLabel, producerTableView);
        return producerSection;

    }

    private VBox createMediaSection(){
        VBox mediaSection = new VBox(10);
        mediaSection.setPadding(new Insets(10));
        Label mediaLabel = new Label("Media Files");

        mediaTableView = new TableView<>(mediaList);
        setupMediaTableColumns();

        mediaSection.getChildren().addAll(mediaLabel, mediaTableView);
        return mediaSection;
    }

    private void setupMediaTableColumns(){
        TableColumn<MediaContentImpl, String> addressCol= new TableColumn<>("Address");
        addressCol.setCellValueFactory(data -> Bindings.createObjectBinding(() -> data.getValue().getAddress()));
        addressCol.setSortable(true); // Enable sorting

        TableColumn<MediaContentImpl, String> producerCol= new TableColumn<>("Producer");
        producerCol.setCellValueFactory(data -> Bindings.createObjectBinding(() -> data.getValue().getUploader().getName()));
        producerCol.setSortable(true); // Enable sorting


        TableColumn<MediaContentImpl, Long> accessCountCol = new TableColumn<>("Access Count");
        accessCountCol.setCellValueFactory(data -> Bindings.createObjectBinding(() -> data.getValue().getAccessCount()));
        accessCountCol.setSortable(true); // Enable sorting

        TableColumn<MediaContentImpl, Long> availabilityCol= new TableColumn<>("Availability (Days)");
        availabilityCol.setCellValueFactory(data -> Bindings.createObjectBinding(() -> data.getValue().getAvailabilityInDays()));
        availabilityCol.setSortable(true); // Enable sorting

        mediaTableView.getColumns().addAll(addressCol,producerCol,accessCountCol, availabilityCol);

    }

    private HBox createButtons(){
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));

        Button addProducerButton = new Button("Add Producer");
        Button addButton = new Button("Add Media");
        Button updateButton = new Button("Update Media");
        Button deleteButton = new Button("Delete Media");
        Button deleteProducerButton = new Button("Delete Producer");

        addProducerButton.setOnAction(e -> addProducer());
        addButton.setOnAction(e -> addMedia());
        updateButton.setOnAction(e -> updateMedia());
        deleteButton.setOnAction(e -> deleteMedia());
        deleteProducerButton.setOnAction(e -> deleteProducer());

        buttons.getChildren().addAll(addProducerButton, addButton, updateButton, deleteButton, deleteProducerButton);
        return buttons;
    }

    // loading initial data for producers and media
    private void loadProducers(){
        producerList.clear();
        producerList.addAll(admin.getUploaders().stream().map(Uploader::getName).toList());
    }

    private void loadMediaFiles(){
        mediaList.clear();
        mediaList.addAll(admin.getMediaFiles(null));
    }


    // Methods for handling CRUD operations

    private void addProducer(){
        // using a simple input dialog to get producer name from user
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Producer");
        dialog.setHeaderText("Enter the name of the new producer:");
        dialog.setContentText("Producer Name:");

        // getting the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(producerName -> {
            try {
                String response = admin.createUploader(producerName);
                producerList.add(producerName);
                System.out.println(response);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: producer with this name already exists.");
            }
        });
    }

    private void addMedia() {
        // creating a new stage (window) for adding media
        Stage addMediaStage = new Stage();
        addMediaStage.setTitle("Add New Media");

        // defining input fields for medis details
        TextField mediaTypeField = new TextField();
        TextField uploaderNameField = new TextField();
        TextField tagsField = new TextField();
        TextField sizeField = new TextField();
        TextField costField = new TextField();
        TextField durationField = new TextField();
        TextField addressField = new TextField();
        TextField titleField = new TextField();

        TextField samplingRateField = new TextField();
        TextField resolutionField = new TextField();

        // Layout for adding media files
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Media Type:"), mediaTypeField,
                new Label("Uploader Name:"), uploaderNameField,
                new Label("Tags (comma-separated):"), tagsField,
                new Label("Size:"), sizeField,
                new Label("Cost:"), costField,
                new Label("Duration:"), durationField,
                new Label("Address:"), addressField,
                new Label("Title:"), titleField,
                new Label("Sampling Rate (if applicable):"), samplingRateField,
                new Label("Resolution (if applicable):"), resolutionField
        );

        // add media button
        Button addButton = new Button("Add Media");
        addButton.setOnAction(e -> {
            try {
                // parsing data
                String mediaType = mediaTypeField.getText();
                String uploaderName = uploaderNameField.getText();
                Set<Tag> tags = Arrays.stream(tagsField.getText().split(","))
                        .map(String::trim)
                        .map(Tag::valueOf) // assuming the enum Tag exists
                        .collect(Collectors.toSet());
                int size = Integer.parseInt(sizeField.getText());
                BigDecimal cost = new BigDecimal(costField.getText());
                int duration = Integer.parseInt(durationField.getText());
                String address = addressField.getText();
                String title = titleField.getText();
                Integer samplingRate = !samplingRateField.getText().isEmpty() ? Integer.parseInt(samplingRateField.getText()) : null;
                Integer resolution = !resolutionField.getText().isEmpty() ? Integer.parseInt(resolutionField.getText()) : null;

                String result = admin.uploadMedia(mediaType, uploaderName, tags, size, cost, duration, address, title, samplingRate, resolution);

                System.out.println(result);

                // refreshing the media list
                loadMediaFiles();

                // closing the add media window
                addMediaStage.close();
            } catch (Exception ex) {
                System.err.println("Error adding media: " + ex.getMessage());
            }
        });

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        // setting layout and scene

        layout.getChildren().add(addButton);
        Scene scene = new Scene(scrollPane, 400, 500);
        addMediaStage.setScene(scene);
        addMediaStage.show();


        }

    private void updateMedia() {
        MediaContentImpl selectedMedia = mediaTableView.getSelectionModel().getSelectedItem();
        if (selectedMedia == null) {
            System.out.println("No media selected for updating.");
            return;
        }

        try {
            admin.playMedia(selectedMedia.getAddress());
            mediaTableView.refresh();

            System.out.println("Access count incremented for media: " + selectedMedia.getTitle() +
                        ". New access count: " + selectedMedia.getAccessCount());

        } catch (Exception ex) {
            System.err.println("Error updating media" + ex.getMessage());
        }


    }

    private void deleteMedia() {
        MediaContentImpl selectedMedia = mediaTableView.getSelectionModel().getSelectedItem();
        if (selectedMedia != null) {
            admin.deleteMediaFile(selectedMedia.getAddress());
            mediaList.remove(selectedMedia);
        }
    }

    private void deleteProducer() {
        String selectedProducer = producerTableView.getSelectionModel().getSelectedItem();
        if (selectedProducer != null) {
            admin.deleteUploader(selectedProducer);
            producerList.remove(selectedProducer);
            System.out.println("Producer deleted: " + selectedProducer);
        } else {
            System.out.println("No producer selected to delete.");
        }
    }

}
