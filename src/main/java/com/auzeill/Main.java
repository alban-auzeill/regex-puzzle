package com.auzeill;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

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


    System.out.println(board.alternativesText(Direction.A_B));

    Set<Line> changedLines = new LinkedHashSet<>(board.allLines());
    while (!changedLines.isEmpty()) {
      var linesToEvaluate = new ArrayList<>(changedLines);
      changedLines.clear();
      for (Line line : linesToEvaluate) {
        var report = new StringBuilder();
        board.applyConstraints(line, report, changedLines);
        System.out.print(report);
      }
    }

    System.out.println(board.alternativesText(Direction.A_B));
  }

}
