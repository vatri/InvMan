package net.vatri.inventory;

// import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.ArrayList;

public class ProductGroupModel extends BaseModel{

	// private String id = "0";
    private String name = "";
    private String price = "0";

    // Group variants values separated with comma 
    public String strVariants = "";

    // List of all product groups
    public List<Map<String,String>> all(){
        return getDao().getGroups();
    }

    public ProductGroupModel(){ }

    public ProductGroupModel(String id){
        Map<String,String> row = getDao().getGroup(id);

        if(null == row){
           System.out.println("Row is null for group ID:"+id);
        } else {
            this.id    = row.get("id");
            this.name  = row.get("group_name");
            this.price = row.get("price");
            this.setVariants(row.get("strVariants"));
        }       
    }

    public ProductGroupModel(String id, String name){
    	this.id = id;
    	this.name = name;
    }
    public ProductGroupModel(String id, String name, String price){
        this.id = id;
        this.name = name;
        this.setPrice(price);
    }

    public boolean save(){
        return getDao().saveGroup(this); 
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
         return this.id;
    }
        
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return this.price;
    }

    public void setVariants(String strVariants){
        this.strVariants = strVariants;
    }
    public String getVariants(){
        return this.strVariants;
    }

    public List<GroupVariantModel> getVariantModels(){
        List<Map<String,String>> variants = getDao().getVariantsByGroup(this.getId());

        List<GroupVariantModel> output = new ArrayList<GroupVariantModel>();
        for( Map<String,String> row : variants ){
            output.add( new GroupVariantModel(row.get("id")) );
        }

        return output;
    }

    public String toString(){
    	return this.getName();
    }
}