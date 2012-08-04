package com.chdorner.roboreader.util;

import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
  private static final String TAG = "com.chdorner.roboreader.util.MD5";

  public static String hexdigest(String str) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(str.getBytes());
      byte messageDigest[] = digest.digest();

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }

      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      Log.e(TAG, e.getMessage());
    }

    return "";
  }
}
