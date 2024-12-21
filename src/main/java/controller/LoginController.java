package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button signup;

    private Connection connection;

    @FXML
    public void initialize() {
        connectToDatabase();
        login.setOnAction(event -> handleLogin());
        signup.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/SignUp.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) signup.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        });

    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:login.db");
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleLogin() {
        String username = userName.getText().trim();
        String userPassword = password.getText().trim();

        if (username.isEmpty() || userPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Username and password cannot be empty");
            return;
        }

        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful");
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username or password");
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.showAndWait();
    }

}
