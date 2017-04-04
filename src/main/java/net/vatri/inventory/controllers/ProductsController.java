package net.vatri.inventory;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.Properties;
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
import net.vatri.inventory.models.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

public class ProductsController extends BaseController implements Initializable {

    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> idCol;
    @FXML private TableColumn<Product, String> productCol;
    @FXML private TableColumn<Product, String> groupCol;
    @FXML private TableColumn<Product, String> priceCol;

    @FXML private Button btnAddProd;

    // @FXML private TextField fldName;
    // @FXML private TextField fldPrice;

	public void initialize(URL url, ResourceBundle rb){

		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		TODO, use following:????
//		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//				.configure() // configures settings from hibernate.cfg.xml
//				.build();
//		try {
//			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
//		} catch (Exception e) {
//			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
//			// so destroy it manually.
//			StandardServiceRegistryBuilder.destroy( registry );
//			System.out.println(e.getMessage());
//		}

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		ObservableList<Product> tblData = FXCollections.observableArrayList(
				session.createQuery("from Product").list()
		);

		session.getTransaction().commit();
		session.close();


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

		Product prod = tblProducts.getSelectionModel().getSelectedItem();
		App.getInstance().repository.put("selectedProductId", prod.getId());
		App.showPage("newProduct");
	}

}
