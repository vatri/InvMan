package net.vatri.inventory.services;
import net.vatri.inventory.models.*;

import java.util.List;
import java.util.Map;

public interface InventoryService{

    public User getUserByEmail(String email);

    // todo: offset?!
    public List<Product> getProducts();
    public Product getProduct(String id);
    public boolean saveProduct(Product product);

    // Todo: offset + limit
    public List<ProductGroup> getGroups();
    public ProductGroup getGroup(String id);
    public boolean saveGroup(ProductGroup group);
    public String getGroupVariantsAsString(ProductGroup group);

    public List<GroupVariant> getVariantsByGroup(String groupId);
    public List<GroupVariant> getVariants();
    public GroupVariant getVariant(String id);
    public boolean saveVariant(GroupVariant variant);
    public boolean saveGroupVariants(ProductGroup group, String text);



    public List<Order> getOrders();
    public List<Order> getOrders(Map<String,String> params);
    public Order getOrder(String id);
    public boolean saveOrder(Order order);
    public List<OrderItem> getOrderItems(String orderId);

    public List<StockModel> getStock();

    public Map<String,String> getStats();

}