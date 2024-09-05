package com.auzeill;

public record Pos(Line line, int col) {
  public static Pos of(Direction direction, int line, int col) {
    return of(new Line(direction, line), col);
  }

  public static Pos of(Line line, int col) {
    return new Pos(line, col);
  }
}
