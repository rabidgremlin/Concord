package com.rabidgremlin.concord.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the credentials validator for a plugin.
 * Responsible for validating credentials to the concord app.
 *
 * Example: {@link com.rabidgremlin.concord.plugin.credentialvalidators.ConfigCredentialsValidator}
 */
public abstract class CredentialsValidator 
{
   protected Map<String,Object> configProperties;
   
   public CredentialsValidator()
   {
	   this.configProperties = new HashMap<>();
   }
	
   public CredentialsValidator(Map<String,Object> configProperties)
   {
	 this.configProperties = configProperties;
   }
	
  public abstract boolean validateCredentials(String userId, String password);
}
