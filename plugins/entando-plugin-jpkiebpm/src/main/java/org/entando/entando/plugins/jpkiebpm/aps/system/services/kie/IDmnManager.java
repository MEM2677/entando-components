package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.json.JSONObject;

public interface IDmnManager extends IKieFormManager {

    public static final String BEAN_NAME = "jpkiebpmsDmnManager";

    public JSONObject getDmnModel(String containerId) throws ApsSystemException;
}
