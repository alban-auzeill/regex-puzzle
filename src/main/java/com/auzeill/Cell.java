package com.auzeill;

import java.util.ArrayList;
import java.util.List;

import static com.auzeill.Utils.precondition;

public final class Cell {
  private final static List<Character> ALL_ALTERNATIVES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
    .mapToObj(c -> (char) c)
    .toList();

  public final List<Pos> positions = new ArrayList<>(Direction.values().length);
  public final List<Character> alternatives;
  public final List<ChangeListener> changeListeners = new ArrayList<>();

  public interface ChangeListener {
    void onRemove(Cell cell, Character removedAlternative);
  }

  public Cell(Pos pos) {
    positions.add(pos);
    this.alternatives = new ArrayList<>(ALL_ALTERNATIVES);
  }

  public void register(ChangeListener listener) {
    changeListeners.add(listener);
  }

  public void removeAlternative(Character alternative, ChangeListener emitter) {
    if (alternatives.remove(alternative)) {
      precondition(!alternatives.isEmpty());
      changeListeners
        .stream().filter(listener -> listener != emitter)
        .forEach(listener -> listener.onRemove(this, alternative));
    }
  }

  public String text() {
    return alternatives.size() == 1 ? (" " + alternatives.get(0)) : String.format("%2d", alternatives.size());
  }

}
