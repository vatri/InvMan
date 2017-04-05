package net.vatri.inventory.models;

import javax.persistence.*;

@Entity
@Table(name="group_variants")
public class GroupVariant {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="variant_name")
    private String variantName;

    private ProductGroup order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public ProductGroup getOrder() {
        return order;
    }

    public void setOrder(ProductGroup order) {
        this.order = order;
    }
}
