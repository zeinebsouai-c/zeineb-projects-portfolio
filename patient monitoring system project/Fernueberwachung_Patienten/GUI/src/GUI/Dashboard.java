package GUI;

import coreLogic.Patient;
import coreLogic.PatientStatus;
import coreLogic.VitalData;
import frontend.FrontEndService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends BorderPane {

    private static Dashboard instance;
    private ListView<Patient> patientList;
    private ListView<String> roomList;
    private final FrontEndService frontEndService;

    public static Dashboard getInstance() {
        return instance;
    }

    public Dashboard(FrontEndService frontEndService) {
        this.frontEndService = frontEndService;
        instance = this;

        // Top menu: ICU, Ward, Post-Op, Home
        HBox topMenu = new HBox(10);
        Button icuButton = new Button("ICU");
        Button wardButton = new Button("Ward");
        Button postOpButton = new Button("Post-Op");
        Button homeButton = new Button("Home");

        topMenu.getChildren().addAll(icuButton, wardButton, postOpButton, homeButton);
        topMenu.setPadding(new Insets(10));
        topMenu.getStyleClass().add("top-menu");

        VBox sideBar = new VBox(10);
        sideBar.setPadding(new Insets(10));
        sideBar.getStyleClass().add("sidebar");
        sideBar.setPrefWidth(280);

        Label searchLabel = new Label("Search by name:");
        TextField searchField = new TextField();

        Label roomSearchLabel = new Label("Search by room:");
        TextField roomSearchField = new TextField();

        searchField.getStyleClass().add("search-field");
        roomSearchField.getStyleClass().add("search-field");

        ToggleGroup viewToggle = new ToggleGroup();
        RadioButton byName = new RadioButton("By Name");
        RadioButton byRoom = new RadioButton("By Room");
        byName.getStyleClass().add("toggle-button");
        byRoom.getStyleClass().add("toggle-button");
        byName.setToggleGroup(viewToggle);
        byRoom.setToggleGroup(viewToggle);
        byName.setSelected(true);

        HBox toggleBox = new HBox(10, byName, byRoom);

        // Initialize lists
        patientList = new ListView<>();
        roomList = new ListView<>();

        // Get data from service
        List<Patient> allPatients = frontEndService.getAllPatients();
        Map<String, List<Patient>> roomMap = frontEndService.getPatientsGroupedByRoom();
        List<String> allRoomNames = frontEndService.getAllRoomNames();

        patientList.getItems().addAll(allPatients);
        roomList.getItems().addAll(allRoomNames);
        FXCollections.sort(roomList.getItems());
        roomList.setFixedCellSize(40);

        // Search listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Patient> filtered = frontEndService.searchPatientsByName(newVal);
            patientList.setItems(FXCollections.observableArrayList(filtered));
        });

        roomSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<String> filtered = frontEndService.searchRoomsByName(newVal);
            roomList.setItems(FXCollections.observableArrayList(filtered));
        });

        patientList.getStyleClass().add("list-view");
        roomList.getStyleClass().add("list-view");

        // Creating sections
        VBox patientSearchSection = new VBox(5, searchLabel, searchField, patientList);
        VBox roomSearchSection = new VBox(5, roomSearchLabel, roomSearchField, roomList);

        // Using StackPane to share the same space for both lists
        StackPane listContainer = new StackPane();
        listContainer.getStyleClass().add("list-container");
        listContainer.getChildren().addAll(patientSearchSection, roomSearchSection);

        // Setting initial states
        patientSearchSection.setVisible(true);
        roomSearchSection.setVisible(false);

        // Toggle between views
        viewToggle.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean showRooms = selected == byRoom;
            patientSearchSection.setVisible(!showRooms);
            roomSearchSection.setVisible(showRooms);

            // Clear selections when switching views
            patientList.getSelectionModel().clearSelection();
            roomList.getSelectionModel().clearSelection();
        });

        // Adding to sidebar
        sideBar.getChildren().addAll(toggleBox, listContainer);

        // Patient list cell factory
        patientList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);
                if (empty || patient == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label dot = new Label("●");
                    dot.setStyle("-fx-text-fill: " + switch (patient.getPatientStatusLevel()) {
                        case CRITICAL -> "red";
                        case WARNING -> "orange";
                        case NORMAL -> "green";
                    } + "; -fx-font-size: 30px;");

                    Label name = new Label(patient.getPatientName() + " " + patient.getPatientSurName());
                    name.setPadding(new Insets(0, 0, 0, 5));
                    name.getStyleClass().add("patient-name");
                    name.setStyle("-fx-text-fill: black;");

                    HBox cellBox = new HBox(10, dot, name);
                    cellBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(cellBox);
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                setStyle(selected ? "-fx-background-color: #b7d1ec; -fx-text-fill: black;" : "-fx-background-color: transparent;");
            }
        });

        // Room list cell factory
        roomList.setCellFactory(listView -> new ListCell<>() {
            private final Label nameLabel = new Label();

            {
                nameLabel.getStyleClass().add("room-name");
                setGraphic(nameLabel);
            }

            @Override
            protected void updateItem(String room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(room);
                    setGraphic(nameLabel);
                }
            }
        });

        Label detailLabel = new Label("Select a patient to view details");
        detailLabel.setFont(Font.font(16));
        VBox detailPane = new VBox(detailLabel);
        detailPane.setPadding(new Insets(20));
        detailPane.getStyleClass().add("detail-pane");

        // Patient selection listener
        patientList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                roomList.getSelectionModel().clearSelection();
                detailPane.getChildren().clear();

                // Get full patient data from service
                Patient fullPatient = frontEndService.getPatientById(newSel.getPatientId());

                // Get vital data for this patient
                HashMap<Integer, VitalData> vitalDataMap = frontEndService.getVitalData();
                VitalData vitalData = vitalDataMap.get(fullPatient.getPatientId());

                Label nameLabel = new Label(fullPatient.getPatientName() + " " + fullPatient.getPatientSurName());
                nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                // Create vital labels with real data
                Label heartRateLabel = new Label("❤️ Heart Rate: " +
                        (vitalData != null ? String.format("%.1f bpm", vitalData.getHeartRate()) : "N/A"));
                heartRateLabel.setStyle("-fx-font-size: 14px;");

                Label respirationLabel = new Label("🌬️ Respiration: " +
                        (vitalData != null ? String.format("%.1f rpm", vitalData.getRespirationRate()) : "N/A"));
                respirationLabel.setStyle("-fx-font-size: 14px;");

                Label tempLabel = new Label("🌡️ Temperature: " +
                        (vitalData != null ? String.format("%.1f °C", vitalData.getBodyTemperature()) : "N/A"));
                tempLabel.setStyle("-fx-font-size: 14px;");

                Label spo2Label = new Label("🩸 SpO2: " +
                        (vitalData != null ? String.format("%.1f%%", vitalData.getPulseOximetry()) : "N/A"));
                spo2Label.setStyle("-fx-font-size: 14px;");

                // FIXED: Dynamic status coloring
                Label alertLabel = new Label(" " + fullPatient.getPatientStatusLevel());
                String statusColor = switch (fullPatient.getPatientStatusLevel()) {
                    case CRITICAL -> "-fx-text-fill: red; -fx-font-weight: bold;";
                    case WARNING -> "-fx-text-fill: orange; -fx-font-weight: bold;";
                    case NORMAL -> "-fx-text-fill: green; -fx-font-weight: bold;";
                };
                alertLabel.setStyle(statusColor);

                // Add all components to detail pane
                detailPane.getChildren().addAll(
                        nameLabel,
                        heartRateLabel,
                        respirationLabel,
                        tempLabel,
                        spo2Label,
                        alertLabel
                );

                // Keep the dummy chart as placeholder
                NumberAxis xAxis = new NumberAxis();
                NumberAxis yAxis = new NumberAxis();
                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setPrefHeight(100);
                lineChart.setLegendVisible(false);
                lineChart.setAnimated(false);

                XYChart.Series<Number, Number> data = new XYChart.Series<>();
                data.getData().add(new XYChart.Data<>(1, 70));
                data.getData().add(new XYChart.Data<>(2, 80));
                data.getData().add(new XYChart.Data<>(3, 75));
                data.getData().add(new XYChart.Data<>(4, 90));
                data.getData().add(new XYChart.Data<>(5, 85));
                lineChart.getData().add(data);

                detailPane.getChildren().add(lineChart);
            }
        });

        // Room selection listener
        roomList.getSelectionModel().selectedItemProperty().addListener((obs, oldRoom, newRoom) -> {
            if (newRoom != null) {
                patientList.getSelectionModel().clearSelection();
                detailPane.getChildren().clear();

                Label roomTitle = new Label(newRoom);
                roomTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                detailPane.getChildren().add(roomTitle);

                VBox patientsContainer = new VBox(10);
                List<Patient> patientsInRoom = frontEndService.getPatientsByRoom(newRoom);

                for (Patient patient : patientsInRoom) {
                    HBox patientRow = new HBox(10);
                    patientRow.setAlignment(Pos.CENTER_LEFT);
                    patientRow.setOnMouseClicked(e -> {
                        patientList.getSelectionModel().select(patient);
                    });

                    Label dot = new Label("●");
                    dot.setStyle("-fx-text-fill: " + switch (patient.getPatientStatusLevel()) {
                        case CRITICAL -> "red";
                        case WARNING -> "orange";
                        case NORMAL -> "green";
                    } + "; -fx-font-size: 30px;");

                    Label name = new Label(patient.getPatientName() + " " + patient.getPatientSurName());
                    name.getStyleClass().add("patient-name");

                    if (patient.getPatientStatusLevel() == PatientStatus.Level.CRITICAL) {
                        Label alertIcon = new Label("⚠️");
                        alertIcon.setStyle("-fx-font-size: 20px;");
                        patientRow.getChildren().add(alertIcon);
                    }

                    patientRow.getChildren().addAll(dot, name);
                    patientsContainer.getChildren().add(patientRow);
                }

                detailPane.getChildren().add(patientsContainer);
            }
        });

        this.setTop(topMenu);
        this.setLeft(sideBar);
        detailPane.setSpacing(10);
        this.setCenter(detailPane);
    }

    public void addPatientToList(Patient patient) {
        javafx.application.Platform.runLater(() -> {
            if (!patientList.getItems().contains(patient)) {
                patientList.getItems().add(patient);
                frontEndService.addPatientToList(patient);
            }
        });
    }

    public void showAlert(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void updatePatientStatusDisplay() {
        javafx.application.Platform.runLater(() -> {
            // Refresh from service
            List<Patient> updatedPatients = frontEndService.getAllPatients();
            patientList.setItems(FXCollections.observableArrayList(updatedPatients));
            patientList.refresh();
        });
    }
}