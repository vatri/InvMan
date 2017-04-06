package net.vatri.inventory.services;

import net.vatri.inventory.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryServiceHibernate implements InventoryService {

    private SessionFactory sessionFactory;

    // Table name definitions:
    private static final String TBL_PRODUCTS = "products";
    private static final String TBL_PRODUCT_GROUPS = "product_groups";
    private static final String TBL_USERS = "users";
    private static final String TBL_PRODUCT_GROUP_VARIANTS = "group_variants";

    private static final String TBL_ORDERS = "orders";
    private static final String TBL_ORDER_ITEMS = "order_items";
    // private static final String TBL_PURCHASES = "purchases";

    public InventoryServiceHibernate(SessionFactory sessionFactory) {
        System.out.println("sessionFactory:");
        System.out.println(sessionFactory);

        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUserByEmail(String email) {
        Session s = getSessionFactory().openSession();

        User user = (User) s.createQuery( "FROM User WHERE email = :email" )
                .setParameter( "email", email )
                .uniqueResult();

        s.close();
System.out.println("USER:");
System.out.println(user);
        return user;
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
        Session session = getSessionFactory().openSession();

        if(group.getId() != null){
            session.save(group);
        } else {
            session.update(group);
        }
        session.close();
        return true;
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
    public boolean saveGroupVariants(ProductGroup group, String strVariants) {
        return false;
    }

    @Override
    public String getGroupVariantsAsString(ProductGroup group){
        if(group.getGroupVariants() == null || group.getGroupVariants().size() < 1) {
            return "";
        }

        String out = "";
        for (GroupVariant gv : group.getGroupVariants()) {
            out += gv.getVariantName() + ",";
        }
        out = out.substring(0, out.length() - 1);
        return out;
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
    public List<Map<String, String>> getStock() {
        return null;
    }

    @Override
    public Map<String, String> getStats() {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM");

            String month1 = formatter.print(LocalDate.now().minusMonths(3) );
            String month2 = formatter.print(LocalDate.now().minusMonths(2) );
            String month3 = formatter.print(LocalDate.now().minusMonths(1) );
            String month4 = formatter.print(LocalDate.now() );

            String strQuery = "SELECT"
                    + " (SELECT count(*) from "+TBL_PRODUCTS+") as products_number "
                    + " ,(SELECT count(*) from "+TBL_ORDERS+") as orders_number "
                    + " ,(SELECT count(*) FROM " + TBL_PRODUCT_GROUPS + " ) as groups_number "
                    + " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month1+"%' ) as orders1"
                    + " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month2+"%' ) as orders2"
                    + " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month3+"%' ) as orders3"
                    + " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month4+"%' ) as orders4"
                    ;
//            return getQueryBuilder().query(strQuery).first();
        Session session = getSessionFactory().openSession();
        Object[] res = (Object[]) session.getEntityManagerFactory().createEntityManager().createNativeQuery(strQuery).getSingleResult();

        session.close();

        Map<String,String> out = new HashMap<String,String>();
        out.put("products_number", res[0].toString());
        out.put("orders_number", res[1].toString());
        out.put("groups_number", res[2].toString());
        out.put("orders1", res[3].toString());
        out.put("orders2", res[4].toString());
        out.put("orders3", res[5].toString());
        out.put("orders4", res[6].toString());

        System.out.println("out");
        System.out.println(out);

        return out;
    }
}
