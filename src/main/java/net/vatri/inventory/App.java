package net.vatri.inventory;

import java.util.Map;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
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

    private FxPageSwitcher pageSwitcher = new FxPageSwitcher(mainPane);

    @Override
    public void start(Stage primaryStage) {

//        mainMenu = getView("Menu");
        mainMenu = new FxView("Menu").get();
        mainMenu.setVisible(false);

        mainPane.setLeft(mainMenu);

        primaryStage.setScene(new Scene(mainPane, 800, 600));
        primaryStage.setTitle("BeeInventory - Inventory Management");
        primaryStage.show();

//        showPage("login");
        pageSwitcher.showPage("login");
    }

    @Override
    public void stop() throws Exception {
        getInstance().sessionFactory.close();
    }

    public static void showPage(String page){
        if(page != "login"){
            mainMenu.setVisible(true);
        }
        getInstance().pageSwitcher.showPage(page);
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