package net.vatri.inventory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FxPageSwitcher{


	private List<FxPage> pages = Arrays.asList(
			new FxPage("login", "LoginView"),
			new FxPage("dashboard", "DashBoardView"),
			new FxPage("products", "ProductsView"),
            new FxPage("newProduct", "AddEditProductView"),
			new FxPage("groups", "GroupsView"),
			new FxPage("addEditGroup", "AddEditGroupView"),
			new FxPage("orders", "OrdersView"),
            new FxPage("addEditOrder", "AddEditOrderView"),
            new FxPage("stock", "StockView")
    );

	private String currentPage;

	private BorderPane pane;

	public FxPageSwitcher(BorderPane pane){
	    this.pane = pane;
    }

	public void showPage(String page){

	    try {
            FxPage selectedPage = pages.stream().filter((pg) -> pg.getPageName() == page).findFirst().get();
            String viewFile = selectedPage.getPageFile();
            Pane view = new FxView(viewFile).get();
            pane.setCenter(view);
        } catch (Exception e){
	        System.out.println("No page " + page + " please check FxPageSwitcher.");
        }
	}
}