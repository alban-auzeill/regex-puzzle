package com.auzeill;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HexaBoardTest {

  @Test
  void test_word_length() {
    var board = new HexaBoard(7);
    assertThat(board.cells(Direction.A_B, 1).length).isEqualTo(7);
    assertThat(board.cells(Direction.B_C, 2).length).isEqualTo(8);
    assertThat(board.cells(Direction.C_A, 6).length).isEqualTo(12);
    assertThat(board.cells(Direction.A_B, 7).length).isEqualTo(13);
    assertThat(board.cells(Direction.B_C, 6).length).isEqualTo(12);
    assertThat(board.cells(Direction.C_A, 12).length).isEqualTo(8);
    assertThat(board.cells(Direction.A_B, 13).length).isEqualTo(7);
  }

  @Test
  void test_alternatives() {
    var board = new HexaBoard(7);
    assertThat(board.text()).hasToString("""
            26262626262626
           2626262626262626
          262626262626262626
         26262626262626262626
        2626262626262626262626
       262626262626262626262626
      26262626262626262626262626
       262626262626262626262626
        2626262626262626262626
         26262626262626262626
          262626262626262626
           2626262626262626
            26262626262626
      """);
    "ABDEGHJKMNPQSTVWYZ".chars()
      .forEach(c -> board.cell(Direction.A_B, 4, 3).removeAlternative((char) c, null));
    assertThat(board.text()).hasToString("""
            26262626262626
           2626262626262626
          262626262626262626
         2626 826262626262626
        2626262626262626262626
       262626262626262626262626
      26262626262626262626262626
       262626262626262626262626
        2626262626262626262626
         26262626262626262626
          262626262626262626
           2626262626262626
            26262626262626
      """);
    "BCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
      .forEach(c -> board.cell(Direction.B_C, 4, 3).removeAlternative((char) c, null));
    assertThat(board.text()).hasToString("""
            26262626262626
           2626262626262626
          262626262626262626
         2626 826262626262626
        2626262626262626262626
       262626262626262626262626
      26262626262626262626262626
       262626262626262626262626
        2626262626262626262626
         26262626262626262626
          262626 A2626262626
           2626262626262626
            26262626262626
      """);
    "ACDEFGHIJKLMNOPQRSTUVWXYZ".chars()
      .forEach(c -> board.cell(Direction.C_A, 4, 3).removeAlternative((char) c, null));
    assertThat(board.text()).hasToString("""
            26262626262626
           2626262626262626
          262626262626262626
         2626 826262626262626
        2626262626262626262626
       262626262626262626 B2626
      26262626262626262626262626
       262626262626262626262626
        2626262626262626262626
         26262626262626262626
          262626 A2626262626
           2626262626262626
            26262626262626
      """);
  }

}
