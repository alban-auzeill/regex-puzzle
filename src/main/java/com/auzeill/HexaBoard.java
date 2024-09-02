package com.auzeill;

public class HexaBoard {
  public final int sideSize;
  public final int lineCount;
  public final Cell[][][] cells;

  public HexaBoard(int sideSize) {
    this.sideSize = sideSize;
    this.lineCount = (2 * sideSize) - 1;
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
        abCells[line - 1][col - 1] = new Cell(line, col);
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      bcCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        int abLine = (lineCount - col + 1) - (line <= sideSize ? 0 : (line - sideSize));
        int abCol = line - (abLine >= sideSize ? 0 : (sideSize - abLine));
        bcCells[line - 1][col - 1] = abCells[abLine - 1][abCol - 1];
      }
    }
    for (int line = 1; line <= lineCount; line++) {
      caCells[line - 1] = new Cell[wordLength(line)];
      for (int col = 1; col <= abCells[line - 1].length; col++) {
        caCells[line - 1][col - 1] = abCells[line - 1][col - 1];
      }
    }
  }

  private int wordLength(int line) {
    return line <= sideSize ? (sideSize - 1) + line : (lineCount + sideSize) - line;
  }

  public Cell cell(Direction direction, int line, int col) {
    return cells(direction, line)[col - 1];
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

}
