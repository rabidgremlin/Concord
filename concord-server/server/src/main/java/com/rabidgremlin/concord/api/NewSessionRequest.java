package com.rabidgremlin.concord.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewSessionRequest
{
  private String userId;

  private String password;

  @Override
  public String toString()
  {
    return "NewSessionRequest [userId=" + userId + ", password=*************]";
  }

}
