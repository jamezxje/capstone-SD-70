package org.fpoly.capstone.util;

public class StringUtil {
  public static String toSlug(String input) {
    String cleanString = input.replaceAll("[^a-zA-Z0-9 ]", "");
    cleanString = cleanString.trim();
    return cleanString.toLowerCase().replaceAll("\\s", "-");
  }
}
