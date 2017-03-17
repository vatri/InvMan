package net.vatri.inventory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.collections.ObservableList;

public interface InventoryDao{

	public Map<String,String> getUserByEmail(String email);

	// todo: offset?!
	public List<Map<String,String>> getProducts();
	public Map<String,String> getProduct(String id);
	public boolean saveProduct(ProductModel productModel);

	// Todo: offset + limit
	public List<Map<String,String>> getGroups();
	public Map<String,String> getGroup(String id);
	public boolean saveGroup(ProductGroupModel groupModel);

	public List<Map<String,String>> getOrders();
	public List<Map<String,String>> getOrders(Map<String,String> params);
	public Map<String,String> getOrder(String id);
	public boolean saveOrder(OrderModel orderModel);
	public ObservableList<OrderItem> getOrderItems(String orderId);

	public List<Map<String,String>> getVariantsByGroup(String groupId);
	public List<Map<String,String>> getVariants();
	public Map<String,String> getVariant(String id);
	public boolean saveVariant(GroupVariantModel variant);

	public List<Map<String,String>> getStock();

	public Map<String,String> getStats();

}