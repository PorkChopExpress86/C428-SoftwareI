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

/**
 * @author Blake Bowden
 * Controls the add part form.
 *
 * FUTURE ENHANCEMENT: being able to copy from one part to more make a minor change for a new part.
 */
public class AddPartController {

    Stage stage;
    Parent scene;

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
    private TextField machineIdOrCompanyTextField;

    @FXML
    private Label machineIdOrCompanyLbl;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;

    /**
     * Cancel button is pressed and the data entered into the text fields is lost
     *
     * @param event cancel button is pressed
     * @throws IOException view could not be found
     */
    @FXML
    public void onActionCancelAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When the Outsourced radio button is selected the machineIdOrCompanyLbl label test is set to company name
     *
     * @param event Outsourced radio button is selected
     */
    @FXML
    public void onActionDisplayCompanyName(ActionEvent event) {
        machineIdOrCompanyLbl.setText("Company Name");
    }

    /**
     * When the In-House radio button is selected the machineIdOrCompanyLbl label test is set to Macnine ID
     *
     * @param event In-House radio button is selected
     */
    @FXML
    public void onActionDisplayMachineID(ActionEvent event) {
        machineIdOrCompanyLbl.setText("Machine ID");
    }


    /**
     * This method will check the inputs for the correct data type, if the data types are not correct then an d
     * alert box with an error message will appear.
     *
     * @param event save button is pressed
     * @throws IOException if MainForm view could not be found
     */
    @FXML
    public void onActionSaveNewPart(ActionEvent event) throws IOException {

        //Check that none of the fields are blank
        if (inventoryTextField.getText().isEmpty() || priceTextField.getText().isEmpty() || maxTextField.getText().isEmpty() || minTextField.getText().isEmpty() || machineIdOrCompanyTextField.getText().isEmpty()) {
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

                // if this point has been reached then all of the fields of the new part are the correct type
                //get index of the of Inventory parts list
                int newPartId;
                int partsIndex = Inventory.getAllParts().size() - 1;
                if (partsIndex == -1) {
                    newPartId = 1;
                } else {
                    newPartId = Inventory.getAllParts().get(partsIndex).getId() + 1;
                }

                String name = nameTextField.getText();
                int inventory = Integer.parseInt(inventoryTextField.getText());
                double price = Double.parseDouble(priceTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int min = Integer.parseInt(minTextField.getText());

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
                    InHouse newPart = new InHouse(newPartId, name, price, inventory, min, max, machineId);
                    Inventory.addPart(newPart);
                } else if (outsourcedRBtn.isSelected()) {
                    String companyName = machineIdOrCompanyTextField.getText();
                    Outsourced newPart = new Outsourced(newPartId, name, price, inventory, min, max, companyName);
                    Inventory.addPart(newPart);
                }

                // go back to main menu
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * @param string input that is parsed as an integer
     * @return true if the string can be parsed as an int, return false if it cannot
     */
    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    /**
     * @param string input that is parsed as an double
     * @return true if the string can be parsed as double, return false if it cannot
     */
    public boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

}
