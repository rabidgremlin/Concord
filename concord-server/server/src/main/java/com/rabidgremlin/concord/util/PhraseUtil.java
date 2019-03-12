package com.rabidgremlin.concord.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class PhraseUtil
{

  public static String computePhraseId(String phrase)
  {
    return DigestUtils.md5Hex(phrase);
  }

}
