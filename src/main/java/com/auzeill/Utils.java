package com.auzeill;

public record Utils() {
  public static void precondition(boolean condition) {
    if (!condition) {
      throw new IllegalStateException("Precondition failed");
    }
  }
}
