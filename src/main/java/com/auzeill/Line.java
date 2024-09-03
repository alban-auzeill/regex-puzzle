package com.auzeill;

public record Line(Direction direction, int index) {
  @Override
  public String toString() {
    return "direction " + direction.name() + ", index " + index;
  }
}
