package net.vatri.inventory;

import java.sql.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.util.Iterator;

 import net.vatri.querybuilder.*;

public abstract class BaseModel{
	
	protected String id = "0";

	/**
	* Database access object. 
	* Note that this can be changed with setDao()
	**/
	protected InventoryDao dao = new QueryBuilderDao( new JdbcQueryBuilder(App.getConfig("db_connection")) );

	public BaseModel(){ }

	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}

	/* DAO */
	public void setDao(InventoryDao newDao){
		this.dao = newDao;
	}
	public InventoryDao getDao(){
		return dao;
	}

}
