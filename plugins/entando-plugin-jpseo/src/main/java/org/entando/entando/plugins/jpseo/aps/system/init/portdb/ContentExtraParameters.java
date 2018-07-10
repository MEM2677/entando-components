/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpseo.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.Content;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContentExtraParameters.TABLE_NAME)
public class ContentExtraParameters implements ExtendedColumnDefinition {
	
	public ContentExtraParameters() {}
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String contentId;
    
    @DatabaseField(columnName = "workxml", dataType = DataType.LONG_STRING)
	private String workxml;
	
	@DatabaseField(columnName = "onlinexml", dataType = DataType.LONG_STRING)
	private String onlinexml;
    
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String contentTableName = Content.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			contentTableName = "`" + contentTableName + "`";
		}
		return new String[] {
            "ALTER TABLE " + TABLE_NAME + " " + "ADD CONSTRAINT " + TABLE_NAME + "_contentid_fk FOREIGN KEY (contentid) "
				+ "REFERENCES " + contentTableName + " (contentid)" };
	}
    
	public static final String TABLE_NAME = "jpseo_contentextraparams";
	
}
/*
CREATE TABLE jpseo_contentextraparams
(
  contentid character varying(16),
  workxml text,
  onlinexml text,
  CONSTRAINT jpseo_contentextraparams_pkey PRIMARY KEY (contentid),
  CONSTRAINT jpseo_contentextraparams_pkey_fkey FOREIGN KEY (contentid)
      REFERENCES contents (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
*/
