package net.vatri.inventory;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.Map;

public class AddEditGroupController extends BaseController implements Initializable{

	@FXML private TextField fldName;
    @FXML private TextField fldPrice;
    @FXML private TextField fldVariants;
    @FXML private Label errorLabel;
    @FXML private Label savedLabel;
 //    @FXML private ComboBox<ProductGroupModel> groupCombo;

    // Based on this value, we know if this is adding or editing page...
	private String _groupId = App.getInstance().repository.get("selectedGroupId");

	public void initialize(URL url, ResourceBundle rb){

		if(_groupId != null){
			_loadGroupData(_groupId);
		}
	}

	private void _loadGroupData(String groupId){
		// System.out.println("Loading product " + productId);
		ProductGroupModel group = new ProductGroupModel(groupId);
		fldName.setText(group.getName());
		fldPrice.setText(group.getPrice());
		// fldVariants.setText(group.getVariantsAsString());
		fldVariants.setText(group.getVariants());
	}

	@FXML protected boolean saveGroup(){

		if( ! fldPrice.getText().matches("[0-9.]*") || fldName.getText().length() < 2){
			errorLabel.setVisible(true);
			return false;
		} else {

			// Set model data and save into database:
			ProductGroupModel model = new ProductGroupModel();

			model.setName(fldName.getText());
			model.setPrice(fldPrice.getText());

			if(_groupId != null && ! _groupId.equals("")){
				model.setId(_groupId);
			}

			model.setVariants(fldVariants.getText());

			if(! model.save()){
				errorLabel.setText("ERROR: can't save your form. Please contact IT support team!");
				return false;
			} else {

				if(_groupId == null){
					fldName.setText("");
					fldPrice.setText("");
					fldVariants.setText("");
				}
				savedLabel.setVisible(true);
				errorLabel.setVisible(false);
				return true;
			}
		}
	}
	@FXML protected void hideLabels(){
		errorLabel.setVisible(false);
		savedLabel.setVisible(false);
	}
	@FXML protected void handleBack(){
		App.showPage("groups");
	}
}//class

