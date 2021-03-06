package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField partSearchField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productStockCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TextField productSearchField;

    public MainFormController() {
    }

    /**
     * @param event Add part button is pressed
     * @throws IOException if AddPart.fxml is not found
     */
    @FXML
    public void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPart.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onActionDeletePart(ActionEvent event) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this part?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete Part");
            Optional<ButtonType> clickButton = alert.showAndWait();

            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                boolean isDeleted = Inventory.deletePart(selectedPart);

                if (isDeleted) {
                    Alert alertDeleteSuccess = new Alert(Alert.AlertType.CONFIRMATION, "The part was successfully deleted.", ButtonType.YES);
                    alertDeleteSuccess.setTitle("Delete Part Success");
                    alertDeleteSuccess.showAndWait();
                } else {
                    Alert alertDeleteFailure = new Alert(Alert.AlertType.ERROR, "The part was not deleted.", ButtonType.OK);
                    alertDeleteFailure.setTitle("Delete Failure");
                    alertDeleteFailure.showAndWait();
                }
            }
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected, please select a part and try again.", ButtonType.OK);
            alertNoSelection.setTitle("No part selected.");
            alertNoSelection.showAndWait();
        }

    }

    /**
     * Deletes the product if and only if there are no associated parts with the product
     *
     * @param event delete button has been pressed
     */
    // TODO: 12/30/2021 finish the delete product function. This function has to have no parts associated with it in order to delete.
    @FXML
    public void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete Product");
            Optional<ButtonType> clickButton = alert.showAndWait();

            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                int productId = productTable.getSelectionModel().getSelectedItem().getId();

                // check that there are no associated parts
                if (Objects.requireNonNull(Inventory.lookupProduct(productId)).getAllAssociatedParts().isEmpty()) {
                    boolean isDeleted = Inventory.deleteProduct(productTable.getSelectionModel().getSelectedItem());

                    if (isDeleted) {
                        Alert alertDeleteSuccess = new Alert(Alert.AlertType.CONFIRMATION, "The product was successfully deleted.");
                        alertDeleteSuccess.setTitle("Delete Success");
                        alertDeleteSuccess.showAndWait();
                    } else {
                        Alert alertDeleteFailure = new Alert(Alert.AlertType.ERROR, "The product was not deleted.");
                        alertDeleteFailure.setTitle("Delete Failure");
                        alertDeleteFailure.showAndWait();
                    }
                } else {
                    Alert alertHasAssociatedPart = new Alert(Alert.AlertType.ERROR, "Cannot delete a product that still has an associated part.");
                    alertHasAssociatedPart.showAndWait();
                }

            }
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "Please select a part and try again");
        }
    }

    /**
     * Prompts users to confirm that they wish to closed the application
     *
     * @param event exit button has been pressed
     */
    @FXML
    public void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to exit?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Exit Application");
        Optional<ButtonType> exitButton = alert.showAndWait();

        if ((exitButton.isPresent()) && (exitButton.get() == ButtonType.YES)) {
            System.exit(0);
        }
    }

    /**
     * Load the part data into the ModifyPartController and then show ModifyPart.fxml
     *
     * @param event part modify button is pressed
     * @throws IOException if view does not exist
     */
    @FXML
    public void onActionModifyPart(ActionEvent event) throws IOException {

        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPartController modifyPartController = loader.getController();
            modifyPartController.displayPart(partsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected. Please select a part and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }
    }

    /**
     * @param event Modify product button has been pressed.
     * @throws IOException is view does not exist
     */
    @FXML
    public void onActionModifyProduct(ActionEvent event) throws IOException {

        if (productTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            ModifyProductsController modifyProductsController = loader.getController();
            modifyProductsController.setSelectedProductData(productTable.getSelectionModel().getSelectedItem());
            modifyProductsController.displayAssociatedPartTable(productTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No product has been selected. Please select a product and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }


    }

    /**
     * This method will take input of integer or string in the productSearchField and display a part id
     * if an integer is entered, or filter products(s) with names that contain part or the entire search text.
     * If the search field is empty then all of the parts will be displayed.
     *
     * @param event key press event in the product search field
     */
    @FXML
    public void onKeyTypeSearchProduct(KeyEvent event) {
        String searchString = productSearchField.getText();

        if (searchString != null) {
            if (searchProduct(searchString).isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Product name " + searchString + " not found.");
                alert.showAndWait();
                partSearchField.clear();
            } else {
                productTable.setItems(searchProduct(searchString));
            }
        }
    }

    /**
     * A string is passed into the function that first detects if the string can be parsed as an Integer.
     * If it can be parsed as an Integer, then it will lookup the productId in the Inventory model.
     * If it cannot be parsed as an Integer, then it will lookup the product name to see if it contains
     * the searchString.
     *
     * @param searchString text from the productSearchField
     * @return ObservableList<Product> that matches the id or contains the searchString in the product name.
     */
    public ObservableList<Product> searchProduct(String searchString) {
        ObservableList<Product> matchedProduct = FXCollections.observableArrayList();

        if (isInteger(searchString)) {
            int searchedProductId = Integer.parseInt(searchString);
            matchedProduct.add(Inventory.lookupProduct(searchedProductId));
        } else {
            matchedProduct = Inventory.lookupProduct(searchString);
        }
        return matchedProduct;
    }

    /**
     * This method will take input of integer or string in the partSearchField and display a part id
     * if an integer is entered, or filter part(s) that contain part or the entire search text.
     * If the search field is empty then all of the parts will be displayed.
     * FUTURE ENHANCEMENT: instead of removing all fo the text, remove the last character that made the search
     * return 0 parts.
     *
     * @param event a key press event in the part text field
     */
    @FXML
    public void onKeyTypedSearchPart(KeyEvent event) {
        String searchString = partSearchField.getText();
        //if the search field is not empty
        if (searchString != null) {
            if (searchPart(searchString).isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Part name " + searchString + " not found.");
                alert.showAndWait();
                partSearchField.clear();
            } else {
                partsTable.setItems(searchPart(searchString));
                partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
                partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            }
        }
    }

    /**
     * @param searchString text from partSearchField
     * @return the ObservableList<Part> of partially or completely matched parts
     */
    public ObservableList<Part> searchPart(String searchString) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();

        if (isInteger(searchString)) {
            int searchedPartId = Integer.parseInt(searchString);
            matchedParts.add(Inventory.lookupPart(searchedPartId));
        } else {
            matchedParts = Inventory.lookupPart(searchString);
        }
        return matchedParts;
    }

    /**
     * Init method that will fill both of the table views with the data of the parts and products
     *
     * @param url            the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    private boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}

