package com.rabidgremlin.concord.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class PossibleLabel
    extends Label
{

  private Double score;

}
