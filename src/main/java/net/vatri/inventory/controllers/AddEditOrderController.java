package net.vatri.inventory.controllers;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;

import javafx.event.ActionEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;

import net.vatri.inventory.App;
import net.vatri.inventory.models.*;

public class AddEditOrderController extends BaseController implements Initializable{

	private static final String STATUS_IN_PROGRESS = "in-progress";
	private static final String STATUS_COMPLETED = "completed";


	@FXML private TextField fldName;
    @FXML private TextField fldCity;
    @FXML private TextField fldAddress;
    @FXML private TextField fldZip;
    @FXML private TextArea fldComment;
    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboType;

    @FXML private ComboBox<Product> comboProducts;
    @FXML private ComboBox<GroupVariant> comboVariants;

    @FXML private TableView<OrderItem> tblItems;
    @FXML private TableColumn<OrderItem, Product> colProduct;
    @FXML private TableColumn<OrderItem, ProductGroup> colVariant;
    @FXML private TableColumn<OrderItem, TextField> colPrice;

    @FXML private Label errorLabel;
    @FXML private Label savedLabel;

    // Based on this value, we know if this is adding or editing page...
	private String _orderId = App.getInstance().repository.get("selectedOrderId");

	public void initialize(URL url, ResourceBundle rb){

		_fillAllProductsCombo();
		comboProducts.getSelectionModel().selectFirst();
		comboStatus.getItems().addAll(STATUS_IN_PROGRESS, STATUS_COMPLETED);

		comboType.getItems().addAll("sell", "buy");

		colProduct.setCellValueFactory( new PropertyValueFactory<>("productName"));
		colVariant.setCellValueFactory( new PropertyValueFactory<>("variantName"));
		colPrice.setCellValueFactory( new PropertyValueFactory<>("price"));

		if(_orderId != null){
			_loadOrderData(_orderId);	
		}
	}

	private void _loadOrderData(String orderId){

		Order order = inventoryService.getOrder(orderId);

		fldName.setText( order.getName());
		fldCity.setText( order.getCity());
		fldAddress.setText( order.getAddress());
		fldZip.setText( order.getZip());
		fldComment.setText( order.getComment());

		comboStatus.getSelectionModel().select(order.getStatus());

		String selectedType = order.getType().equals("buy") ? "Buy" : "Sell";

		comboType.getSelectionModel().select(selectedType);

		tblItems.getItems().addAll( order.getItems() );

	}

	private void _fillAllProductsCombo(){
		comboProducts.getItems().addAll(
				inventoryService.getProducts()
		);
	}

	private void _fillVariantsCombo(Product product){

		ProductGroup group = product.getGroup();
 		comboVariants.getItems().clear();
		comboVariants.getItems().addAll(
				group.getGroupVariants()
		);
		comboVariants.getSelectionModel().selectFirst();
	}

	// Fired when product in the combo is selected:
	@FXML private void productSelected(){
		if( comboProducts.getSelectionModel().getSelectedItem() != null){
			System.out.println("Value in product combo is not ProductModel");
			return ;
		}
		Product selectedProduct = comboProducts.getSelectionModel().getSelectedItem();
		_fillVariantsCombo(selectedProduct);
	}

	// Fired when Add button is clicked
	@FXML private void addProduct(){

		// If no product is selected, just skip this action
		if( comboProducts.getSelectionModel().getSelectedItem() != null){
			System.out.println("Value in product combo is not ProductModel");
			return ;
		}

		Product product      = comboProducts.getSelectionModel().getSelectedItem();
		GroupVariant variant = comboVariants.getSelectionModel().getSelectedItem();

		if( product != null){
		    OrderItem item = new OrderItem();

            item.setProduct(product);
            item.setGroupVariant(variant);

			tblItems.getItems().add(item);
			comboProducts.getSelectionModel().selectFirst();
		}
	}

	@FXML protected boolean save(ActionEvent event){

		Order order = new Order();

		order.setName(fldName.getText());
		order.setType( comboType.getSelectionModel().getSelectedItem().toLowerCase() );
		order.setAddress(fldAddress.getText());
		order.setCity(fldCity.getText());
		order.setZip(fldZip.getText());
		order.setStatus( comboStatus.getSelectionModel().getSelectedItem() );
		order.setComment( fldComment.getText() );
		order.setItems(tblItems.getItems());

		if(_orderId != null){
			order.setId( Integer.parseInt(_orderId));
		}

		if( inventoryService.saveOrder(order) ){
			App.showPage("orders");
		} else {
			System.out.println("Can't save order!");
			errorLabel.setVisible(true);
		}

		return true;
	}

	@FXML private void handleBack(){
		App.showPage("orders");
	}

	@FXML private void handleRemove(){

		OrderItem selectedItem = tblItems.getSelectionModel().getSelectedItem();

		if(selectedItem != null) {
			tblItems.getItems().removeAll(selectedItem);
		}
	}

}//class