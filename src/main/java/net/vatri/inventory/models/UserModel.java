package net.vatri.inventory;

// import java.sql.*;
import java.util.Map;


public class UserModel extends BaseModel {

	// ID defined in BaseModel
	// public String id = "0";
	public String email = "";
	public String password = "";

	public void loadByEmail(String email){
		Map<String,String> row = getDao().getUserByEmail(email);

		if(row != null && row.get("id") != null){
			setId(row.get("id"));
			setEmail(row.get("email"));
			setPassword(row.get("password"));
		}
	}

	//Email
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return this.email;
	}

	// Password
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}



}