package com.auzeill;

import java.util.ArrayList;
import java.util.List;

public final class Cell {
  private final static List<Character> ALL_ALTERNATIVES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
    .mapToObj(c -> (char) c)
    .toList();

  private final int line;
  private final int col;
  private final List<Character> alternatives;

  public Cell(int line, int col) {
    this.line = line;
    this.col = col;
    this.alternatives = new ArrayList<>(ALL_ALTERNATIVES);
  }

  public String text() {
    return String.format("(%02d,%02d)", line, col);
  }

  public String alternativesText() {
    return String.format("%03d", alternatives.size());
  }

}
