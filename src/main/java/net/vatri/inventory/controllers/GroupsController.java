package net.vatri.inventory.controllers;

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

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

//import java.util.Collection;
import java.util.List;
import java.util.Map;

// import javafx.event.*;
import javafx.scene.input.MouseEvent;

public class GroupsController extends BaseController implements Initializable {

    @FXML private TableView<ProductGroupModel> tblGroups;
    @FXML private TableColumn<ProductGroupModel, String> idCol;
    @FXML private TableColumn<ProductGroupModel, String> groupCol;
    @FXML private TableColumn<ProductGroupModel, String> priceCol;

    @FXML private Button btnAddGroup;

    // @FXML private TextField fldName;
    // @FXML private TextField fldPrice;
    

	public void initialize(URL url, ResourceBundle rb){

		// Generate products table rows...
		ObservableList<ProductGroupModel> tblData = FXCollections.observableArrayList();
		ProductGroupModel pgModel = new ProductGroupModel();

		for(Map<String,String> group : pgModel.all()){
//System.out.println(p);
			ProductGroupModel tmpModel = new ProductGroupModel(
				group.get("id"),
				group.get("group_name"),
				group.get("price")
			);
			tblData.add(tmpModel);
		}

		idCol.setCellValueFactory( new PropertyValueFactory<>("id"));
		groupCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

		tblGroups.setItems(tblData);
	}// all()

	@FXML protected void openGroup(){
		ProductGroupModel group = tblGroups.getSelectionModel().getSelectedItem();
		App.getInstance().repository.put("selectedGroupId", group.getId());
		App.showPage("addEditGroup");
	}

	@FXML protected void handleAddGroup(){
		// Since we are adding group, set selected ID to null
		App.getInstance().repository.put("selectedGroupId", null);
		App.showPage("addEditGroup");
	}

}
