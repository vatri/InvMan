package net.vatri.inventory.services;

import net.vatri.inventory.models.*;

import java.util.List;
import java.util.Map;

/**
 * Created by boris on 5.4.17..
 */
public class InventoryServiceHibernate implements InventoryService {
    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return null;
    }

    @Override
    public Product getProduct(String id) {
        return null;
    }

    @Override
    public boolean saveProduct(Product product) {
        return false;
    }

    @Override
    public List<ProductGroup> getGroups() {
        return null;
    }

    @Override
    public ProductGroup getGroup(String id) {
        return null;
    }

    @Override
    public boolean saveGroup(ProductGroup group) {
        return false;
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }

    @Override
    public List<Order> getOrders(Map<String, String> params) {
        return null;
    }

    @Override
    public Order getOrder(String id) {
        return null;
    }

    @Override
    public boolean saveOrder(Order order) {
        return false;
    }

    @Override
    public List<OrderItem> getOrderItems(String orderId) {
        return null;
    }

    @Override
    public List<GroupVariant> getVariantsByGroup(String groupId) {
        return null;
    }

    @Override
    public List<GroupVariant> getVariants() {
        return null;
    }

    @Override
    public GroupVariant getVariant(String id) {
        return null;
    }

    @Override
    public boolean saveVariant(GroupVariant variant) {
        return false;
    }

    @Override
    public List<Map<String, String>> getStock() {
        return null;
    }

    @Override
    public Map<String, String> getStats() {
        return null;
    }
}
