package com.auzeill;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellTest {

  @Test
  void test_alternatives() {
    Cell cell = new Cell(Pos.of(Direction.A_B, 6, 10));
    assertThat(cell.text()).isEqualTo("26");
    "ABCDEFGHIJKLMNOPQ".chars().forEach(c -> cell.removeAlternative((char) c, null));
    assertThat(cell.text()).isEqualTo(" 9");
  }

}
