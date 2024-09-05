package com.auzeill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.auzeill.Utils.precondition;

public class LineSolver implements Cell.ChangeListener {

  /**
   * Pattern to match the line, used to reduce the number of cell alternatives until we find one solution
   */
  public final Pattern pattern;

  /**
   * Position of the line on the HexaBoard
   */
  public final Line line;

  /**
   * Cells of the line, each cell contains a list of character alternatives
   */
  public final Cell[] cells;

  /**
   * List of possible alternatives for the line prefix having a length of alternativesLength.
   * If alternativesLength == length() and there is only one alternative, the word is solved.
   */
  private List<String> alternatives;

  /**
   * The length of the word prefix already resolved
   */
  private int alternativesLength;

  public LineSolver(Line line, Pattern pattern, Cell[] cells) {
    this.line = line;
    this.pattern = pattern;
    this.cells = cells;
    this.alternatives = List.of("");
    this.alternativesLength = 0;
    for (Cell cell : cells) {
      cell.register(this);
    }
  }

  public int length() {
    return cells.length;
  }

  @Override
  public void onRemove(Cell cell, Character removedAlternative) {
    Pos pos = cell.positions.stream()
      .filter(p -> p.line().equals(line))
      .findFirst()
      .orElseThrow();
    if (alternativesLength < pos.col()) {
      return;
    }
    int charIndex = pos.col() - 1;
    var statistics = new AlternativesStatistics(alternativesLength);
    Iterator<String> iterator = alternatives.iterator();
    while (iterator.hasNext()) {
      String alternative = iterator.next();
      if (alternative.charAt(charIndex) == removedAlternative) {
        statistics.reject(alternative);
        iterator.remove();
      }
    }
    if (statistics.isNotEmpty()) {
      alternatives.forEach(statistics::accept);
      removeAlternatives(statistics);
    }
  }

  public boolean isSolved() {
    return alternativesLength == length() && alternatives.size() == 1;
  }

  public long nextReduceComplexity() {
    if (alternativesLength == length()) {
      return Long.MAX_VALUE;
    }
    var nextAlternativesLength = alternativesLength + 1;
    return ((long) alternatives.size()) * cells[nextAlternativesLength - 1].alternatives.size();
  }

  public boolean reduce() {
    precondition(!isSolved());
    alternativesLength++;
    var cell = cells[alternativesLength - 1];
    var newAlternatives = new ArrayList<String>(this.alternatives.size() * cell.alternatives.size());
    var statistics = new AlternativesStatistics(alternativesLength);
    for (String alternative : this.alternatives) {
      for (Character newCharacter : cell.alternatives) {
        String newAlternative = alternative + newCharacter.charValue();
        if (matches(newAlternative)) {
          statistics.accept(newAlternative);
          newAlternatives.add(newAlternative);
        } else {
          statistics.reject(newAlternative);
        }
      }
    }
    this.alternatives = newAlternatives;
    return removeAlternatives(statistics);
  }

  private boolean removeAlternatives(AlternativesStatistics statistics) {
    boolean reduced = false;
    List<Set<Character>> rejectedCharacters = statistics.computeRejectedCharacters();
    for (int i = 0; i < alternativesLength; i++) {
      for (Character rejectedCharacter : rejectedCharacters.get(i)) {
        cells[i].removeAlternative(rejectedCharacter, this);
        reduced = true;
      }
    }
    return reduced;
  }

  private boolean matches(String candidate) {
    Matcher matcher = pattern.matcher(candidate);
    if (candidate.length() == length()) {
      return matcher.matches();
    } else {
      return matcher.matches() || matcher.hitEnd();
    }
  }

}
