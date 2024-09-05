package com.auzeill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.auzeill.Utils.precondition;

public class AlternativesStatistics {

  public final int length;
  public int count;
  public final List<Set<Character>> acceptedCharacters;
  public final List<Set<Character>> rejectedCharacters;

  public AlternativesStatistics(int length) {
    this.length = length;
    count = 0;
    acceptedCharacters = new ArrayList<>(length);
    rejectedCharacters = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      acceptedCharacters.add(new HashSet<>());
      rejectedCharacters.add(new HashSet<>());
    }
  }

  public boolean isNotEmpty() {
    return count > 0;
  }

  public void accept(String alternative) {
    add(acceptedCharacters, alternative);
  }

  public void reject(String alternative) {
    add(rejectedCharacters, alternative);
  }

  private void add(List<Set<Character>> list, String alternative) {
    precondition(alternative.length() == length);
    count++;
    for (int i = 0; i < alternative.length(); i++) {
      list.get(i).add(alternative.charAt(i));
    }
  }

  public List<Set<Character>> computeRejectedCharacters() {
    for (int i = 0; i < length; i++) {
      rejectedCharacters.get(i).removeAll(acceptedCharacters.get(i));
    }
    return rejectedCharacters;
  }

}
