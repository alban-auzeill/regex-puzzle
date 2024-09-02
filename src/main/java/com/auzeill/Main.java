package com.auzeill;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  // from https://gist.github.com/LeventErkok/4942496

  public static final String[] A_B = {
    ".*H.*H.*",
    "(DI|NS|TH|OM)*",
    "F.*[AO].*[AO].*",
    "(O|RHH|MM)*",
    ".*",
    "C*MC(CCC|MM)*",
    "[^C]*[^R]*III.*",
    "(...?)\\1*",
    "([^X]|XCC)*",
    "(RR|HHH)*.?",
    "N.*X.X.X.*E",
    "R*D*M*",
    ".(C|HH)*"
  };

  public static final String[] B_C = {
    ".*G.*V.*H.*",
    "[CR]*",
    ".*XEXM*",
    ".*DD.*CCM.*",
    ".*XHCR.*X.*",
    ".*(.)(.)(.)(.)\\4\\3\\2\\1.*",
    ".*(IN|SE|HI)",
    "[^C]*MMM[^C]*",
    ".*(.)C\\1X\\1.*",
    "[CEIMU]*OH[AEMOR]*",
    "(RX|[^R])*",
    "[^M]*M[^M]*",
    "(S|MM|HHH)*"
  };

  public static final String[] C_A = {
    ".*SE.*UE.*",
    ".*LR.*RL.*",
    ".*OXR.*",
    "([^EMC]|EM)*",
    "(HHX|[^HX])*",
    ".*PRR.*DDC.*",
    ".*",
    "[AM]*CM(RC)*R?",
    "([^MC]|MM|CC)*",
    "(E|CR|MN)*",
    "P+(..)\\1.*",
    "[CHMNOR]*I[CHMNOR]*",
    "(ND|ET|IN)[^X]*"
  };

  public static int wordLength(int line) {
    if (line < 1 || line > 13) {
      throw new IllegalArgumentException("Invalid value '" + line + "', must be between 1 and 13");
    }
    return line <= 7 ? 6 + line : 20 - line;
  }

  public static void main(String[] args) {
    String text = "ABB";
    Pattern p = Pattern.compile("^(A|B)+$");
    Matcher matcher = p.matcher(text);
    int maxMatchLength = 0;
    for (int partialLen = 1; partialLen <= text.length(); partialLen++) {
      matcher.reset();
      matcher.region(0, partialLen);
      if (matcher.matches() || matcher.hitEnd()) {
        maxMatchLength = partialLen;
      }
    }
    System.out.println("Max match length: " + maxMatchLength);
  }

  // 01..........A B C D E F G
  // 02.........B A A A A A A A
  // 03........C A A A A A A A A
  // 04.......D A A A A A A A A A
  // 05......E A A A A A A A A A A
  // 06.....F A A A A A A A A A A A
  // 07....G A A A A A A A A A A A A
  // 08.....A A A A A A A A A A A A
  // 09......A A A A A A A A A A A
  // 10.......A A A A A A A A A A
  // 11........A A A A A A A A A
  // 12.........A A A A A A A A
  // 13..........A A A A A A A

  static class Board {
    Cell[][] cells;

    public Board() {
      cells = new Cell[7][];
      for (int line = 1; line <= 13; line++) {
        cells[line] = new Cell[wordLength(line)];
        for (int col = 0; col < cells[line].length; col++) {
          cells[line][col] = new Cell(line, col);
        }
      }
    }
  }

  record Pos(int line, int col) {
    void rotateLeft() {
    }
  }

  static class Cell {
    private final static List<Character> DEFAULT_POSSIBLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
      .mapToObj(c -> (char) c)
      .toList();

    private final int line;
    private final int col;
    private final List<Character> possibleCharacters;

    public Cell(int line, int col) {
      this.line = line;
      this.col = col;
      this.possibleCharacters = new ArrayList<>(DEFAULT_POSSIBLE_CHARACTERS);
    }

  }

  static class Word {
    private final Direction direction;
    private final int line;
    private final List<Cell> cells;

    public Word(Direction direction, int line) {
      this.direction = direction;
      this.line = line;
      cells = new ArrayList<>();
    }
  }

}
