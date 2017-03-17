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
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
// import javafx.scene.control.ListView;

import javafx.scene.control.ComboBox;
// import javafx.collections.ObservableList;
// import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

    @FXML private ComboBox<ProductModel> comboProducts;
    @FXML private ComboBox<GroupVariantModel> comboVariants;


    @FXML private TableView<OrderItem> tblItems;
    @FXML private TableColumn<OrderItem, ProductModel> colProduct;
    @FXML private TableColumn<OrderItem, ProductGroupModel> colVariant;
    @FXML private TableColumn<OrderItem, TextField> colPrice;

	// @FXML private ListView<ProductModel> listAvailableProducts;
	// @FXML private ListView<ProductModel> listAddedProducts;

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
		OrderModel order = new OrderModel(orderId);

		fldName.setText( order.getName());
		fldCity.setText( order.getCity());
		fldAddress.setText( order.getAddress());
		fldZip.setText( order.getZip());
		fldComment.setText( order.getComment());

		comboStatus.getSelectionModel().select(order.getStatus());

		String selectedType = order.getType().equals("buy") ? "Buy" : "Sell";

		comboType.getSelectionModel().select(selectedType);

		// fldZip.setText( order.getName());
		tblItems.getItems().addAll( order.getItems() );

	}

	private void _fillAllProductsCombo(){
		for( Map<String,String> row : new ProductModel().all() ){
			comboProducts.getItems().add(
				new ProductModel(row.get("id"))
			);
		}
	}

	private void _fillVariantsCombo(ProductModel product){

		ProductGroupModel group = new ProductGroupModel(product.getGroup());
 		comboVariants.getItems().clear();

		List<GroupVariantModel> variants = group.getVariantModels();
		if(variants != null){
			for(GroupVariantModel v : variants){
				comboVariants.getItems().add(v);
			}
		}
		comboVariants.getSelectionModel().selectFirst();
	}

	// Fired when product in the combo is selected:
	@FXML private void productSelected(){
		if( comboProducts.getSelectionModel().getSelectedItem() instanceof ProductModel != true){
			System.out.println("Value in product combo is not ProductModel");
			return ;
		}
		ProductModel selectedProduct = comboProducts.getSelectionModel().getSelectedItem();
		_fillVariantsCombo(selectedProduct);
	}

	// Fired when Add button is clicked
	@FXML private void addProduct(){

		// If no product is selected, just skip this action
		if( comboProducts.getSelectionModel().getSelectedItem() instanceof ProductModel != true){
			System.out.println("Value in product combo is not ProductModel");
			return ;
		}

		ProductModel product      = comboProducts.getSelectionModel().getSelectedItem();
		GroupVariantModel variant = comboVariants.getSelectionModel().getSelectedItem();

		if( product instanceof ProductModel){
			tblItems.getItems().add(
		        new OrderItem(product, variant)
			);
			// Selects String "select product..." (reset combo)
			comboProducts.getSelectionModel().selectFirst();
		}
	}

	@FXML protected boolean save(ActionEvent event){

		OrderModel order = new OrderModel();

		order.setName(fldName.getText());
		order.setType( comboType.getSelectionModel().getSelectedItem().toLowerCase() );
		order.setAddress(fldAddress.getText());
		order.setCity(fldCity.getText());
		order.setZip(fldZip.getText());
		order.setStatus( comboStatus.getSelectionModel().getSelectedItem() );
		order.setComment( fldComment.getText() );
		order.setItems(tblItems.getItems());

		if(_orderId != null){
			order.setId(_orderId);
		}

		if(order.save()){
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