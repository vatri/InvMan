package net.vatri.inventory.controllers;

import javafx.scene.control.cell.PropertyValueFactory;
import net.vatri.inventory.App;

import net.vatri.inventory.models.GroupVariant;
import net.vatri.inventory.models.ProductGroup;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddEditGroupController extends BaseController implements Initializable{

	@FXML private TextField fldName;
    @FXML private TextField fldPrice;
    @FXML private Label errorLabel;
    @FXML private Label savedLabel;
    @FXML private TableView<GroupVariant> tblVariants;
    @FXML private TableColumn<GroupVariant, String> colVariantName;
    @FXML private TextField newVariantNameFld;

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
//		fldVariants.setText(inventoryService.getGroupVariantsAsString(group));
        _loadGroupVariantsTable(group);
	}

	private void _loadGroupVariantsTable(ProductGroup group){
        colVariantName.setCellValueFactory( new PropertyValueFactory<>("variantName"));
        tblVariants.getItems().addAll(group.getGroupVariants());
    }

	@FXML protected boolean saveGroup(){

		if( ! fldPrice.getText().matches("[0-9.]*") || fldName.getText().length() < 2){
			errorLabel.setVisible(true);
			return false;
		} else {

            // Set model data and save into database:
            ProductGroup group = new ProductGroup();
            if(_groupId != null && ! _groupId.equals("")){
                group = inventoryService.getGroup(_groupId);
            } else {
                group.setCreated( DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())  );
            }

            group.setGroupName(fldName.getText());
            group.setPrice(fldPrice.getText());
            group.setGroupVariants( tblVariants.getItems() );

            // Required in order to save successfully by Hibernate...
            for(GroupVariant gv : group.getGroupVariants()){
                gv.setGroup(group);
            }

			if(! inventoryService.saveGroup(group)){
                errorLabel.setText("ERROR: can't save your form. Please contact IT support team!");
				return false;
			} else {

				if(_groupId == null){
					fldName.setText("");
					fldPrice.setText("");
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

	@FXML private void handleAddNewVariant(){
        if(newVariantNameFld.getText().length() > 0){

            GroupVariant groupVariant = new GroupVariant();
            groupVariant.setVariantName(newVariantNameFld.getText());
            tblVariants.getItems().add(groupVariant);
            newVariantNameFld.setText("");
        }
    }
}//class

