package net.vatri.inventory;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App extends Application {

    private static BorderPane mainPane = new BorderPane();
    private static Parent mainMenu;
    private static Map<String, String> _config;

    static {
        _config = new HashMap<String, String>();
        _config.put("db_connection", "jdbc:sqlite:InvMan.sqlite3");
    }

    /**
     * Data repository for exchange between controllers.
     **/
    public Map<String, String> repository = new HashMap<String, String>();

    /**
     * Variable for the singleton pattern...
     **/
    private static App instance = null;

    /**
     * Hibernate session factory
     **/
    private SessionFactory sessionFactory = null;

    @Override
    public void start(Stage primaryStage) {

        mainMenu = getView("Menu");

        mainPane.setLeft(mainMenu);

        primaryStage.setScene(new Scene(mainPane, 800, 600));
        primaryStage.setTitle("BeeInventory - Inventory Management");
        primaryStage.show();

        showPage("login");
    }

    @Override
    public void stop() throws Exception {
        getInstance().sessionFactory.close();
    }


    public static void showPage(String page) {

        System.out.println("Showing page:" + page);

        String viewFile;

        switch (page) {

            case "dashboard":
                viewFile = "DashBoardView";
                mainMenu.setVisible(true);
                break;

            case "products":
                viewFile = "ProductsView";
                break;
            // TODO: change to addEditController
            case "newProduct":
                viewFile = "AddEditProductView";
                break;

            case "groups":
                viewFile = "GroupsView";
                break;

            case "addEditGroup":
                viewFile = "AddEditGroupView";
                break;
            case "orders":
                viewFile = "OrdersView";
                break;

            case "addEditOrder":
                viewFile = "AddEditOrderView";
                break;

            case "stock":
                viewFile = "StockView";
                break;

            default:
            case "login":
                viewFile = "LoginView";
                mainMenu.setVisible(false);
                break;
        }

        mainPane.setCenter(getView(viewFile));
    }

    /**
     * Load JavaFX (fxml) view file
     **/
    public static Parent getView(String viewFile) {
        Parent activeElement = null;
        try {


//            String viewPath = System.getProperty("user.dir") + "/src/main/resources/views/";

            String viewPath = new StringBuilder(System.getProperty("user.dir"))
                    .append(File.separator).append("src")
                    .append(File.separator).append("main")
                    .append(File.separator).append("resources")
                    .append(File.separator).append("views")
                    .append(File.separator).toString();

            java.net.URL viewRes = new java.net.URL("file://" + viewPath + viewFile + ".fxml");
            activeElement = FXMLLoader.load(viewRes);

        } catch (Exception e) {
            System.out.println(e.getCause());
            // System.out.println(e.printStackTrace());
            e.printStackTrace();
        }
        return activeElement;
    }

    public static String getConfig(String item) {
        return _config.get(item);
    }

    /**
     * Return signleton instance...
     **/
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                StandardServiceRegistryBuilder.destroy(registry);
                System.out.println(e.getMessage());
            }
        }
        return sessionFactory;
    }

    public static void main(String[] args) {
        launch(args);
    }
}