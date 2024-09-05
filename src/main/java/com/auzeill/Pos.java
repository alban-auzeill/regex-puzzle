package com.auzeill;

public record Pos(Line line, int col) {
  public static Pos of(Direction direction, int line, int col) {
    Line line1 = new Line(direction, line);
    return new Pos(line1, col);
  }
}
