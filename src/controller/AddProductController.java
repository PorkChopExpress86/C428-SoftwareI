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
 * Controller for the Add Product form
 * This will allow a new product to be entered and its associated parts to be selected and saved to the application
 */
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

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

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField partSearchTextField;

    @FXML
    private Button addBtn;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button removeAssociatedPartBtn;

    @FXML
    private Button saveBtn;

    /**
     * When the form loads a new product must be create so it can accept associated parts while it's main data
     * is being entered. Also the part table needs to have all of the parts loaded so the user can pick a part
     * to associate with the new product.
     *
     * @param url            URL
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //get the next productId
        int productLastIndex = Inventory.getAllProducts().size() - 1;
        int nextProductId;
        if (productLastIndex == -1) {
            nextProductId = 1;
        } else {
            nextProductId = Inventory.getAllProducts().get(productLastIndex).getId() + 1;
        }

        // fill in the next product id
        idTextField.setText(String.valueOf(nextProductId));

        //must create new product to be able to add associated parts.
        Product newProduct = new Product(nextProductId, null, 0, 0, 0, 0);
        Inventory.addProduct(newProduct);

        //fill the parts table
        partsTable.setItems(Inventory.getAllParts());
        partPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * When a product is being modified
     *
     * @param product the new product being created
     */
    // use in modify product
    public void displayAssociatedPartTable(Product product) {
        associatedPartsTable.setItems(product.getAllAssociatedParts());
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * When the add button is pressed, the selected part from the parts table view, is added to the associated part
     * table.
     *
     * @param event the add button is pressed
     */
    @FXML
    public void onActionAddAssociatedPart(ActionEvent event) {

        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            int partId = Integer.parseInt(idTextField.getText());
            Product product = Inventory.lookupProduct(partId);
            Part part = partsTable.getSelectionModel().getSelectedItem();
            boolean isAssociated = false;

            for (Part p : product.getAllAssociatedParts()) {
                if (p.getId() == part.getId()) {
                    isAssociated = true;
                }
            }
            if (!isAssociated) {
                Objects.requireNonNull(product).addAssociatedPart(part);
                displayAssociatedPartTable(product);
            } else {
                Alert alertPartAlreadyAssociated = new Alert(Alert.AlertType.ERROR, "The part is already associated with this product.");
                alertPartAlreadyAssociated.setTitle("Part is associated");
                alertPartAlreadyAssociated.showAndWait();
            }
        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected. Please select a part and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }
    }


    /**
     * Because the init method creates a new part, if it is cancelled, then it must be destroyed before loading the
     * main menu.
     *
     * @param event the cancel button is pressed
     * @throws IOException the view does not exist
     */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        //remove the new Product from Inventory
        Product product = Inventory.lookupProduct(Integer.parseInt(idTextField.getText()));
        Inventory.deleteProduct(product);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Products may need to have their associated parts removed if the parts come from a new source or if the product
     * is to be deleted.
     *
     * @param event Remove associated part button has been pressed
     */
    @FXML
    public void onActionRemoveAssociatedPart(ActionEvent event) {
        if (associatedPartsTable.getSelectionModel().getSelectedItem() != null) {
            //Warn about removing part
            Alert alertWarnRemoveAssociatedPart = new Alert(Alert.AlertType.WARNING, "Do you want to remove this associated part?", ButtonType.YES, ButtonType.CANCEL);
            alertWarnRemoveAssociatedPart.setTitle("Remove Associated Part");
            Optional<ButtonType> clickButton = alertWarnRemoveAssociatedPart.showAndWait();

            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                int associatedPartId = Integer.parseInt(idTextField.getText());
                Product product = Inventory.lookupProduct(associatedPartId);
                Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
                if (product != null) {
                    product.deleteAssociatedPart(selectedPart);
                }
            }

        } else {
            Alert alertNoSelection = new Alert(Alert.AlertType.ERROR, "No part has been selected. Please select a part and try again.");
            alertNoSelection.setTitle("No selection");
            alertNoSelection.showAndWait();
        }

    }

    /**
     * Before adding the new product data, all of the inputs are checked so they are not empty AND that the text
     * fields contain the correct data types. If both of these conditions are true, then the product will have it's
     * attributes updated with the data in the add product form. After this process is complete, the main form will
     * be loaded
     *
     * @param event The save button is pressed
     * @throws IOException the view cannot be found
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
                //if this point has been reached then the new product is ready to have the data set
                int productId = Integer.parseInt(idTextField.getText());
                Product product = Inventory.lookupProduct(productId);

                product.setName(nameTextField.getText());
                product.setStock(Integer.parseInt(inventoryTextField.getText()));
                product.setPrice(Double.parseDouble(priceTextField.getText()));
                product.setMax(Integer.parseInt(maxTextField.getText()));
                product.setMin(Integer.parseInt(minTextField.getText()));

                //go back to main form
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }
    }

    /**
     * The search part field will determine to search the ID of the part or the name of the part based on the
     * data type entered in the text field. An alert will appear if there is no match and the text box will
     * be cleared.
     * FUTURE ENHANCEMENT: instead of removing all of the text, remove the last character that made the search
     * return 0 parts.
     *
     * @param event any character is entered into the Search Part field
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


