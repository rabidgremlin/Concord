package com.rabidgremlin.concord.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.binout.jaxrs.csv.CsvSchema;

@NoArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "text", "possibleLabel" })
public class UnlabelledPhrase
{
  private String text;

  private String possibleLabel;

}
