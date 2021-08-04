package com.dns.polinsight.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashUtil {

  public String makeHash(List<String> list, String salt) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    digest.reset();
    digest.update(salt.getBytes(StandardCharsets.UTF_8));
    for (String str : list)
      digest.update(str.getBytes(StandardCharsets.UTF_8));

    StringBuilder sb = new StringBuilder();
    for (byte b : digest.digest())
      sb.append(String.format("%02x", b));
    return sb.toString();
  }

}
