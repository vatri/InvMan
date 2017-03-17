package net.vatri.activerecord;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

import java.sql.*;

public class JdbcActiveRecord implements ActiveRecord{
	
	/**
	* SQL variable parts... 
	*
	*   (WARNING) Don't forget to clear all these params in _clearActiveRecord() !
	*
	**/ 

	private String                _select = "";

	private String                _tbl = "";
	private Map<String, String>   _where = new HashMap<String, String>();
	
	/* Values for LIMIT part */
	private int                   _offset = 0;
	private int                   _limit = 500;
	
	/* Values for ORDER BY part: */
	private String                _orderByField = "";
	private String                _orderByType = "ASC";

	/* Array of join rules tables and fields */
	private List<String[]>        _joins = new ArrayList<String[]>();

	private String                _query = "";

	/* JDBC connection properties */
	private String                connectionUrl;

	// Bug: one method can close connection while an another method still uses it...
		// private Connection          connection;

	public JdbcActiveRecord(String dbUrl){
		this.connectionUrl = dbUrl;
	}

	public JdbcActiveRecord select(String sel){
		_select = sel;
		return this;
	}

	/**
	* Append multiple filters before running the query. 
	* To append "WHERE id = 1" we simply call ...where("id","1")
	**/
	public JdbcActiveRecord where(String fld, String val){
		this._where.put(fld, val);
		return this;
	}
	// public JdbcActiveRecord whereIn(String fld, String[] arr){
	// 	// this._where.put(fld, val);
	// 	return this;
	// }
	// public JdbcActiveRecord whereNotIn(String fld, String[] arr){
	// 	// this._where.put(fld, val);
	// 	return this;
	// }

	/**
	* Set table to select from.
	**/
	public JdbcActiveRecord from(String tbl){
		this._tbl = tbl;
		return this;
	}

	/**
	* Join tables
	**/
	public JdbcActiveRecord join(String[] join){
		this._joins.add(join);
		return this;
	}

	/**
	* Set custom made query to be run
	**/
	public ActiveRecord query(String strQuery){
		this._query = strQuery;
		return this;
	}


	/**
	* Return first row from a query which is created
	**/
	public Map<String,String> first(){

		Map<String, String> output = new HashMap<String, String>();

		String finalSql = _generateFinalQuery();

		try{

			Connection connection = _openConnection();

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(finalSql);

			/* Map columns from query to output / resulting row: */
			rs.next();
			for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
				String colName = rs.getMetaData().getColumnLabel(i);
				output.put(rs.getMetaData().getColumnLabel(i), rs.getString(colName));
			}

			rs.close();
			stmt.close();
			connection.close();

		} catch(Exception e){ 
			System.out.println("ActiveRecord.first() can't run query... " + finalSql );
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}

		_clearActiveRecord();

		return output;
	}

	/**
	* List of results based on previous get(), where() and other methods.
	**/
	public List< Map<String, String>> result(){
		//todo...

		List< Map<String, String> > output = new ArrayList< Map<String, String> >();

		String finalSql = _generateFinalQuery();

		try{

			Connection connection = _openConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(finalSql);

			/* Loop through columns and their values and put into resulting List */
			while(rs.next()){
				Map<String, String> rowCols = new HashMap<String, String>();
				for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
					String colName = rs.getMetaData().getColumnLabel(i);
					rowCols.put(colName, rs.getString(colName));
				}
// System.out.println("Adding row...");
				output.add(rowCols);
			}

			rs.close();
			stmt.close();
			connection.close();

		} catch(Exception e){
			System.out.println("result() can't run query: " + finalSql);
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}

		_clearActiveRecord();

		return output;
	}

	/**
	* We execute this when no result is needed.
	**/
	public void executeQuery(){

		String finalSql = _generateFinalQuery();

		try{
			Connection connection = _openConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(finalSql);
			stmt.close();
			connection.close();
		} catch(Exception e){
			System.out.println("executeQuery() can't run query: " + finalSql);
			System.out.println(e.getMessage());
		}

		_clearActiveRecord();
	}

	/**
	* Insert row into table
	*
	* @return String ID of inserted row
	**/
	public String insert(String tbl, Map<String, String> row){
		
		/* Method's output - ID of last inserted row */
		String output = "0";

		/* Prepare a SQL string for columns and for values */
		String sqlCols = " ( ";
		String sqlVals = " ( ";

		/* Go through the Map and add cols and values to the SQL string: */
		for (Map.Entry<String, String> entry : row.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();

			sqlCols += "`"+key+"`,";
			sqlVals += "'"+value+"',";
		}

		/* Remove the comma before closing bracket: */
		sqlCols = sqlCols.substring(0,sqlCols.length()-1);
		sqlVals = sqlVals.substring(0,sqlVals.length()-1);

		/* Close brackets */
		sqlCols += " ) ";
		sqlVals += " ) ";

		/* Generate and run final query */
		String sqlInsert = "INSERT INTO `" + tbl + "` " + sqlCols 
			+ " VALUES " + sqlVals;

		String oldId = this.orderBy("id", "DESC").from(tbl).first().get("id");

		/* Preventing NullPointerException if there is no rows in below command */
		if(oldId == null){
			oldId = "0";
		}

		try{

			Connection connection = _openConnection();
			Statement statement = connection.createStatement();

			statement.executeUpdate(sqlInsert);

			statement.close();
			connection.close();

		} catch(Exception e){
			System.out.println("ActiveRecord.insert() can't run query: " + sqlInsert);
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}

		/* This is last row's id BEFORE insert. We will compare this from one after query. */
		String insertedRowId = this.orderBy("id", "DESC").from(tbl).first().get("id");

		/* Compare rows from before and after inserting */
		if(insertedRowId != null){
			if( ! insertedRowId.equals(oldId) ){
				output = insertedRowId;
			}
		}

		return output;
	}

	public boolean update(String tbl, Map<String, String> row){

		/* Method's output */
		boolean output = false;

		String strQuery = "UPDATE " + tbl + " SET ";

		for (Map.Entry<String, String> entry : row.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();

		    strQuery += String.format("`%s` = '%s',", key, value);
		}
		
		/* Remove last comma: */
		strQuery = strQuery.substring(0, strQuery.length() - 1);

		strQuery += _generateSqlWhere();

		try{

			Connection connection = _openConnection();
			Statement statement = connection.createStatement();
			int rows = statement.executeUpdate(strQuery);
			output = rows > 0;

			// rs.close();
			statement.close();
			connection.close();

		} catch(Exception e) {
			System.out.println("JDBC Active Record.update() can't run query: " + strQuery);
			System.out.println(e.getMessage());
		}

		// System.out.println("Running query "+strQuery);

		_clearActiveRecord();

		return output;
	}

	public boolean delete(String tbl){
		if(this._where.size() < 1){
			System.out.println("where() call is required!");
			return false;
		}

		String sqlDelete = "DELETE FROM " + tbl;
		sqlDelete += _generateSqlWhere();

//System.out.println("SQL Delete = "+sqlDelete);

		try{

			Connection connection = _openConnection();
			Statement statement = connection.createStatement();

			statement.execute(sqlDelete);

			connection.close();
		} catch(Exception e){
			System.out.println("JDBC Active Record.delete() can't run query: " + sqlDelete);
			System.out.println(e.getMessage());
			return false;
		}

		_clearActiveRecord();

		return true;
	}


	public ActiveRecord orderBy(String fld, String type){
		this._orderByField = fld;
		this._orderByType = type;
		return this;
	}

	public JdbcActiveRecord limit(int offset, int limit){
		this._offset = offset;
		this._limit  = limit;
		return this;
	}

	/**
	* Get final query string based on previous calls like .where() , .from() etc
	**/
	private String _generateFinalQuery(){

		if(! _query.equals("")){
			return _query;
		}

		if( _tbl.equals("") ){
			System.out.println("No table set... You need to call .from() method of this class.");
			return "";
		}

		String strQuery = "SELECT ";

		if( _select.equals("")){
			strQuery += " * ";
		} else {
			strQuery += _select;
		}

		/* Append select fields from joined tables: */
		// strQuery += _appendJoinedSelectFields();

		strQuery += " FROM " + this._tbl;
		strQuery += _generateSqlJoins();
		strQuery += _generateSqlWhere();
		
		if( ! _orderByField.equals("") ){
			strQuery += " ORDER BY " + _orderByField + " " + _orderByType;
		}

		// System.out.println("Active Record final query = "+strQuery);

		return strQuery;
	}

	private String _generateSqlJoins(){
		if(_joins.size() < 1){
			return "";
		}

		String out = "";
		for(String[] joinItem : _joins){
			out += " JOIN " + joinItem[0] 
				+ " ON " + joinItem[0] + "." + joinItem[1] 
				+ " = " + this._tbl + "." + joinItem[2];
		}
		return out;
	}

	/**
	* Get a string with WHERE statement based on previous calls of where() method
	**/ 
	private String _generateSqlWhere(){
 		String strSqlWhere = " ";

		if(this._where.size() > 0){
			strSqlWhere += " WHERE ";
			for (Map.Entry<String, String> entry : _where.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    strSqlWhere += "`" + key + "` = '" + value + "' AND ";
			}

			/* Remove space and last "AND" from the SQL: */ 
			strSqlWhere = strSqlWhere.substring(0, strSqlWhere.length()-4);
		}
		return strSqlWhere;
	}

	private void _clearActiveRecord(){
		this._where.clear();
		this._tbl = "";
		this._joins.clear();
		this._query = "";
		this._select = "";
		this._orderByField = "";
		this._orderByType = "ASC";
	}


	private Connection _openConnection(){
		Connection connection = null;
		try{
			connection = DriverManager.getConnection(this.connectionUrl);
		} catch(Exception e){
			System.out.println("Can't connect to database");
		}
		return connection;
	}

	// private void _closeConnection(){
	// 	try{
	// 		connection.close();
	// 	} catch(Exception e){ }
	// }
}