
/*******************************************************************************
 *  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
 *  All rights reserved
 ******************************************************************************/

package pl.ug.edu;

import java.io.IOException;
import java.util.List;
import pl.ug.edu.als.ALS;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;

public class Main {

  public static void main(String[] args) {
    try {
      runAlsAlgorithm();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void runAlsAlgorithm() throws IOException {
    Parser parser = new Parser();
    List<Review> list = parser.readData("sample.txt");
    System.out.println(DataUtil.getHighestProductId(list));
    ALS als = new ALS(3, 0.1, DataUtil.getHighestProductId(list) + 1);
    for (Review r : list) {
      System.out.println(r);
      als.addReview(r);
    }
    als.generateRandomPMatrix();
    als.generateRandomUMatrix();
    als.alg();
  }
}
