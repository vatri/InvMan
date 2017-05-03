package net.vatri.inventory.libs;

public class FxPage{

	private String pageName, pageFile;

	public FxPage(String name, String file){
		this.pageName = name;
		this.pageFile = file;
	}

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageFile() {
        return pageFile;
    }

    public void setPageFile(String pageFile) {
        this.pageFile = pageFile;
    }
}