package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.binout.jaxrs.csv.CsvSchema;

@AllArgsConstructor
@NoArgsConstructor
@Data
@CsvSchema(separator = ',', columns = { "label", "shortDescription", "longDescription" })
public class Label
{
  private String label;

  private String shortDescription;

  private String longDescription;

}
