package com.auzeill;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlternativesStatisticsTest {

  @Test
  void test_accept_and_reject() {
    var statistics = new AlternativesStatistics(3);
    statistics.accept("axc");
    statistics.accept("axf");
    statistics.reject("axe");
    statistics.reject("bxc");
    statistics.reject("bxf");
    statistics.reject("bxe");
    assertThatThrownBy(() -> statistics.accept("invalid"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("alternative.length(): 7 != length: 3");
    assertEquals(List.of(Set.of('b'), Set.of(), Set.of('e')), statistics.computeRejectedCharacters());
  }

}
