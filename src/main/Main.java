package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

import java.util.Objects;

/**
 * Main method, entry point of the application
 *
 * @author Blake Bowden
 * @version 1.0
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        InHouse part1 = new InHouse(1, "Brakes", 12.99, 10, 1, 20, 111);
        InHouse part2 = new InHouse(2, "Rims", 56.99, 10, 1, 20, 514);
        Outsourced part3 = new Outsourced(3, "Tire", 14.99, 15, 1, 20, "Tires R' Us");
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        Product product1 = new Product(1, "Bicycle", 49.99, 15, 1, 20);
        Product product2 = new Product(2, "Tricycle", 59.99, 13, 1, 20);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);

        product1.addAssociatedPart(part1);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part3);

        launch(args);
    }
}