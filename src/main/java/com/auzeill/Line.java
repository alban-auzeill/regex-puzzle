package com.auzeill;

public record Line(Direction direction, int index) implements Comparable<Line> {

  @Override
  public String toString() {
    return "direction " + direction.name() + ", index " + index;
  }

  @Override
  public int compareTo(Line o) {
    int directionCompare = direction.compareTo(o.direction);
    if (directionCompare != 0) {
      return directionCompare;
    }
    return Integer.compare(index, o.index);
  }

}
