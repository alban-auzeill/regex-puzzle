package com.auzeill;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HexaBoard {
  public final int sideSize;
  public final int lineCount;
  public final Cell[][][] cells;
  public final Pattern[][] constraints;

  public HexaBoard(int sideSize) {
    this.sideSize = sideSize;
    lineCount = (2 * sideSize) - 1;
    cells = new Cell[Direction.values().length][][];
    Cell[][] abCells = new Cell[lineCount][];
    Cell[][] bcCells = new Cell[lineCount][];
    Cell[][] caCells = new Cell[lineCount][];
    cells[Direction.A_B.ordinal()] = abCells;
    cells[Direction.B_C.ordinal()] = bcCells;
    cells[Direction.C_A.ordinal()] = caCells;
    for (int line = 1; line <= lineCount; line++) {
      abCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        abCells[line - 1][col - 1] = new Cell(Pos.of(Direction.A_B, line, col));
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      bcCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        int abLine = (lineCount - col + 1) - (line <= sideSize ? 0 : (line - sideSize));
        int abCol = line - (abLine >= sideSize ? 0 : (sideSize - abLine));
        Cell cell = abCells[abLine - 1][abCol - 1];
        bcCells[line - 1][col - 1] = cell;
        cell.positions.add(Pos.of(Direction.B_C, line, col));
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      caCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        int abLine = col + (line < sideSize ? (sideSize - line) : 0);
        int abCol = (lineCount - line + 1) - (abLine >= sideSize ? abLine - sideSize : 0);
        Cell cell = abCells[abLine - 1][abCol - 1];
        caCells[line - 1][col - 1] = cell;
        cell.positions.add(Pos.of(Direction.C_A, line, col));
      }
    }
    constraints = new Pattern[Direction.values().length][];
  }

  public HexaBoard withConstraints(Direction direction, String... patterns) {
    if (patterns.length != lineCount) {
      throw new IllegalArgumentException("Patterns must have the same length as the board");
    }
    constraints[direction.ordinal()] = Stream.of(patterns)
      .map(Pattern::compile)
      .toArray(Pattern[]::new);
    return this;
  }

  private int wordLength(int line) {
    return line <= sideSize ? (sideSize - 1) + line : (lineCount + sideSize) - line;
  }

  public Cell cell(Direction direction, int line, int col) {
    return cells(direction, line)[col - 1];
  }

  public Cell[] cells(Line line) {
    return cells(line.direction(), line.index());
  }

  public Cell[] cells(Direction direction, int line) {
    return cells[direction.ordinal()][line - 1];
  }

  public CharSequence text() {
    var out = new StringBuilder();
    for (int line = 1; line <= lineCount; line++) {
      Cell[] cells = cells(Direction.A_B, line);
      out.append(" ".repeat(lineCount - cells.length));
      for (Cell cell : cells) {
        out.append(cell.text());
      }
      out.append('\n');
    }
    return out;
  }

  public Pattern constraint(Line line) {
    Pattern[] patterns = constraints[line.direction().ordinal()];
    if (patterns == null) {
      throw new IllegalStateException("No constraints for " + line);
    }
    return patterns[line.index() - 1];
  }


  public List<Line> allLines() {
    var lines = new ArrayList<Line>();
    for (Direction direction : Direction.values()) {
      for (int line = 1; line <= lineCount; line++) {
        lines.add(new Line(direction, line));
      }
    }
    return lines;
  }

}
