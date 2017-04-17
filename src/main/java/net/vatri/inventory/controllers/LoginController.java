package net.vatri.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import net.vatri.inventory.App;
import net.vatri.inventory.models.User;


public class LoginController extends BaseController{

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML protected void btnLoginPressed(ActionEvent event) {
    	User user = inventoryService.getUserByEmail(emailField.getText());
		if(user.getPassword().equals(passwordField.getText() ) ){
			App.showPage("dashboard");
		} else {
			errorLabel.setVisible(true);
		}
    }
}