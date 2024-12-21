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

public class SignUpController {

    @FXML
    private TextField username;

    @FXML
    private TextField fullName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private DatePicker birthdate;

    @FXML
    private PasswordField password;

    @FXML
    private Button cancel;

    @FXML
    private Button signup;

    private Connection connection;

    @FXML
    public void initialize() throws SQLException {
        connectToDatabase();
        signup.setOnAction(event -> handleSignUp());
        cancel.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Login.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        });
    }

    private void connectToDatabase() throws SQLException {
            connection = DriverManager.getConnection("jdbc:sqlite:login.db");
            System.out.println("Database connection established.");
    }

    private void handleSignUp() {
        String user = username.getText().trim();
        String name = fullName.getText().trim();
        String phone = phoneNumber.getText().trim();
        String pass = password.getText().trim();
        String confirmPass = confirmPassword.getText().trim();
        String dob = (birthdate.getValue() != null) ? birthdate.getValue().toString() : null;

        if (user.isEmpty() || name.isEmpty() || phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || dob == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }
        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }


        try {
            String query = "INSERT INTO users (username, full_name, password, phone, birthdate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, pass);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, dob);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 5) {
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Your account has been created.");
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to register user. " + e.getMessage());
        }
    }

    private void clearFields() {
        username.clear();
        fullName.clear();
        phoneNumber.clear();
        password.clear();
        confirmPassword.clear();
        birthdate.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
