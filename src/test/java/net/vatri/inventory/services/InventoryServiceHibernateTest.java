package net.vatri.inventory.services;

import junit.framework.TestCase;
import net.vatri.inventory.models.GroupVariant;
import net.vatri.inventory.models.ProductGroup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class InventoryServiceHibernateTest extends TestCase {

    private InventoryService inventoryService;

    @Override
    public void setUp(){
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Session sess = mock(Session.class);

        when(sessionFactory.openSession()).thenReturn(sess);
        when(sess.save(new Object())).thenReturn(null);

        inventoryService = new InventoryServiceHibernate(sessionFactory);
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
            if(vn.length() < 1){ continue; }
            GroupVariant gv = new GroupVariant();
            gv.setVariantName(vn);
            list.add(gv);
        }
        return list;
    }

}