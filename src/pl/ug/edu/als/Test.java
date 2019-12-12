
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.als;

import java.io.IOException;

public final class Test {

  private Test() {
    throw new IllegalStateException("Utility Class");
  }

  public static void testTimeForDifferentD(int minD, int maxD, double lambda) throws IOException {
    ALS als;
    for (int i = minD; i <= maxD; i++) {
      als = new ALS(i, lambda);
      als.runAlsAlgorithm();
    }
  }
}

