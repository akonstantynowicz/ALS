
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.als;

import java.io.IOException;

public final class Test {

  private Test() {
    throw new IllegalStateException("Utility Class");
  }

  public static void testForDifferentD(int minD, int maxD, double lambda) throws IOException {
    ALS als = new ALS(lambda);
    als.prepareInitialData();
    for (int i = minD; i <= maxD; i++) {
      als.setD(i);
      als.algorithm();
    }
  }

  public static void testForDifferentLambda(int d, double minLambda, double maxLambda)
      throws IOException {
    ALS als = new ALS(d);
    als.prepareInitialData();
    for (double i = minLambda; i <= maxLambda; i += 0.05) {
      als.setLambda(i);
      als.algorithm();
    }
  }
}

