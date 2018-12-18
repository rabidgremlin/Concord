package com.rabidgremlin.concord.plugin.credentialvalidators;

import com.rabidgremlin.concord.plugin.CredentialsValidator;

import java.util.Map;

public class ConfigCredentialsValidator
    extends CredentialsValidator
{

  public ConfigCredentialsValidator(Map<String, Object> configProperties)
  {
    super(configProperties);
  }

  @Override
  public boolean validateCredentials(String userId, String password)
  {
    return configProperties.containsKey(userId.toLowerCase()) && configProperties.get(userId.toLowerCase()).equals(password);
  }

}
