package com.rabidgremlin.concord;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ConcordServerConfiguration
    extends Configuration
{  
  @NotEmpty
  private String jwtTokenSecret;
   
  @JsonProperty("jwtTokenSecret")
  public void setJwtTokenSecret(String secret)
  {
    jwtTokenSecret = secret;
  }
  
  public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
    return jwtTokenSecret.getBytes("UTF-8");
}
}
