package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShippingController {

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    @FXML
    private TextField instructions;

    @FXML
    private ComboBox<String> province;

    @FXML
    private Button btnConfirmDelivery;

    private static final String DB_URL = "jdbc:sqlite:shipping.db";

    @FXML
    public void initialize() {
        province.setItems(FXCollections.observableArrayList(
                "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh",
                "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên",
                "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội",
                "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên",
                "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lạng Sơn", "Lào Cai",
                "Lâm Đồng", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận",
                "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh",
                "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
                "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh",
                "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
        ));


        btnConfirmDelivery.setOnAction(event -> submitDeliveryInfo());
    }

    // Method to validate the input fields
    private boolean validateInputs() {
        String nameInput = name.getText().trim();
        String phoneInput = phone.getText().trim();
        String addressInput = address.getText().trim();
        String provinceInput = province.getValue();

        if (nameInput.isEmpty() || !nameInput.matches("[a-zA-Z ]+")) {
            showAlert("Invalid Input", "Name must contain only letters and cannot be empty.");
            return false;
        }

        if (phoneInput.isEmpty() || !phoneInput.matches("\\d{10}")) {
            showAlert("Invalid Input", "Phone must be a 10-digit number.");
            return false;
        }

        if (addressInput.isEmpty()) {
            showAlert("Invalid Input", "Address cannot be empty.");
            return false;
        }

        if (provinceInput == null || provinceInput.isEmpty()) {
            showAlert("Invalid Input", "Please select a province.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void submitDeliveryInfo() {
        if (validateInputs()) {
            String nameInput = name.getText().trim();
            String phoneInput = phone.getText().trim();
            String addressInput = address.getText().trim();
            String instructionsInput = instructions.getText().trim();
            String provinceInput = province.getValue();

            saveToDatabase(nameInput, phoneInput, addressInput, provinceInput, instructionsInput);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Delivery information submitted");
            successAlert.showAndWait();

            clearFields();
        }
    }

    private void saveToDatabase(String name, String phone, String address, String province, String instructions) {
        String insertQuery = "INSERT INTO shipping_info (name, phone, address, province, shipping_instructions) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, province);
            preparedStatement.setString(5, instructions);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save delivery information: " + e.getMessage());
        }
    }

    private void clearFields() {
        name.clear();
        phone.clear();
        address.clear();
        instructions.clear();
        province.getSelectionModel().clearSelection();
    }
}
