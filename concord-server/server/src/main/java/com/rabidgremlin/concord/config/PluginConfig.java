package com.rabidgremlin.concord.config;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PluginConfig
{
  @NotEmpty
  private String className;

  private Map<String, Object> configProperties = new HashMap<>();

  public String getClassName()
  {
    return className;
  }

  @JsonProperty("class")
  public void setClassName(String className)
  {
    this.className = className;
  }

  public Map<String, Object> getConfigProperties()
  {
    return configProperties;
  }

  @JsonProperty("config")
  public void setConfigProperties(Map<String, Object> configProperties)
  {
    this.configProperties = configProperties;
  }

}
