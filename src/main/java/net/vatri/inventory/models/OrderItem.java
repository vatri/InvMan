package net.vatri.inventory;

import javafx.scene.control.TextField;


public class OrderItem{

	private ProductModel product;
	private GroupVariantModel variant;
	private TextField price;

	public OrderItem(ProductModel product, GroupVariantModel variant){
		this.product = product;
		this.variant = variant;
		this.setPrice( product.getPrice() );
	}

	public OrderItem(ProductModel product, GroupVariantModel variant, String price){
		this(product,variant);//calls another constructor
		this.setPrice( price );
	}

	public String getProductName(){
		return product.toString();
	}
	public ProductModel getProduct(){
		return product;
	}

	public String getVariantName(){
		return variant != null ? variant.toString() : "-";
	}
	public GroupVariantModel getVariant(){
		return variant;
	}

	public TextField getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = new TextField( price );
	}
	public String getPriceValue(){
		return price.getText();
	}

}