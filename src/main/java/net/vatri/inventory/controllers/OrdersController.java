package net.vatri.inventory.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.vatri.inventory.App;
import net.vatri.inventory.models.Order;

import java.util.Map;
import java.util.HashMap;

public class OrdersController extends BaseController implements Initializable {

    @FXML private TableView<Order> tblOrders;

    @FXML private ComboBox<String> typeFilterCombo;

    @FXML private TableColumn<Order, String> idCol;
    @FXML private TableColumn<Order, String> nameCol;
    @FXML private TableColumn<Order, String> typeCol;
    @FXML private TableColumn<Order, String> createdCol;
    @FXML private TableColumn<Order, String> statusCol;
    @FXML private TableColumn<Order, String> commentCol;

    @FXML private Button btnAdd;

	public void initialize(URL url, ResourceBundle rb){

		typeFilterCombo.getSelectionModel().selectFirst();

		ObservableList<Order> tblData = FXCollections.observableArrayList(
		        inventoryService.getOrders()
        );

        tblOrders.setItems(tblData);

		idCol.setCellValueFactory( new PropertyValueFactory<>("id"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		createdCol.setCellValueFactory(new PropertyValueFactory<>("created"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

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
		ObservableList<Order> tblData = FXCollections.observableArrayList(
		        inventoryService.getOrders(params)
        );
		tblOrders.getItems().clear();
		tblOrders.setItems(tblData);
	}

	@FXML protected void handleAdd(){
		App.showPage("addEditOrder");
	}

	@FXML protected void openOrder(){

        Order order = tblOrders.getSelectionModel().getSelectedItem();
		if( order != null) {
			App.getInstance().repository.put("selectedOrderId", order.getId().toString());
			App.showPage("addEditOrder");
		}
	}

}
