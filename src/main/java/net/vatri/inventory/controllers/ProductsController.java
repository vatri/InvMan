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

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

//import java.util.Collection;
import java.util.List;
import java.util.Map;

// import javafx.event.*;
import javafx.scene.input.MouseEvent;

public class ProductsController extends BaseController implements Initializable {

    @FXML private TableView<ProductModel> tblProducts;
    @FXML private TableColumn<ProductModel, String> idCol;
    @FXML private TableColumn<ProductModel, String> productCol;
    @FXML private TableColumn<ProductModel, String> groupCol;
    @FXML private TableColumn<ProductModel, String> priceCol;

    @FXML private Button btnAddProd;

    // @FXML private TextField fldName;
    // @FXML private TextField fldPrice;
    

	public void initialize(URL url, ResourceBundle rb){

		// Generate products table rows...
		ObservableList<ProductModel> tblData = FXCollections.observableArrayList();
		ProductModel productModel = new ProductModel();

		for(Map<String,String> prod : productModel.all()){
// System.out.println("Url="+url);
			ProductModel tmpModel = new ProductModel(
				prod.get("id"),
				prod.get("name"),
				prod.get("price")
			);
			tmpModel.setGroup(prod.get("group_name"), prod.get("group_price"));

			// tmpModel.setGroup("Group...");

			tblData.add(tmpModel);
		}

		idCol.setCellValueFactory( new PropertyValueFactory<>("id"));
		productCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

		tblProducts.setItems(tblData);
	}// all()

	@FXML protected void handleAddProd(){
		// Since we are adding product, set selected ID to null
		App.getInstance().repository.put("selectedProductId", null);
		App.showPage("newProduct");
	}

	@FXML protected void openProduct(){

		ProductModel prod = tblProducts.getSelectionModel().getSelectedItem();
		App.getInstance().repository.put("selectedProductId", prod.getId());
		App.showPage("newProduct");
	}

}
