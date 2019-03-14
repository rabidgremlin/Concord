package com.rabidgremlin.concord.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Phrase
{

  @ToString.Exclude
  private String phraseId;

  private String text;

  private Boolean completed;

  private String label;

  public static String computePhraseId(String phrase)
  {
    return DigestUtils.md5Hex(phrase);
  }

  public Phrase(String text)
  {
    this.text = text;
    this.phraseId = computePhraseId(text);
  }

}
