package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.Statement;

public class HomeController {

    @FXML
    private ImageView cartImage;
    @FXML
    private Label numMediaInCart;
    @FXML
    private TextField searchField;
    @FXML
    private SplitMenuButton splitMenuBtnSearch;
    @FXML
    private Button orderBtn;

    @FXML
    private HBox hboxMedia;
    @FXML
    private VBox vboxMedia1, vboxMedia2, vboxMedia3, vboxMedia4;
    @FXML
    private HBox pageNumberHbox;

    private int cartCount = 0;

    @FXML
    public void initialize() {

        cartImage.setOnMouseClicked(event -> onCartClicked());

        orderBtn.setOnAction(event -> handleOrder());

        splitMenuBtnSearch.setOnAction(event -> handleSearch());

        searchField.setOnAction(event -> handleSearch());

        splitMenuBtnSearch.getItems().addAll(
                new MenuItem("By Name A-Z"),
                new MenuItem("By Price High to Low"),
                new MenuItem("By Price Low to High")
        );

        for (MenuItem item : splitMenuBtnSearch.getItems()) {
            item.setOnAction(event -> {
                String selectedOption = item.getText();
                handleSearchOption(selectedOption);
            });
        }
    }

    private void handleSearchOption(String option) {
        String query = "";
        switch (option) {
            case "Name A-Z":
                query = "SELECT * FROM products ORDER BY product_name ASC;\n";
                break;
            case "Price High to Low":
                query = "SELECT * FROM products ORDER BY price DESC";
                break;
            case "Price Low to High":
                query = "SELECT * FROM products ORDER BY price ASC";
                break;
            default:
                return;
        }
    }

    private void onCartClicked() {
        // viet logic cart
    }

    private void handleOrder() {
        cartCount++;
        updateCartCount();
    }

    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            //bat dau query tim kiem
        } else {
            System.out.println("Product not found");
        }
    }


    private void loadMediaContent() {  //load media content
        vboxMedia1.getChildren().add(new Label("CD"));
        vboxMedia2.getChildren().add(new Label("DVD"));
        vboxMedia3.getChildren().add(new Label("Book"));
        vboxMedia4.getChildren().add(new Label("Podcast"));
    }

    private void updateCartCount() {
        numMediaInCart.setText(cartCount + " media");
        System.out.println("Cart count updated: " + cartCount);
    }
}
