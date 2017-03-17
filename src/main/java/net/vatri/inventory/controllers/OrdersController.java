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
import javafx.scene.control.ComboBox;


import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

//import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


// import javafx.event.*;
import javafx.scene.input.MouseEvent;

public class OrdersController extends BaseController implements Initializable {

    @FXML private TableView<OrderModel> tblOrders;

    @FXML private ComboBox<String> typeFilterCombo;

    @FXML private TableColumn<OrderModel, String> idCol;
    @FXML private TableColumn<OrderModel, String> nameCol;
    @FXML private TableColumn<OrderModel, String> typeCol;
    @FXML private TableColumn<OrderModel, String> createdCol;
    @FXML private TableColumn<OrderModel, String> statusCol;
    @FXML private TableColumn<OrderModel, String> commentCol;

    @FXML private Button btnAdd;

    // @FXML private TextField fldName;
    // @FXML private TextField fldPrice;
    

	public void initialize(URL url, ResourceBundle rb){


		typeFilterCombo.getSelectionModel().selectFirst();

		// Generate products table rows...
		ObservableList<OrderModel> tblData = FXCollections.observableArrayList();
		OrderModel orderModel = new OrderModel();

		for(Map<String,String> order : orderModel.all()){
// System.out.println("Url="+url);
			OrderModel tmpModel = new OrderModel(
				order.get("id")
				// order.get("status"),
				// order.get("comment")
			);

			// tmpModel.setGroup(prod.get("group_name"), prod.get("group_price"));

			tblData.add(tmpModel);
		}

		idCol.setCellValueFactory( new PropertyValueFactory<>("id"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		createdCol.setCellValueFactory(new PropertyValueFactory<>("created"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

		tblOrders.setItems(tblData);
	}// all()

	@FXML private void handleFilterByType(){

		String selectedType = typeFilterCombo.getSelectionModel().getSelectedItem();

		String type = "";
		if(selectedType.equals("Sell")){
			type = "sell";
		} else if(selectedType.equals("Buy")){
			type = "buy";
		}

		Map<String,String> params = new HashMap<String,String>();

		if( ! type.equals("")){
			params.put("type", type);
		}
		ObservableList<OrderModel> tblData = FXCollections.observableArrayList();

		OrderModel orderModel = new OrderModel();

		for(Map<String,String> order : orderModel.all(params)){
			OrderModel tmpModel = new OrderModel(order.get("id"));

			tblData.add(tmpModel);
		}

		tblOrders.getItems().clear();
		// System.out.println(tblData);

		tblOrders.setItems(tblData);
	}

	@FXML protected void handleAdd(){
		// Since we are adding product, set selected ID to null
		App.getInstance().repository.put("selectedOrderId", null);
		App.showPage("addEditOrder");
	}

	@FXML protected void openOrder(){

		OrderModel order = tblOrders.getSelectionModel().getSelectedItem();
		if( order != null) {
			App.getInstance().repository.put("selectedOrderId", order.getId());
			App.showPage("addEditOrder");
		}
		// System.out.println("Test..");

		// ProductModel prod = tblProducts.getSelectionModel().getSelectedItem();
		// App.getInstance().repository.put("selectedProductId", prod.getId());
		// App.showPage("newProduct");
	}

}
