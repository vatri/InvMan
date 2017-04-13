package net.vatri.inventory.models;

import javafx.scene.control.TextField;

import javax.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {

    private Integer id;
    private Order order;
    private Product product;
    private GroupVariant groupVariant;
    private String price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name="product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
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
