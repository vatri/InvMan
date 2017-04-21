package net.vatri.inventory;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class FxView extends Pane {

    private Pane fxElement = null;

    public FxView(String fileName){

        try {


            String viewPath = System.getProperty("user.dir") + "/src/main/resources/views/";
            java.net.URL viewRes = new java.net.URL("file://" + viewPath + fileName + ".fxml");
            fxElement = FXMLLoader.load(viewRes);

        } catch (Exception e) {
            System.out.println(e.getCause());
            // System.out.println(e.printStackTrace());
            e.printStackTrace();
        }
    }

    public Pane get(){
        return fxElement;
    }


}
