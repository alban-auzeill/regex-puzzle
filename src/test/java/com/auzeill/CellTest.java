package com.auzeill;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellTest {

  @Test
  void test_as_text() {
    Cell cell = new Cell(Pos.of(Direction.A_B, 6, 10));
    assertThat(cell.text()).isEqualTo("(06,10)");
    cell = new Cell(Pos.of(Direction.A_B, 11, 3));
    assertThat(cell.text()).isEqualTo("(11,03)");
  }

  @Test
  void test_alternatives() {
    Cell cell = new Cell(Pos.of(Direction.A_B, 6, 10));
    assertThat(cell.alternativesText()).isEqualTo("026");
  }

}
