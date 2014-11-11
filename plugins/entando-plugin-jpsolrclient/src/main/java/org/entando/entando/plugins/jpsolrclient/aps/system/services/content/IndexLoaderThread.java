/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsolrclient.aps.system.services.content;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Thread Class delegate to load content index, in use on SearchEngine.
 * @author E.Santoboni
 */
public class IndexLoaderThread extends Thread {

	private static final Logger _logger =  LoggerFactory.getLogger(IndexLoaderThread.class);

	public IndexLoaderThread(CmsSearchEngineManager searchEngineManager,
			IContentManager contentManager, Indexer indexer, List<String> oldContentsId) {
		this._contentManager = contentManager;
		this._searchEngineManager = searchEngineManager;
		this._indexer = indexer;
		this._oldContentsId = oldContentsId;
	}

	@Override
	public void run() {
		LastReloadInfo reloadInfo = new LastReloadInfo();
		try {
			if (null != this._oldContentsId) {
				for (int i = 0; i < this._oldContentsId.size(); i++) {
					String id = this._oldContentsId.get(i);
					this._indexer.delete(id);
				}
			}
			this.loadNewIndex();
			reloadInfo.setResult(LastReloadInfo.ID_SUCCESS_RESULT);
		} catch (Throwable t) {
			reloadInfo.setResult(LastReloadInfo.ID_FAILURE_RESULT);
			_logger.error("error in run", t);
		} finally {
			reloadInfo.setDate(new Date());
			this._searchEngineManager.notifyEndingIndexLoading(reloadInfo);
			this._searchEngineManager.sellOfQueueEvents();
		}
	}

	private void loadNewIndex() throws Throwable {
		try {
			List<String> contentsId = this._contentManager.searchId(null);
			for (int i=0; i<contentsId.size(); i++) {
				String id = contentsId.get(i);
				this.reloadContentIndex(id);
			}
			_logger.debug("Indicizzazione effettuata");
		} catch (Throwable t) {
			_logger.error("error in reloadIndex", t);
			throw t;
		}
	}

	private void reloadContentIndex(String id) {
		try {
			Content content = this._contentManager.loadContent(id, true);
			if (content != null) {
				this._indexer.addContent(content);
				_logger.debug("Indexed content {}", content.getId());
			}
		} catch (Throwable t) {
			_logger.error("Error reloading index: content id {}", id, t);
		}
	}

	private CmsSearchEngineManager _searchEngineManager;
	private IContentManager _contentManager;
	private Indexer _indexer;
	private List<String> _oldContentsId;

}