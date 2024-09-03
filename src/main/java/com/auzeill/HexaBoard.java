package com.auzeill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
        abCells[line - 1][col - 1] = new Cell(new Pos(Direction.A_B, line, col));
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      bcCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        int abLine = (lineCount - col + 1) - (line <= sideSize ? 0 : (line - sideSize));
        int abCol = line - (abLine >= sideSize ? 0 : (sideSize - abLine));
        Cell cell = abCells[abLine - 1][abCol - 1];
        bcCells[line - 1][col - 1] = cell;
        cell.positions.add(new Pos(Direction.B_C, line, col));
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      caCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        int abLine = col + (line < sideSize ? (sideSize - line) : 0);
        int abCol = (lineCount - line + 1) - (abLine >= sideSize ? abLine - sideSize : 0);
        Cell cell = abCells[abLine - 1][abCol - 1];
        caCells[line - 1][col - 1] = cell;
        cell.positions.add(new Pos(Direction.C_A, line, col));
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

  public CharSequence text(Direction direction) {
    var out = new StringBuilder();
    for (int line = 1; line <= lineCount; line++) {
      Cell[] cells = cells(direction, line);
      out.append("    ".repeat(lineCount - cells.length));
      for (int col = 1; col <= cells.length; col++) {
        if (col != 1) {
          out.append(" ");
        }
        out.append(cell(direction, line, col).text());
      }
      out.append('\n');
    }
    return out;
  }

  public CharSequence alternativesText(Direction direction) {
    var out = new StringBuilder();
    for (int line = 1; line <= lineCount; line++) {
      Cell[] cells = cells(direction, line);
      out.append("  ".repeat(lineCount - cells.length));
      for (int col = 1; col <= cells.length; col++) {
        if (col != 1) {
          out.append(" ");
        }
        out.append(cell(direction, line, col).alternativesText());
      }
      out.append('\n');
    }
    return out;
  }

  private static CharSequence resolvedPart(Cell[] lineCells) {
    var part = new StringBuilder(lineCells.length);
    for (Cell cell : lineCells) {
      if (cell.alternatives.size() != 1) {
        break;
      }
      part.append(cell.alternatives.getFirst().charValue());
    }
    return part;
  }

  public Pattern constraint(Line line) {
    Pattern[] patterns = constraints[line.direction().ordinal()];
    if (patterns == null) {
      throw new IllegalStateException("No constraints for " + line);
    }
    return patterns[line.index() - 1];
  }

  public int applyConstraints(Line line, StringBuilder report, Set<Line> changedLines) {
    int changeCount = 0;
    Pattern pattern = constraint(line);
    Cell[] lineCells = cells(line);
    CharSequence resolvedPart = resolvedPart(lineCells);
    if (resolvedPart.length() == lineCells.length) {
      report.append("Apply constraint ").append(line).append(", line already resolved to '").append(resolvedPart).append("'\n");
      return changeCount;
    }
    int colToResolve = resolvedPart.length() + 1;
    Cell cell = lineCells[colToResolve - 1];
    List<Character> alternatives = cell.alternatives;
    report.append("Apply constraint ").append(line).append(", resolved part '").append(resolvedPart)
      .append(" alternatives [").append(alternatives.stream().map(Object::toString).collect(Collectors.joining())).append("]\n");
    var candidate = new StringBuilder(resolvedPart.length() + 1);
    candidate.append(resolvedPart);
    Iterator<Character> iterator = alternatives.iterator();
    while (iterator.hasNext()) {
      Character alternative = iterator.next();
      candidate.append(alternative.charValue());
      Matcher matcher = pattern.matcher(candidate);
      boolean matches = candidate.length() == lineCells.length ? matcher.matches() : matcher.matches() || matcher.hitEnd();
      if (!matches) {
        iterator.remove();
        changeCount++;
        cell.positions.stream().map(Pos::line).forEach(changedLines::add);
      }
      candidate.setLength(resolvedPart.length());
    }
    if (alternatives.isEmpty()) {
      throw new IllegalStateException("No alternatives left for " + line + ", resolvedPart '" + resolvedPart + "'");
    }
    if (changeCount > 0) {
      report.append("Remove ").append(changeCount).append(" alternatives\n");
    }
    return changeCount;
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
