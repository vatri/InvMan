package net.vatri.inventory.services;

import junit.framework.TestCase;
import net.vatri.inventory.models.GroupVariant;
import net.vatri.inventory.models.ProductGroup;

import java.util.ArrayList;
import java.util.List;

public class InventoryServiceHibernateTest extends TestCase {

    private InventoryService inventoryService;

    @Override
    public void setUp(){
        inventoryService = new InventoryServiceHibernate();
    }

    // This is required so after saving of this object, we can take ID and save variants for example:
    public void testSaveGroupUpdateIdOfModelObject() throws Exception {

        ProductGroup g = new ProductGroup();
        g.setGroupName("Test");

        inventoryService.saveGroup(g);

        assertTrue(g.getId() > 0);
    }

    public void testGroupVariantsSetAndGetAsString(){
        ProductGroup group = new ProductGroup();

        // Test no variants set:
        assertEquals(
                "",
                inventoryService.getGroupVariantsAsString(group)
        );

        // Test if last comma is ignored
        group.setGroupVariants( this.getProductGroupVariants("M,L,") );

        assertEquals(
                "M,L",
                inventoryService.getGroupVariantsAsString(group)
        );

        // Test empty string
        group.setGroupVariants( this.getProductGroupVariants("") );
        assertEquals(
                "",
                inventoryService.getGroupVariantsAsString(group)
        );
        // Test invalid variant name inside string
        group.setGroupVariants( this.getProductGroupVariants("M,,L") );
        assertEquals(
                "M,L",
                inventoryService.getGroupVariantsAsString(group)
        );
    }


    private List<GroupVariant> getProductGroupVariants(String variants){
        List<GroupVariant> list = new ArrayList<GroupVariant>();
        for(String vn : variants.split(",")){
            GroupVariant gv = new GroupVariant();
            gv.setVariantName(vn);
            list.add(gv);
        }
        return list;
    }

}