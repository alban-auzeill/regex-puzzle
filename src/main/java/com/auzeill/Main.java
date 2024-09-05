package com.auzeill;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.TreeMap;

public class Main {

  public static void main(String[] args) {

    // from https://gist.github.com/LeventErkok/4942496
    var board = new HexaBoard(7)
      .withConstraints(Direction.A_B,
        ".*H.*H.*", "(DI|NS|TH|OM)*", "F.*[AO].*[AO].*", "(O|RHH|MM)*", ".*", "C*MC(CCC|MM)*", "[^C]*[^R]*III.*",
        "(...?)\\1*", "([^X]|XCC)*", "(RR|HHH)*.?", "N.*X.X.X.*E", "R*D*M*", ".(C|HH)*"
      )
      .withConstraints(Direction.B_C,
        ".*G.*V.*H.*", "[CR]*", ".*XEXM*", ".*DD.*CCM.*", ".*XHCR.*X.*", ".*(.)(.)(.)(.)\\4\\3\\2\\1.*", ".*(IN|SE|HI)",
        "[^C]*MMM[^C]*", ".*(.)C\\1X\\1.*", "[CEIMU]*OH[AEMOR]*", "(RX|[^R])*", "[^M]*M[^M]*", "(S|MM|HHH)*"
      )
      .withConstraints(Direction.C_A,
        ".*SE.*UE.*", ".*LR.*RL.*", ".*OXR.*", "([^EMC]|EM)*", "(HHX|[^HX])*", ".*PRR.*DDC.*", ".*",
        "[AM]*CM(RC)*R?", "([^MC]|MM|CC)*", "(E|CR|MN)*", "P+(..)\\1.*", "[CHMNOR]*I[CHMNOR]*", "(ND|ET|IN)[^X]*"
      );

    long start = System.currentTimeMillis();

    System.out.println(board.text());

    var reducerByLine = new TreeMap<Line, LineSolver>();
    for (Line line : board.allLines()) {
      reducerByLine.put(line, new LineSolver(line, board.constraint(line), board.cells(line)));
    }

    var reducers = new LinkedHashSet<>(board.allLines().stream().map(reducerByLine::get).toList());
    while (!reducers.isEmpty()) {
      var nextReduceToEvaluate = reducers.stream()
        .min(Comparator.comparingLong(LineSolver::nextReduceComplexity))
        .orElseThrow();
      if (!nextReduceToEvaluate.isSolved() && nextReduceToEvaluate.reduce()) {
        System.out.println(board.text());
      }
      if (nextReduceToEvaluate.isSolved()) {
        reducers.remove(nextReduceToEvaluate);
      }
    }

    long end = System.currentTimeMillis();
    System.out.println("Time: %,d ms".formatted(end - start));
  }

}
