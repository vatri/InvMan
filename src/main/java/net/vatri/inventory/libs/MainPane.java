package net.vatri.inventory.libs;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane implements IMainPane{
    @Override
    public void setPage(Node view) {
        super.setCenter(view);
    }
}
