package net.vatri.inventory.controllers;

import net.vatri.inventory.App;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import net.vatri.inventory.models.GroupVariant;
import net.vatri.inventory.models.ProductGroup;

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
		ProductGroup group = inventoryService.getGroup(groupId);
		fldName.setText(group.getGroupName());
		fldPrice.setText(group.getPrice());
		// fldVariants.setText(group.getVariantsAsString());
		fldVariants.setText(inventoryService.getGroupVariantsAsString(group));
	}

	@FXML protected boolean saveGroup(){

		if( ! fldPrice.getText().matches("[0-9.]*") || fldName.getText().length() < 2){
			errorLabel.setVisible(true);
			return false;
		} else {

			// Set model data and save into database:
			ProductGroup group = new ProductGroup();

			group.setGroupName(fldName.getText());
			group.setPrice(fldPrice.getText());

			if(_groupId != null && ! _groupId.equals("")){
				group.setId( Integer.parseInt(_groupId));
			}

			if(! inventoryService.saveGroup(group)){
                errorLabel.setText("ERROR: can't save your form. Please contact IT support team!");
				return false;
			} else {

                inventoryService.saveGroupVariants(group, fldVariants.getText());

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

