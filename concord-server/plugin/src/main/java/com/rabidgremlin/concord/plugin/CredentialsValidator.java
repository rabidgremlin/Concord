package com.rabidgremlin.concord.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the credentials validator for a plugin.
 * Responsible for validating credentials to the concord application.
 *
 * Example: {@link com.rabidgremlin.concord.plugin.credentialvalidators.ConfigCredentialsValidator}
 */
public abstract class CredentialsValidator 
{
   protected final Map<String,Object> configProperties;

  /**
   * Initialises credential validator without config properties.
   */
  public CredentialsValidator()
   {
	   this.configProperties = new HashMap<>();
   }

  /**
   * Initialises credential validator with config properties (optional); e.g. a map of (userId -> password) pairs.
   *
   * @param configProperties - map of config properties; defined in server.yml
   */
   public CredentialsValidator(Map<String,Object> configProperties)
   {
	 this.configProperties = configProperties;
   }

  /**
   * Validates the given credentials based on the config properties.
   */
  public abstract boolean validateCredentials(String userId, String password);
}
