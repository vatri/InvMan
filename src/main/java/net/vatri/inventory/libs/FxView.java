package net.vatri.inventory.libs;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import net.vatri.inventory.App;

import java.net.URL;

public class FxView extends Pane {

    private Pane fxElement = null;

    public FxView(String fileName){

        try {
            URL fileUrl = App.class.getResource("/views/" + fileName + ".fxml");
            if(fileUrl == null){
                throw new java.io.FileNotFoundException("FXML file can't be found");
            }
            fxElement = new FXMLLoader().load(fileUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // System.out.println(e.printStackTrace());
//            e.printStackTrace();
        }
    }

    public Pane get(){
        return fxElement;
    }


}
