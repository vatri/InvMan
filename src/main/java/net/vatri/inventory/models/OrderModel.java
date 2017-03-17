package net.vatri.inventory;

import java.util.*;
import javafx.collections.ObservableList;

public class OrderModel extends BaseModel {

	// ID defined in BaseModel
    // public String id = "0";
    public String name = "";
    public String type = "";
    public String created = "";
    public String address = "";
    public String city = "";
    public String zip = "";
    public String status = "";
    public String comment = "";
    public String total_price = "";

    private ObservableList<OrderItem> orderItems = null;

    public List<Map<String, String>> all(){
        return getDao().getOrders();
    }

    public List<Map<String, String>> all(Map<String, String> params){
        return getDao().getOrders(params);
    }

    public OrderModel(){ }

    // Load product by ID
    public OrderModel(String id){
        Map<String,String> row = getDao().getOrder(id);

        if(null == row){
           System.out.println("Row is null for order ID:" + id);
        } else {
// System.out.println(row);

            this.setId(row.get("id"));
            this.setName(row.get("name"));
            this.setType(row.get("type"));

            this.setCity(row.get("city"));
            this.setAddress(row.get("address"));
            this.setZip(row.get("zip"));


            this.setCreated(row.get("created"));
            this.setStatus(row.get("status"));
            this.setComment(row.get("comment"));

            this.loadItems(this.getId());
        }
    }

    public OrderModel(String id, String status, String comment){
    	this.id    = id;
    	this.status  = status;
        this.comment = comment;
    }

    public boolean save(){
        if( _validateModel() ){
            return getDao().saveOrder(this); 
        }
        return false;
    }

    private boolean _validateModel(){
        if( name.length() < 3 ){
            System.out.println("Name not set for OrderModel.");
            return false;
        }

        if(getItems().size()<1){
            System.out.println("No items in order.");
            return false;
        }

        return true;
    }

    // Setters/getters

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }    

    // Price
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return this.comment;
    }


    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return created;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }


    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return city;
    }


    public void setZip(String zip){
        this.zip = zip;
    }
    public String getZip(){
        return zip;
    }

    public void loadItems(String orderId){
        ObservableList<OrderItem> items = getDao().getOrderItems(orderId);
        this.setItems(items);
    }
    public void setItems(ObservableList<OrderItem> items){
        this.orderItems = items;
    }
    public ObservableList<OrderItem> getItems(){
        return this.orderItems;
    }

    public String toString(){
    	return name + "-" + city;
    }

}