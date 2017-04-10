package net.vatri.inventory.services;

import net.vatri.inventory.models.*;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Query;
import java.util.*;


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
        this.setSessionFactory(sessionFactory);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Get list of Hibernate entities
     * @param name Name of Entity
     * @return List
     */
    private List list(String name){
        Session session = getSessionFactory().openSession();
        try {
            return session.createQuery("FROM " + name).list();
        } finally {
            session.close();
        }
    }

    /**
     * Get hibernate entity by ID (primary key)
     * @param entity For example: Product.class
     * @param id Id of entity to get
     * @return Object
     */
    private Object get(Class entity, String id){
        Session session = getSessionFactory().openSession();
        Object res = session.get(entity, Integer.parseInt(id));
        session.close();
        return res;
    }

    private boolean save(Object entity, Integer id){
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        try {
            if (id != null) {
                session.update(entity);
            } else {
                session.save(entity);
            }
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e){
            System.out.print(e.getMessage());
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
    }





    @Override
    public User getUserByEmail(String email) {
        Session session = getSessionFactory().openSession();
        User user = (User) session.createQuery( "FROM User WHERE email = :email" )
                .setParameter( "email", email )
                .uniqueResult();

        session.close();
        return user;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> list = (List<Product>) this.list("Product");
        return list;
    }

    @Override
    public Product getProduct(String id) {
        return (Product) this.get(Product.class, id);
    }

    @Override
    public boolean saveProduct(Product product) {
        return this.save(product, product.getId());
    }

    @Override
    public List<ProductGroup> getGroups() {
        return (List<ProductGroup>) this.list("ProductGroup");
    }

    @Override
    public ProductGroup getGroup(String id) {
        return (ProductGroup) this.get(ProductGroup.class, id);
    }

    @Override
    public boolean saveGroup(ProductGroup group) {
        return this.save(group, group.getId());
    }

    @Override
    public List<GroupVariant> getVariantsByGroup(String groupId) {
        return null;
    }

    @Override
    public List<GroupVariant> getVariants() {
        return (List<GroupVariant>) this.list("GroupVariant");
    }

    @Override
    public GroupVariant getVariant(String id) {
        return (GroupVariant) this.get(GroupVariant.class, id);
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

        // Make group Entity attached so that lazy loading works:
        Session session = getSessionFactory().openSession();
        session.update(group);

        List<GroupVariant> groupVariants = group.getGroupVariants();

        if(groupVariants == null || groupVariants.size() < 1) {
            return "";
        }

        String out = "";
        for (GroupVariant gv : groupVariants) {
            out += gv.getVariantName() + ",";
        }
        out = out.substring(0, out.length() - 1);

        session.close();
        return out;
    }

    @Override
    public List<Order> getOrders() {
        return (List<Order>) this.list("Order");
    }

    @Override
    public List<Order> getOrders(Map<String, String> params) {

        Session session = getSessionFactory().openSession();

        Query query = session.createQuery("FROM Order");

        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> par = it.next();
            String key = par.getKey();
            String val = par.getValue();

            query.setParameter(key, val);
        }

        List<Order> list = (List<Order>) query.getResultList();
        session.close();

        return list;
    }

    @Override
    public Order getOrder(String id) {
        return (Order) this.get(Order.class, id);
    }

    @Override
    public boolean saveOrder(Order order){
        return this.save(order, order.getId());
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
                    + " , (SELECT count(*) FROM " + TBL_ORDERS + " where created LIKE '%"+month4+"%' ) as orders4";

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

        return out;
    }
}
