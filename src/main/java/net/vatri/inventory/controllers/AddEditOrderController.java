package net.vatri.inventory.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.cell.TextFieldTableCell;
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
    @FXML private TableColumn<Product, String> colProduct;
    @FXML private TableColumn<GroupVariant, String> colVariant;
    @FXML private TableColumn<OrderItem, String> colPrice;

    @FXML private Label errorLabel;
    @FXML private Label savedLabel;

    // Based on this value, we know if this is adding or editing page...
	private String _orderId = App.getInstance().repository.get("selectedOrderId");
	private Order editingOrder;

	private List<OrderItem> removeList = new ArrayList<OrderItem>();

	public void initialize(URL url, ResourceBundle rb){

		_fillAllProductsCombo();
		comboStatus.getItems().addAll(STATUS_IN_PROGRESS, STATUS_COMPLETED);

		comboType.getItems().addAll("sell", "buy");

        tblItems.setEditable(true);

        colProduct.setCellValueFactory( new PropertyValueFactory<>("product"));
		colVariant.setCellValueFactory( new PropertyValueFactory<>("groupVariant"));
		colPrice.setCellValueFactory( new PropertyValueFactory<>("price"));
        colPrice.setCellFactory( TextFieldTableCell.<OrderItem>forTableColumn() );

        // LAMBA...
//        colPrice.setOnEditCommit((TableColumn.CellEditEvent<OrderItem, String> t) -> {
//            ((OrderItem) t.getTableView().getItems().get(
//                    t.getTablePosition().getRow())
//            ).setPrice(t.getNewValue());
//        });

        // No Lamba...
        colPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderItem, String> t) {
                ((OrderItem) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPrice(t.getNewValue());
            }
        });

//        colPrice.setEditable(true);

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
		this.editingOrder = order;
	}

	private void _fillAllProductsCombo(){
		comboProducts.getItems().addAll(
				inventoryService.getProducts()
		);
		comboProducts.getSelectionModel().selectFirst();
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
		if( comboProducts.getSelectionModel().getSelectedItem() instanceof Product == false){
			System.out.println("Value in product combo is not Product model");
			return ;
		}
        Product selectedProduct = comboProducts.getSelectionModel().getSelectedItem();

		_fillVariantsCombo(selectedProduct);
	}

	// Fired when Add button is clicked
	@FXML private void addProduct(){

		// If no product is selected, just skip this action
		if( comboProducts.getSelectionModel().getSelectedItem() instanceof Product == false){
			System.out.println("Value in product combo is not Product model");
			return ;
		}

		Product product      = comboProducts.getSelectionModel().getSelectedItem();
		GroupVariant variant = comboVariants.getSelectionModel().getSelectedItem();

		if( product != null){
		    OrderItem item = new OrderItem();

            item.setProduct(product);
            item.setGroupVariant(variant);

            item.setOrder(this.editingOrder); // Ensures saving of orderitem

			tblItems.getItems().add(item);
			comboProducts.getSelectionModel().selectFirst();
			comboVariants.getItems().clear();
		}
	}

	@FXML protected boolean save(){

		Order order = new Order();

		if( this.editingOrder == null){
            order.setCreated( DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) );
        } else {
		    order = this.editingOrder;
        }

		order.setName(fldName.getText());
		order.setType( comboType.getSelectionModel().getSelectedItem().toLowerCase() );
		order.setAddress(fldAddress.getText());
		order.setCity(fldCity.getText());
		order.setZip(fldZip.getText());
		order.setStatus( comboStatus.getSelectionModel().getSelectedItem() );
		order.setComment( fldComment.getText() );
		order.setItems(tblItems.getItems());

		if( inventoryService.saveOrder(order) ){
		    this.removeItems();
			App.showPage("orders");
		} else {
			System.out.println("Can't save order!");
			errorLabel.setVisible(true);
		}

		return true;
	}

	private void removeItems(){
	    for(OrderItem item : this.removeList){
            inventoryService.removeOrderItem(item);
        }
    }

	@FXML private void handleBack(){
		App.showPage("orders");
	}

	@FXML private void handleRemove(){

		OrderItem selectedItem = tblItems.getSelectionModel().getSelectedItem();

		if(selectedItem != null) {
			tblItems.getItems().removeAll(selectedItem);
			this.removeList.add(selectedItem);
		}
	}

}//class