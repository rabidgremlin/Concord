package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO add validation annotations

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewSessionRequest
{
  private String userId;

  @ToString.Exclude
  private String password;

}
