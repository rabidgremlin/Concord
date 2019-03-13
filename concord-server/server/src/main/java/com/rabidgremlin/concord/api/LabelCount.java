package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabelCount
{

  private String label;

  private int count;

}
