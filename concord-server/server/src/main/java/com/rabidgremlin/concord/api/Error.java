package com.rabidgremlin.concord.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public final class Error
{

  private ErrorError error = null;

  public Error error(ErrorError error)
  {
    this.error = error;
    return this;
  }

  @ApiModelProperty()
  @JsonProperty("error")
  public ErrorError getError()
  {
    return error;
  }

  public void setError(ErrorError error)
  {
    this.error = error;
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
    Error error = (Error) o;
    return Objects.equals(this.error, error.error);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(error);
  }

}
