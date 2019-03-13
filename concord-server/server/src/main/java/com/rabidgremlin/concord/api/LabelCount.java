package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LabelCount
{

  private String label;

  private int count;

}
