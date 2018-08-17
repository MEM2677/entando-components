/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Mapping.TABLE_NAME)
public class Mapping {

	public Mapping() {}

	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull=false, id = true)
	private int _id;

	@DatabaseField(columnName = "jrole", 
			dataType = DataType.LONG_STRING,
			canBeNull=false)
	private String _jrole;

	@DatabaseField(columnName = "egroup", 
			dataType = DataType.STRING, 
			width=20,  canBeNull= true)
	private String _egroup;

	@DatabaseField(columnName = "erole", 
			dataType = DataType.STRING, 
			width=20,  canBeNull= true)
	private String _erole;


	public static final String TABLE_NAME = "jpjaasauth_mapping";
}
