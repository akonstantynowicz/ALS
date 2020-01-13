package pl.ug.edu.als;

import java.io.IOException;
import java.util.List;
import pl.ug.edu.data.Review;

public class Test {

  private Test() {
    throw new IllegalStateException("Utility Class");
  }

  public static void testForDifferentDAndLambda(List<Review> reviewList, int minD, int maxD,
      double minLambda,
      double maxLambda, int n) throws IOException {
    ALS als = new ALS(reviewList, n);
    als.prepareAlgorithm();
    for (int i = minD; i <= maxD; i++) {
      for (double j = minLambda; j <= maxLambda; j = j + 0.1) {
        als.setD(i);
        als.setLambda(j);
        als.runAlgorithm();
      }
    }
  }

}
