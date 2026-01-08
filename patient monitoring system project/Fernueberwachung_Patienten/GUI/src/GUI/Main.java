package GUI;


import frontend.FrontEndService;
import hospital.Hospital;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        FrontEndService frontEndService = new FrontEndService();
        Dashboard dashboard = new Dashboard(frontEndService);
        Scene scene = new Scene(dashboard, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());

        stage.setTitle("Patient Monitoring Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
