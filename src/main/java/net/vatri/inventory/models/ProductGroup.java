package net.vatri.inventory.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product_groups")
public class ProductGroup {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="group_name")
    private String groupName;
    private String price;
    private String created;//Todo - Date type...
    private List<GroupVariant> groupVariants;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productGroup")
    public List<GroupVariant> getGroupVariants() {
        return groupVariants;
    }

    public void setGroupVariants(List<GroupVariant> groupVariants) {
        this.groupVariants = groupVariants;
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}