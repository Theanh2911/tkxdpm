package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;

public class ViewCartController {

    @FXML
    private VBox vboxCart;

    @FXML
    private Label labelSubtotal;

    @FXML
    private Label labelVAT;

    @FXML
    private Label labelAmount;

    @FXML
    private Button btnPlaceOrder;

    private Connection connection;

    @FXML
    public void initialize() {
        connectToDatabase();
        populateCart();
        btnPlaceOrder.setOnAction(event -> handlePlaceOrder());
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:cart.db");
            System.out.println("Connected to SQLite database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private void populateCart() {
        try {
            String query = "SELECT * FROM cart";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            double subtotal = 0;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String itemName = resultSet.getString("item_name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                double totalPrice = price * quantity;
                subtotal += totalPrice;

                vboxCart.getChildren().add(createCartItem(id, itemName, price, quantity, totalPrice));
            }

            updateTotals(subtotal);
        } catch (SQLException e) {
            System.err.println("Error loading cart items: " + e.getMessage());
        }
    }


    private VBox createCartItem(int id, String itemName, double price, int quantity, double totalPrice) {
        VBox itemBox = new VBox();
        itemBox.setSpacing(5);

        Text itemText = new Text(itemName + " - " + quantity + " x " + price + " VND = " + totalPrice + " VND");
        itemBox.getChildren().add(itemText);

        Button removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-cursor: hand;");
        removeButton.setOnAction(event -> removeCartItem(id));

        itemBox.getChildren().add(removeButton);
        return itemBox;
    }

    private void updateTotals(double subtotal) {
        double vat = subtotal * 0.1;
        double totalAmount = subtotal + vat;

        labelSubtotal.setText(String.format("%.2f VND", subtotal));
        labelVAT.setText(String.format("%.2f VND", vat));
        labelAmount.setText(String.format("%.2f VND", totalAmount));
    }

    private void removeCartItem(int id) {
        try {
            String deleteQuery = "DELETE FROM cart WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            vboxCart.getChildren().clear();
            populateCart();
        } catch (SQLException e) {
            System.err.println("Error removing item from cart: " + e.getMessage());
        }
    }

    private void handlePlaceOrder() {
        try {
            String clearCartQuery = "DELETE FROM cart";
            Statement statement = connection.createStatement();
            statement.executeUpdate(clearCartQuery);

            showAlert(Alert.AlertType.INFORMATION, "Your order has been placed successfully!");


            vboxCart.getChildren().clear();
            populateCart();
        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.showAndWait();
    }

}
