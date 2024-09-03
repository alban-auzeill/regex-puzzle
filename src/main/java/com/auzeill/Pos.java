package com.auzeill;

public record Pos(Line line, int col) {
  public Pos(Direction direction, int line, int col) {
    this(new Line(direction, line), col);
  }
}
