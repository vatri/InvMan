package net.vatri.querybuilder;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public interface QueryBuilder{

	public QueryBuilder select(String select);

	public QueryBuilder where(String fld, String val);
	// public QueryBuilder whereIn(String fld, String[] arr);
	// public QueryBuilder whereNotIn(String fld, String[] arr);

	public QueryBuilder join(String[] newJoin);

	public QueryBuilder from(String tbl);

	public QueryBuilder limit(int offset, int limit);
	public QueryBuilder orderBy(String fld, String type);

	public List< Map<String, String> > result();
	public Map<String,String> first();
	public void executeQuery();

	public String insert(String tbl, Map<String, String> row);
	public boolean update(String tbl, Map<String, String> row);
	public boolean delete(String tbl);

	public QueryBuilder query(String strQuery);
}