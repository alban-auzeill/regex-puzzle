package com.auzeill;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Constraints {

  public final Direction direction;
  public final Pattern[] patterns;
  public final Cell[] cells;

  public Constraints(Direction direction, String[] patterns, Cell[] cells) {
    this.direction = direction;
    this.patterns = Stream.of(patterns)
      .map(Pattern::compile)
      .toArray(Pattern[]::new);
    this.cells = cells;
  }

}
