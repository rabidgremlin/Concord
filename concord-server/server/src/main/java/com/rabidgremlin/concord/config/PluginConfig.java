package com.rabidgremlin.concord.config;

import java.util.HashMap;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PluginConfig
{
  @NotEmpty
  private String className;

  private HashMap<String, Object> configProperties = new HashMap<String, Object>();

  public String getClassName()
  {
    return className;
  }

  @JsonProperty("class")
  public void setClassName(String className)
  {
    this.className = className;
  }

  public HashMap<String, Object> getConfigProperties()
  {
    return configProperties;
  }

  @JsonProperty("config")
  public void setConfigProperties(HashMap<String, Object> configProperties)
  {
    this.configProperties = configProperties;
  }

}
