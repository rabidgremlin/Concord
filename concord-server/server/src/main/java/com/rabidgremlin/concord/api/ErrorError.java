package com.rabidgremlin.concord.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents an error returned by the api.
 */
public final class ErrorError
{

  private Integer code = null;

  private String message = null;

  public ErrorError code(Integer code)
  {
    this.code = code;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("code")
  public Integer getCode()
  {
    return code;
  }

  public void setCode(Integer code)
  {
    this.code = code;
  }

  public ErrorError message(String message)
  {
    this.message = message;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("message")
  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    ErrorError errorError = (ErrorError) o;
    return Objects.equals(code, errorError.code) && Objects.equals(message, errorError.message);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(code, message);
  }

}
