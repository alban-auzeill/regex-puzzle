package com.auzeill;

import java.util.ArrayList;
import java.util.List;

public final class Cell {
  private final static List<Character> ALL_ALTERNATIVES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
    .mapToObj(c -> (char) c)
    .toList();

  public final List<Pos> positions = new ArrayList<>(Direction.values().length);
  public final List<Character> alternatives;

  public Cell(Pos pos) {
    positions.add(pos);
    this.alternatives = new ArrayList<>(ALL_ALTERNATIVES);
  }

  public String text() {
    return String.format("(%02d,%02d)", positions.getFirst().line(), positions.getFirst().col());
  }

  public String alternativesText() {
    if (alternatives.size() == 1) {
      return " " + alternatives.get(0) + " ";
    }
    return String.format("%03d", alternatives.size());
  }

}
