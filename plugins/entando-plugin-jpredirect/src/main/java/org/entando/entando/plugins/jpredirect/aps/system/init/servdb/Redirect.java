/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Redirect.TABLE_NAME)
public class Redirect {
	
	public Redirect() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "pagecode", 
		dataType = DataType.STRING, 
		width=30,  canBeNull=false)
	private String _pagecode;
	
	@DatabaseField(columnName = "groupname", 
		dataType = DataType.STRING, 
		width=20,  canBeNull=true)
	private String _groupname;
	
	@DatabaseField(columnName = "rolename", 
		dataType = DataType.STRING, 
		width=20,  canBeNull= true)
	private String _rolename;
	

public static final String TABLE_NAME = "jpredirect_redirect";
}
