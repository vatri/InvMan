package net.vatri.inventory.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    private Integer id;

    private Order order;
    private Product product;
    private GroupVariant groupVariant;

    private String price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public GroupVariant getGroupVariant() {
        return groupVariant;
    }

    public void setGroupVariant(GroupVariant groupVariant) {
        this.groupVariant = groupVariant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
