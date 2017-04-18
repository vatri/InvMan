package net.vatri.inventory.controllers;

import net.vatri.inventory.models.StockModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StockController extends BaseController implements Initializable {

    @FXML
    private TableView<StockModel> tblStock;
    @FXML
    private TableColumn<StockModel, String> colProduct;
    @FXML
    private TableColumn<StockModel, String> colVariant;
    @FXML
    private TableColumn<StockModel, String> colStock;

    public void initialize(URL url, ResourceBundle rb) {

        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colVariant.setCellValueFactory(new PropertyValueFactory<>("variantName"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tblStock.setItems(FXCollections.observableArrayList(
                inventoryService.getStock()
        ));
    }

}