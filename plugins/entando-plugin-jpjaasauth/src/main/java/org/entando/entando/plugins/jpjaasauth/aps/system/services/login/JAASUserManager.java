package org.entando.entando.plugins.jpjaasauth.aps.system.services.login;

import java.io.Reader;
import java.io.StringReader;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpjaasauth.aps.system.JAASAuthSystemConstants;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model.JAASServiceConfig;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model.JAASUser;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JAASUserManager extends UserManager implements IJAASUserManager {

	private static final Logger logger = LoggerFactory.getLogger(JAASUserManager.class);

	@Override
	public void init() throws Exception {
		super.init();
		try {			
			loadConfig();
			if (null != this.getConfig()) {
				logger.warn("JAAS authentication service status: {}, privileged: {}",
						this.getConfig().isEnabled(),
						this.getConfig().isPrivileged());
			}
		} catch (Throwable t) {
			logger.error("Error loading JAAS service configuration", t);
			logger.warn("JAAS authentication not available");
		}
	}

	protected void loadConfig() throws Throwable {
		ObjectMapper objectMapper = new ObjectMapper();
		ConfigInterface configManager = this.getConfigManager();
		String xml = configManager.getConfigItem(JAASAuthSystemConstants.JAAS_CONFIG_ITEM);

		if (xml == null) {
			throw new ApsSystemException("Configuration item not present: " + JAASAuthSystemConstants.JAAS_CONFIG_ITEM);
		}
		JSONObject jo = XML.toJSONObject(xml);
		String json = jo.get(CONFIG_ROOT).toString();		
		Reader reader = new StringReader(json);
		JAASServiceConfig cfg = objectMapper.readValue(reader, JAASServiceConfig.class);
		// TODO cache
		this.setConfig(cfg);
	}

	@Override
	public void updateConfig(JAASServiceConfig cfg) throws ApsSystemException {
		ConfigInterface configManager = this.getConfigManager();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String json = objectMapper.writeValueAsString(cfg);
			String xml = XML.toString(new JSONObject(json), CONFIG_ROOT);
			configManager.updateConfigItem(JAASAuthSystemConstants.JAAS_CONFIG_ITEM, xml);
			// TODO cache
			this.setConfig(cfg);
		} catch (Throwable t) {
			logger.error("error updating JAAS configuration", t);
		}
	}

	@Override
	public UserDetails getUser(String username, String password) throws ApsSystemException {
		UserDetails user = null;
		config = this.getConfig();

		try {
			if (null != this.getConfig()
					&& this.getConfig().isEnabled()) {
				// prefetch the standard user
				if (!config.isPrivileged()) {
					user = super.getUser(username, password);
				}

				// if the JAAS login is not privileged we return immediately the standard user
				if (null != user
						&& !config.isPrivileged()) {
					return user;
				}

				for (String realm: config.getRealms()) {
					logger.info("logging into realm '{}' with credential provided", realm);

					try {
						user = realmLogin(username, password, realm);
						if (null != user) {
							logger.info("user '{}' successfully logged in to realm '{}'",
									username, realm);
							return user;
						}
					} catch (FailedLoginException t) {
						logger.info("'{}' failed to login in to realm '{}'", 
								username, realm);
					}
				}
			}
		} catch (Throwable t) {
			logger.error("error during JAAS authentication", t);
		}
		return super.getUser(username, password);
	}


	protected UserDetails realmLogin(String username, String password, String realm) throws Throwable {
		JAASUser user = null;

		if (StringUtils.isNotBlank(username)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(realm)) {
			LoginContext lc = new LoginContext(realm,
					new JAASCallbackHandler(username, password));
			lc.login();
			if (null != lc
					&& null != lc.getSubject()) {
				user = new JAASUser();
				user.setUsername(username);
				user.setPassword(""); // no password!
				user.setSubject(lc.getSubject());
				user.setRealm(realm);
			} else {
				logger.warn("failed to login into realm {} OR invalid subject", realm);
			}
		} else {
			logger.warn("skipping realm {} username {} password provided: {} because of invalid credentials",
					realm, username, (null != password));
		}
		return user;
	}
	

	public ConfigInterface getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}

	@Override
	public JAASServiceConfig getConfig() {
		JAASServiceConfig cfg = new JAASServiceConfig();
		cfg.setEnabled(this.config.isEnabled());
		cfg.setPrivileged(this.config.isPrivileged());
		cfg.setRealms(this.config.getRealms());
		return cfg;
	}

	public void setConfig(JAASServiceConfig config) {
		this.config = config;
	}

	private ConfigInterface configManager;
	private JAASServiceConfig config;

	public final static String CONFIG_ROOT = "config";
}
