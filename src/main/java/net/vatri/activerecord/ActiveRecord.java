package net.vatri.activerecord;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public interface ActiveRecord{

	public ActiveRecord select(String select);

	public ActiveRecord where(String fld, String val);
	// public ActiveRecord whereIn(String fld, String[] arr);
	// public ActiveRecord whereNotIn(String fld, String[] arr);

	public ActiveRecord join(String[] newJoin);

	public ActiveRecord from(String tbl);

	public ActiveRecord limit(int offset, int limit);
	public ActiveRecord orderBy(String fld, String type);

	public List< Map<String, String> > result();
	public Map<String,String> first();
	public void executeQuery();

	public String insert(String tbl, Map<String, String> row);
	public boolean update(String tbl, Map<String, String> row);
	public boolean delete(String tbl);

	public ActiveRecord query(String strQuery);
}