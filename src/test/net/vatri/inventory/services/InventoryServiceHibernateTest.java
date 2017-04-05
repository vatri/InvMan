package net.vatri.inventory.services;

import junit.framework.TestCase;
import net.vatri.inventory.models.ProductGroup;

public class InventoryServiceHibernateTest extends TestCase {

    // This is required so after saving of this object, we can take ID and save variants for example:
    public void testSaveGroupUpdateIdOfModelObject() throws Exception {
        InventoryService s = new InventoryServiceHibernate();

        ProductGroup g = new ProductGroup();
        g.setGroupName("Test");

        s.saveGroup(g);

        assertTrue(g.getId() > 0);
    }

}