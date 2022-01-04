package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Controls the ability to modify an existing part in the Inventory array. It will load the data into the text fields to
 * be changed. It will only accept changes that are of the correct date type.
 */
public class ModifyPartController {

    Stage stage;
    Parent scene;

    @FXML
    private TextField idTextField;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private Label machineOrCompanyLbl;

    @FXML
    private TextField machineIdOrCompanyTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private TextField priceTextField;

    @FXML
    public void onActionCancelModifyPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * If the part is changed from inHouse to outSourced, then a warning will appear to confirm the change. If Yes is
     * clicked, the machineOrCompany label will change and the textField will be cleared.
     *
     * @param event OutsourceRBtn has been selected
     */
    @FXML
    public void onActionDisplayCompanyName(ActionEvent event) {
        if (outsourcedRBtn.isSelected()) {
            Alert alertSwitchClass = new Alert(Alert.AlertType.WARNING, "Are you sure you want to switch from In House part to Outsourced?", ButtonType.YES, ButtonType.CANCEL);
            alertSwitchClass.setTitle("WARNING! PART CHANGE!");
            Optional<ButtonType> clickButton = alertSwitchClass.showAndWait();
            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                machineOrCompanyLbl.setText("Company Name");
                machineIdOrCompanyTextField.clear();
            } else {
                inHouseRBtn.setSelected(true);
            }
        }
    }

    /**
     * If the part is changed from outSourced to inHouse, then a warning will appear to confirm the change. If Yes is
     * clicked, the machineOrCompany label will change and the textField will be cleared.
     *
     * @param event OutsourceRBtn has been selected
     */
    @FXML
    public void onActionDisplayMachineID(ActionEvent event) {
        if (inHouseRBtn.isSelected()) {
            Alert alertSwitchClass = new Alert(Alert.AlertType.WARNING, "Are you sure you want to switch from In House part to Outsourced?", ButtonType.YES, ButtonType.CANCEL);
            alertSwitchClass.setTitle("WARNING! PART CHANGE!");
            Optional<ButtonType> clickButton = alertSwitchClass.showAndWait();
            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                machineOrCompanyLbl.setText("Machine ID");
                machineIdOrCompanyTextField.clear();
            } else {
                outsourcedRBtn.setSelected(true);
            }
        }
    }

    /**
     * Display the part information that was selected in the main form and determine if inhouse or outsourced
     *
     * @param part The part to be displayed
     */
    public void displayPart(Part part) {
        idTextField.setText(String.valueOf(part.getId()));
        nameTextField.setText(part.getName());
        inventoryTextField.setText(String.valueOf(part.getStock()));
        priceTextField.setText(String.valueOf(part.getPrice()));
        maxTextField.setText(String.valueOf(part.getMax()));
        minTextField.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            inHouseRBtn.setSelected(true);
            machineIdOrCompanyTextField.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else {
            outsourcedRBtn.setSelected(true);
            machineIdOrCompanyTextField.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    /**
     * When the event occurs, all of the fields must NOT be empty, and they must contain the correct data type and
     * max must be greater than min, and inventory must be between min and max. If all of these conditions are met the]
     * the data is saved to a new part in inventory.
     *
     * @param event Save button is pressed
     * @throws IOException view is not found
     */
    @FXML
    void onActionSaveModifyPart(ActionEvent event) throws IOException {
        //Check that none of the fields are blank
        if (inventoryTextField.getText().isEmpty() || priceTextField.getText().isEmpty() || maxTextField.getText().isEmpty() || minTextField.getText().isEmpty() || machineIdOrCompanyTextField.getText().isEmpty()) {
            System.out.println("At least one field is empty");
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
            } else if (inHouseRBtn.isSelected() && !isInteger(machineIdOrCompanyTextField.getText())) {
                badInputError.setContentText("MachineId must be a whole number");
                badInputError.show();
            } else {
                int partId = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                int inventory = Integer.parseInt(inventoryTextField.getText());
                double price = Double.parseDouble(priceTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int min = Integer.parseInt(minTextField.getText());

                // Need the index for Inventory.updatePart() part method
                int index = Inventory.getAllParts().indexOf(Inventory.lookupPart(partId));

                //if the min, max and inventory amounts are correct, skip this if statement
                if (min >= max || inventory < min || inventory > max) {
                    Alert minMaxError = new Alert(Alert.AlertType.ERROR);
                    minMaxError.setTitle("Invalid input");
                    if (min >= max) {
                        minMaxError.setContentText("Maximum must be larger than minimum");
                    } else {
                        minMaxError.setContentText("Inventory needs to be between min and max amounts");
                    }
                    minMaxError.showAndWait();
                    return;
                } else if (inHouseRBtn.isSelected()) {
                    int machineId = Integer.parseInt(machineIdOrCompanyTextField.getText());
                    InHouse modifiedPart = new InHouse(partId, name, price, inventory, min, max, machineId);
                    Inventory.updatePart(index, modifiedPart);
                } else if (outsourcedRBtn.isSelected()) {
                    String companyName = machineIdOrCompanyTextField.getText();
                    Outsourced modifiedPart = new Outsourced(partId, name, price, inventory, min, max, companyName);
                    Inventory.updatePart(index, modifiedPart);
                }

                // go back to main menu
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }

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