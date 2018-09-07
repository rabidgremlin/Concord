package com.rabidgremlin.concord.integration;

import java.util.HashMap;

public abstract class CredentialsValidator 
{
   protected HashMap<String,Object> configProperties;
   
   public CredentialsValidator()
   {
	   this.configProperties = new HashMap<String,Object>();
   }
	
   public CredentialsValidator(HashMap<String,Object> configProperties)
   {
	 this.configProperties = configProperties;
   }
	
  public abstract boolean validateCredentials(String userId,String password);
}
