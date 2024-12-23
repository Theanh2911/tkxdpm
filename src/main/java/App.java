import controller.HomeController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Configs;

import java.io.IOException;

public class App extends Application {

    @FXML
    ImageView logo;

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the scene using HOME_PATH instead of SPLASH_SCREEN_PATH
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(Configs.HOME_PATH));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            // After fade in, start fade out
            fadeIn.play();
            fadeIn.setOnFinished((e) -> fadeOut.play());

            // After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                try {
                    HomeController homeController = new HomeController();
                    homeController.initialize();
                    homeController.setImage();
                    homeController.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
