package net.vatri.inventory;

import net.vatri.activerecord.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ActiveRecordDao implements InventoryDao{
	
	// Table name definitions:
	private static final String TBL_PRODUCTS = "products";
	private static final String TBL_PRODUCT_GROUPS = "product_groups";
	private static final String TBL_USERS = "users";
	private static final String TBL_PRODUCT_GROUP_VARIANTS = "group_variants";

	private static final String TBL_ORDERS = "orders";
	private static final String TBL_ORDER_ITEMS = "order_items";
	// private static final String TBL_PURCHASES = "purchases";

	private ActiveRecord db = new JdbcActiveRecord(App.getConfig("db_connection"));

	/**
	* Setting active record object used for accessing database
	**/
	public void setActiveRecord(ActiveRecord newDb){
		this.db = newDb;
	}
	public ActiveRecord getActiveRecord(){
		return this.db;
	}

	public Map<String,String> getUserByEmail(String email){
		return getActiveRecord().where("email", email)
			.from(TBL_USERS)
			.first();
	}

	// todo: offset?!
	public List<Map<String,String>> getProducts(){
        String strQuery = "SELECT p.* , pg.price as group_price, pg.group_name"
            + " FROM " + TBL_PRODUCTS + " p "
            + " LEFT JOIN " + TBL_PRODUCT_GROUPS + " pg ON pg.id = p.group_id "
            + " ORDER BY p.id DESC ";

        return getActiveRecord().query(strQuery).result();
	}

	// Get product row by ID
	public Map<String,String> getProduct(String id){
		return getActiveRecord().where("id", id).from(TBL_PRODUCTS).first();
	}

	// Save product object to database. Initialy called from Model.save()...
	public boolean saveProduct(ProductModel product){

		if("".equals(product.getName())){
			System.out.println("ActiveRecordDao.saveProduct() - Product name is required!");
			return false;
		}

		Map<String,String> data = new HashMap<String,String>();
		data.put("name", product.getName());
		data.put("price", product.getProductPrice());
		data.put("group_id", product.getGroup());

		if("0".equals(product.getId())){
			// getActiveRecord().insert(TBL_PRODUCTS, data);
			// System.out.println("Inserting product");
			String insertedId = getActiveRecord().insert(TBL_PRODUCTS, data);
		} else {
			getActiveRecord().where("id", product.getId()).update(TBL_PRODUCTS, data);
		}
		return true;
	}

	// Todo: offset + limit
	public List<Map<String,String>> getGroups(){
		return getActiveRecord().orderBy("id", "DESC")
            .from(TBL_PRODUCT_GROUPS)
            .result();
	}

	// Get product group by ID.
	public Map<String,String> getGroup(String id){

		String strQuery = "SELECT `g`.* "
				+ ", group_concat(gv.variant_name) strVariants "
				+ " FROM " + TBL_PRODUCT_GROUPS + " AS `g`"
				+ "LEFT JOIN " + TBL_PRODUCT_GROUP_VARIANTS + " gv "
					+ " ON gv.group_id = `g`.id "
				+ " WHERE `g`.id = " + id
				+ " LIMIT 1";
// System.out.println(strQuery);
		// Map<String,String> row = getActiveRecord().where("id", id).from(TBL_PRODUCT_GROUPS).first();
		return getActiveRecord().query(strQuery).first();
	}

	/**
	* Save product group
	*
	* @param ProductGroupModel Group object.
	* @return boolean
	**/
	public boolean saveGroup(ProductGroupModel group){

		if("".equals(group.getName())){
			System.out.println("ActiveRecordDao.saveProduct() - Product name is required!");
			return false;
		}

		Map<String,String> data = new HashMap<String,String>();
		data.put("group_name", group.getName());
		data.put("price", group.getPrice());

		if("0".equals(group.getId())){
			String insertedId = getActiveRecord().insert(TBL_PRODUCT_GROUPS, data);
			group.setId(insertedId);
		} else {
			getActiveRecord().where("id", group.getId()).update(TBL_PRODUCT_GROUPS, data);
		}

		// group.saveVariants();
		this._saveGroupVariants(group.getId(), group.getVariants());
		return true;
	}

	
	/**
	* Save group variants into database. Delete deleted rows and create new if any.
	*
	* @param String ID of group for which we're saving variants
	* @param String List of variants as string separated with comma
	* @return boolean
	**/
	private boolean _saveGroupVariants(String groupId, String strVariants){

        // Simple validation
        if(groupId.equals("0") || groupId.equals("") || strVariants == null){
            return false;
        }
        String[] newVariantsArray = strVariants.split(",");
        
        /* We need to delete the group's variants which are not entered in the text field by user */
        String sqlDeleteUnNeeded = "DELETE FROM `"+TBL_PRODUCT_GROUP_VARIANTS+"` "
                + " WHERE variant_name NOT IN( ";
        
        for(String tmpVariant : newVariantsArray){
            sqlDeleteUnNeeded += "'" + tmpVariant + "',";
        }

        /* Remove last comma */
        sqlDeleteUnNeeded = sqlDeleteUnNeeded.substring(0, sqlDeleteUnNeeded.length() - 1);
        sqlDeleteUnNeeded += " ) AND group_id = " + groupId
                /* Skip deletion of used variants: */
                + " AND id NOT IN ( " + _getUsedVariantsByGroupId(groupId) + " )";

        getActiveRecord().query(sqlDeleteUnNeeded).executeQuery(); 

        /* Do filter to make sure we insert only new variants */
        _removeExistingVariants(newVariantsArray, groupId);

        for(String variant : newVariantsArray){
            // Skip if empty variant...
            if( variant == null || variant.equals("")){
                continue;
            }

            variant = variant.trim();

            Map<String,String> row = new HashMap<String,String>();
            row.put("group_id", groupId);
            row.put("variant_name", variant);

            getActiveRecord().insert(TBL_PRODUCT_GROUP_VARIANTS,row);
// System.out.println("Inserted variant: "+variant);
        }
        return true;
    }
    /**
    * Return comma-separated string of product variants being in use (e.g. in orders, purchases, etc.)
    * 
    * @param ID of group to filter by
    * @return String 
    **/
    private String _getUsedVariantsByGroupId(String groupId){

        String out = "";

        String strQuery = "SELECT gv.id "
        		+ " FROM `" + TBL_PRODUCT_GROUP_VARIANTS + "` gv "
                + " WHERE gv.group_id = " + groupId + " AND gv.id IN( "
	                    + " SELECT product_variant_id "
	                    	+ " FROM `" + TBL_ORDER_ITEMS + "` "
	                    	+ " WHERE product_variant_id NOT NULL "
                + " ) ";
        
        List<Map<String,String>> res = getActiveRecord().query(strQuery).result();

        for(Map<String,String> row : res){
            // System.out.println(row);
            out += row.get("id");
        }

        /* Remove last comma (if there is at least one item) */
        if(res.size() > 0){
            out = out.substring(0, out.length() - 1);
        }
        return out;
    }

    /**
    * Filter - remove existing product group variants from new variants list
    * @param newVariants String array of new variant values
    * @param groupId ID of group for which we do filter.
    **/
    private void _removeExistingVariants(String[] newVariants, String groupId){

        /* First, let's make an String[] array of existing variants */
        List<Map<String,String>> existingVariants = getActiveRecord().where("group_id", groupId)
            .from("group_variants")
            .result();
        
        String[] oldVariants = new String[existingVariants.size()];
        
        for(int i = 0; i < existingVariants.size(); i++){
            Map<String,String> row = existingVariants.get(i);
            String tmpVariantName = row.get("variant_name");
            oldVariants[i] = tmpVariantName;
        }

        /* Iterate through the new variants and remove existing ones */
        for(int i = 0; i < newVariants.length; i++){

            String tmpVariantName = newVariants[i];

            if(Arrays.asList(oldVariants).contains(tmpVariantName)){
                newVariants[i] = null;
            }
        }
    }




    // ***************** ORDERS:


	// Todo: offset + limit
	public List<Map<String,String>> getOrders(){
		return getActiveRecord().orderBy("id", "DESC")
            .from(TBL_ORDERS)
            .result();
	}

	/**
	* Get list of FILTERED orders
	**/
	public List<Map<String,String>> getOrders(Map<String,String> params){

		ActiveRecord db = getActiveRecord();

		db.orderBy("id", "DESC");

		Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> par = it.next();
			String key = par.getKey();
			String val = par.getValue();

			db.where(key, val);
		}

		db.from(TBL_ORDERS);

		return db.result();
	}


	// Get product group by ID.
	public Map<String,String> getOrder(String id){

		String strQuery = "SELECT * FROM " + TBL_ORDERS + " where id = " + id
				+ " LIMIT 1";
		// Map<String,String> row = getActiveRecord().where("id", id).from(TBL_PRODUCT_GROUPS).first();
		return getActiveRecord().query(strQuery).first();
	}

	/**
	* Save product group
	*
	* @param ProductGroupModel Group object.
	* @return boolean
	**/
	public boolean saveOrder(OrderModel order){

		// Validation is done in Model prior to saving...

		// System.out.println(order.getItems());

		Map<String,String> data = new HashMap<String,String>();

		data.put("name", order.getName());
		data.put("type", order.getType());

		data.put("address", order.getAddress());
		data.put("city", order.getCity());
		data.put("zip", order.getZip());
		data.put("status", order.getStatus());
		data.put("comment", order.getComment());

		if(Integer.parseInt(order.getId() ) < 1){
			String insertedId = getActiveRecord().insert(TBL_ORDERS, data);
			order.setId(insertedId);
		} else {
			getActiveRecord().where("id", order.getId()).update(TBL_ORDERS, data);
		}

		return _saveOrderItems(order);
	}

	private boolean _saveOrderItems(OrderModel order){
		ObservableList<OrderItem> items;

		if( Integer.parseInt(order.getId()) < 1){
			System.out.println("Order not set?!");
			return false;
		}

		getActiveRecord().query("DELETE FROM " + TBL_ORDER_ITEMS + " WHERE order_id = " + order.getId());

		for(OrderItem item : order.getItems()){
//System.out.println(item.getProduct());

			Map<String,String> rowData = new HashMap<String,String>();

			rowData.put("order_id", order.getId());
			rowData.put("product_id", item.getProduct().getId());
			if( item.getVariant() != null){
				rowData.put("product_variant_id", item.getVariant().getId());
			}
			rowData.put("price", item.getPriceValue());
// System.out.println("row data:");
// System.out.println(rowData);
			getActiveRecord().insert(TBL_ORDER_ITEMS, rowData);
		}
		return true;
	}

	public ObservableList<OrderItem> getOrderItems(String orderId){
		List<Map<String,String>> result = getActiveRecord()
			.where("order_id", orderId)
			.from(TBL_ORDER_ITEMS)
			.result();

		ObservableList<OrderItem> items = FXCollections.observableArrayList();

		for(Map<String,String> row : result){
			OrderItem item = new OrderItem(
				new ProductModel(row.get("product_id")),
				new GroupVariantModel(row.get("product_variant_id")),
				row.get("price")
			);
			items.add(item);
		}

		return items;
	}





	public List<Map<String,String>> getVariantsByGroup(String groupId){

		if(groupId != null && Integer.parseInt(groupId) < 1){
			return null;
		}

		String strQuery = "SELECT v.* " 
			+ "FROM " + TBL_PRODUCT_GROUP_VARIANTS + " v "
			+ " WHERE v.group_id = " + groupId;

		return getActiveRecord().query(strQuery).result();
	}
	public List<Map<String,String>> getVariants(){
		return null;//todo
	}
	public Map<String,String> getVariant(String id){
		return getActiveRecord().where("id", id).from(TBL_PRODUCT_GROUP_VARIANTS).first();
	}
	public boolean saveVariant(GroupVariantModel variant){
		return true;//todo
	}

	public List<Map<String,String>> getStock(){

		String strQuery = "SELECT DISTINCT" 
				+ "	p.name as product_name "
				+ "	, gv.variant_name "
				+ " ,(select buy_cnt  - sell_cnt "
				+ " FROM ( "
					+ " SELECT "
						+ "( "
							+ " select count(o.id) "
							+ " from orders o "
							+ " LEFT JOIN order_items oi on oi.order_id = o.id "
							+ " WHERE o.type = 'buy' and oi.product_id = p.id AND oi.product_variant_id = gv.id "
						+ " ) as buy_cnt "
						+ " ,( "
							+ " select count(o2.id) "
							+ " from orders o2 "
							+ " LEFT JOIN order_items oi on oi.order_id = o2.id "
							+ " WHERE o2.type = 'sell' and oi.product_id = p.id AND oi.product_variant_id = gv.id "
							+ " ) as sell_cnt "
					+ " ) as tbl "
				+ " ) as `stock` "
			+ " FROM products p "
			+ " LEFT JOIN group_variants gv ON gv.group_id = p.group_id "
			+ " ORDER BY product_name ";
//System.out.println(strQuery);
		return getActiveRecord().query(strQuery).result();
	}

	public Map<String,String> getStats(){

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM");

		String month1 = formatter.print(LocalDate.now().minusMonths(3) );
		String month2 = formatter.print(LocalDate.now().minusMonths(2) );
		String month3 = formatter.print(LocalDate.now().minusMonths(1) );
		String month4 = formatter.print(LocalDate.now() );

		String strQuery = "SELECT"
				+ " (SELECT count(*) from "+TBL_PRODUCTS+") as products_number "
				+ " ,(SELECT count(*) from "+TBL_ORDERS+") as orders_number "
				+ " ,(SELECT count(*) FROM " + TBL_PRODUCT_GROUPS + " ) as groups_number "
				+ " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month1+"%' ) as orders1"
				+ " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month2+"%' ) as orders2"
				+ " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month3+"%' ) as orders3"
				+ " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month4+"%' ) as orders4"
		;
		return getActiveRecord().query(strQuery).first();
	}



	// ***************** END ORDERS:

}