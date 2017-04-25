package net.vatri.inventory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class FxPageSwitcher{

	private List<FxPage> pages;

	private String currentPage;

	private BorderPane pane;

	public FxPageSwitcher(BorderPane pane, List<FxPage> pages){
	    this.pane = pane;
	    this.pages = pages;
    }

	public void showPage(String page){

	    try {
            FxPage selectedPage = pages.stream()
                    .filter((pg) -> pg.getPageName() == page)
                    .findFirst()
                    .get();

            if(selectedPage == null){
                System.out.println("Can't find page " + page);
                return ;
            }
            String viewFile = selectedPage.getPageFile();
            Pane view = new FxView(viewFile).get();
            pane.setCenter(view);
        } catch (Exception e){
	        System.out.println("No page " + page + " please check FxPageSwitcher.");
        }
	}
}