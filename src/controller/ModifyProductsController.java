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

/**
 * Controls the modify product form so a product can have its data changed, its associated parts added or removed. All
 * of the text fields inputs will be checked to make sure they are not empty and that the correct data type is used.
 * <p>
 * Several logic errors when working with this class. A separate class accessible tempProduct variable was needed to allow
 * the data to be changed and not saved if cancel is pressed.
 */
public class ModifyProductsController implements Initializable {

    Stage stage;
    Parent scene;

    Product tempProduct;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField partSearchTextField;

    @FXML
    private TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Part, String> associatedNameCol;

    @FXML
    private TableColumn<Part, Integer> associatedStockCol;

    @FXML
    private TableColumn<Part, Double> associatedPriceCol;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partPartIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    /**
     * Init method to load the parts in the partsTable
     *
     * @param url            url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        partPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Show the associated parts of the product that was selected to be modified. Loaded from
     * MainFormController class when modify is pressed in the MainForm.
     *
     * @param product the new product being created
     */
    // use in modify parts
    public void displayAssociatedPartTable(Product product) {
        associatedPartsTable.setItems(product.getAllAssociatedParts());
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Loads the data of the product into the modify form and creates a tempProduct where changes are made so if
     * canceled the original will not be changed.
     *
     * @param product Product that is to be modified
     */
    public void setSelectedProductData(Product product) {
        idTextField.setText(String.valueOf(product.getId()));
        nameTextField.setText(product.getName());
        inventoryTextField.setText(String.valueOf(product.getStock()));
        priceTextField.setText(String.valueOf(product.getPrice()));
        maxTextField.setText(String.valueOf(product.getMax()));
        minTextField.setText(String.valueOf(product.getMin()));

        //to be able to not change the data when cancel is pressed, a new temp product will be created
        this.tempProduct = new Product(-10, product.getName(), product.getPrice(), product.getStock(), product.getMin(), product.getMax());
        for (Part p : product.getAllAssociatedParts()) {
            this.tempProduct.addAssociatedPart(p);
        }
        Inventory.addProduct(this.tempProduct);
    }

    /**
     * The part in the top partsTable will be added to the bottom associated parts table.
     *
     * @param event Add button is pressed
     */
    @FXML
    public void onActionAddAssociatedPart(ActionEvent event) {
        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            int partId = Integer.parseInt(idTextField.getText());
            Part part = partsTable.getSelectionModel().getSelectedItem();
            boolean isAssociated = false;

            if (this.tempProduct.getAllAssociatedParts() != null) {
                for (Part p : this.tempProduct.getAllAssociatedParts()) {
                    if (p.getId() == part.getId()) {
                        isAssociated = true;
                    }
                }
                if (!isAssociated) {
                    Objects.requireNonNull(this.tempProduct).addAssociatedPart(part);
                    displayAssociatedPartTable(this.tempProduct);
                } else {
                    Alert alertPartAlreadyAssociated = new Alert(Alert.AlertType.ERROR, "The part is already associated with this product.");
                    alertPartAlreadyAssociated.setTitle("Part is associated");
                    alertPartAlreadyAssociated.showAndWait();
                }
            }
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected. Please select a part and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }
    }

    /**
     * First the tempProduct will be delete and then the form will close go back to the main menu.
     * DONE (FUTURE ENHANCEMENT: When cancel is pressed, any changes should not be saved. Currently if parts are
     * added or removed and cancel is clicked the changes were already made.)
     *
     * @param event Cancel button is pressed
     * @throws IOException view is not found
     */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        Inventory.deleteProduct(this.tempProduct);
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This will remove the associated part from tempProdcut
     *
     * @param event remove associated part is pressed
     */
    @FXML
    public void onActionRemoveAssociatedPart(ActionEvent event) {
        if (associatedPartsTable.getSelectionModel().getSelectedItem() != null) {
            //Warn about removing part
            Alert alertWarnRemoveAssociatedPart = new Alert(Alert.AlertType.WARNING, "Do you want to remove this associated part?", ButtonType.YES, ButtonType.CANCEL);
            alertWarnRemoveAssociatedPart.setTitle("Remove Associated Part");
            Optional<ButtonType> clickButton = alertWarnRemoveAssociatedPart.showAndWait();
            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
                if (this.tempProduct != null) {
                    this.tempProduct.deleteAssociatedPart(selectedPart);
                    displayAssociatedPartTable(this.tempProduct);
                }
            }
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected. Please select a part and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }
    }

    /**
     * When the save button is pressed, the first thing to do is check that all of the fields are not empty. If they are
     * then an alert error will appear. Next it will check that the text entered can be parsed as an integer or double for
     * each data type it will be stored as. Then all of the data will be stored in a new Product and the associated parts
     * will be copied from the tempProduct to the new product. Then the original product will be updated with the new product
     * data and the tempProduct will be deleted.
     *
     * @param event save button is pressed
     * @throws IOException view is not found
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        if (idTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || inventoryTextField.getText().isEmpty() || priceTextField.getText().isEmpty() || maxTextField.getText().isEmpty() || minTextField.getText().isEmpty()) {
            Alert emptyFiledAlert = new Alert(Alert.AlertType.ERROR, "All text fields must contain values before adding a part");
            emptyFiledAlert.setTitle("Input Error");
            emptyFiledAlert.showAndWait();
        } else {
            // check input types
            Alert badInputError = new Alert(Alert.AlertType.ERROR);
            badInputError.setTitle("Invalid Input");
            if (!isInteger(inventoryTextField.getText())) {
                badInputError.setContentText("Inventory must be a whole number.");
                badInputError.showAndWait();
            } else if (!isDouble(priceTextField.getText())) {
                badInputError.setContentText("Price must be a number");
                badInputError.show();
            } else if (!isInteger(maxTextField.getText())) {
                badInputError.setContentText("Max value must be a whole number");
                badInputError.show();
            } else if (!isInteger(minTextField.getText())) {
                badInputError.setContentText("Min value must be a whole number");
                badInputError.show();
            } else {
                //if this point has been reached then the product is ready to have the data set
                int productId = Integer.parseInt(idTextField.getText());
                int productIndex = Inventory.getAllProducts().indexOf(Inventory.lookupProduct(productId));
                String name = nameTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                int inventory = Integer.parseInt(inventoryTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int min = Integer.parseInt(minTextField.getText());
                //create a new product and replace the original with it and then delete the temp
                Product replacementProduct = new Product(productId, name, price, inventory, min, max);
                //copy all associated parts to the original product
                for (Part p : Objects.requireNonNull(Inventory.lookupProduct(-10)).getAllAssociatedParts()) {
                    replacementProduct.addAssociatedPart(p);
                }
                Inventory.updateProduct(productIndex, replacementProduct);
                Inventory.deleteProduct(this.tempProduct);
                //go back to main form
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }
    }

    /**
     * This method will take input of integer or string in the partSearchField and display a part id
     * if an integer is entered, or filter part(s) with names that contain part or the entire search text.
     * If the search field is empty then all of the parts will be displayed.
     *
     * @param event key press event in the product search field
     */
    @FXML
    public void onKeyTypedPartSearch(KeyEvent event) {
        String partToSearch = partSearchTextField.getText();
        if (partToSearch != null) {
            if (!searchPart(partToSearch).isEmpty()) {
                partsTable.setItems(searchPart(partToSearch));
                partPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Part name " + partToSearch + " not found.");
                alert.showAndWait();
                partSearchTextField.clear();
            }
        }
    }

    /**
     * A string is passed into the function that first detects if the string can be parsed as an Integer.
     * If it can be parsed as an Integer, then it will lookup the product Id in the Inventory model.
     * If it cannot be parsed as an Integer, then it will lookup the product name to see if it contains
     * the searchString.
     *
     * @param searchString text from partSearchField
     * @return the ObservableList<Part> of partially or completely matched parts
     */
    private ObservableList<Part> searchPart(String searchString) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        if (isInteger(searchString)) {
            int searchedPartId = Integer.parseInt(searchString);
            matchedParts.add(Inventory.lookupPart(searchedPartId));
        } else {
            matchedParts = Inventory.lookupPart(searchString);
        }
        return matchedParts;
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
