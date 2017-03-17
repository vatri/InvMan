package net.vatri.inventory;

// import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Arrays;

public class GroupVariantModel extends BaseModel{

	// private String id = "0";
    private String groupId = "";
    private String variantName = "0";

    // List of all product variants
    public List<Map<String,String>> all(){
        return getDao().getVariants();
    }

    public GroupVariantModel(){ }

    public GroupVariantModel(String id){
        Map<String,String> row = getDao().getVariant(id);

        if(null == row){
           System.out.println("Row is null for group variant ID:"+id);
        } else {
            // System.out.println(row);
            this.setId(row.get("id"));
            this.setGroup(row.get("group_id"));
            this.setVariantName(row.get("variant_name"));
        }
    }

    public GroupVariantModel(String id, String groupId, String variantName){
        this.setId(id);
        this.setGroup(groupId);
        this.setVariantName(variantName);
    }

    public boolean save(){
        return getDao().saveVariant(this); 
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
         return this.id;
    }
        
    public void setVariantName(String variantName){
        this.variantName = variantName;
    }
    public String getVariantName(){
        return this.variantName;
    }


    public void setGroup(String groupId){
        this.groupId = groupId;
    }
    public String getGroup(){
        return this.groupId;
    }
   

    public String toString(){
    	return this.getVariantName();
    }
}