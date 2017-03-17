package net.vatri.inventory;

import java.util.*;


public class ProductModel extends BaseModel {

	// ID defined in BaseModel
    // public String id = "0";
    public String name = "";
    public String price = "0";
    public String group = "0";
    public String groupPrice = "0";

    public List<Map<String, String>> all(){
        return getDao().getProducts();
    }

    // Load product by ID
    public ProductModel(String id){
        Map<String,String> row = getDao().getProduct(id);

        if(null == row){
           System.out.println("Row is null for group ID:"+id);
        } else {
            this.id    = row.get("id");
            this.name  = row.get("name");
            this.price = row.get("price");
            this.group = row.get("group_id");
        }
    }

    public ProductModel(){ }

    public ProductModel(String id, String name, String price){
    	this.id    = id;
    	this.name  = name;
        this.price = price;
    }

    public boolean save(){
        return getDao().saveProduct(this); 
    }

    // Name
    public void setName(String name){
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    // Group
    public void setGroup(String group, String groupPrice){
        this.group = group;
        this.groupPrice = groupPrice;
    }
    // Group ID:
    public String getGroup(){
        return this.group;
    }    

    // Price
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return ( groupPrice == null || groupPrice.equals("") || groupPrice.equals("0") ) 
                ? price 
                : groupPrice;
    }

    public String getProductPrice(){
        return this.price;
    }

    public String toString(){
    	return name;
    }
}