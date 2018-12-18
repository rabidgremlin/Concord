package com.rabidgremlin.concord.api;

// TODO add validation annotations
public class NewSessionRequest
{
  private String userId;

  private String password;

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  @Override
  public String toString()
  {
    return "NewSessionRequest [userId=" + userId + ", password=*************]";
  }

}
