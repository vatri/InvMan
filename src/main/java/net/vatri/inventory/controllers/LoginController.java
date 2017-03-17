package net.vatri.inventory;

// import net.vatri.inventory.*;

// import javafx.scene.layout.GridPane;
// import javafx.geometry.Pos;
// import javafx.geometry.Insets; 
// import javafx.scene.control.*;
// import javafx.scene.text.Text;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;

// import javafx.scene.control.Button;

// import javafx.event.EventHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class LoginController extends BaseController{

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML protected void btnLoginPressed(ActionEvent event) {

    	// UserModel user = new UserModel("email", emailField.getText());
    	UserModel user = new UserModel();
    	user.loadByEmail(emailField.getText());

 // System.out.println("E-mail:"+passwordField.getText());

		if(user.getPassword().equals(passwordField.getText() ) ){
			App.showPage("dashboard");
		} else {
			errorLabel.setVisible(true);
		}
    }
}