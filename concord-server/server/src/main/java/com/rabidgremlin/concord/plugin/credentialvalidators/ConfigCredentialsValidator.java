package com.rabidgremlin.concord.plugin.credentialvalidators;

import java.util.HashMap;

import com.rabidgremlin.concord.plugin.CredentialsValidator;

public class ConfigCredentialsValidator
    extends CredentialsValidator
{

  public ConfigCredentialsValidator(HashMap<String, Object> configProperties)
  {
    super(configProperties);
  }

  @Override
  public boolean validateCredentials(String userId, String password)
  {

    return configProperties.containsKey(userId.toLowerCase()) && configProperties.get(userId.toLowerCase()).equals(password);
  }

}
