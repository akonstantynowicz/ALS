
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu;

import java.io.IOException;
import pl.ug.edu.als.ALS;

/**
 * <h1>ALS</h1>
 * This program is a propotype recommender system which uses Alternating Least Square method
 * @author Anna Konstantynowicz
 * @author Marcin Szczepaniak
 * @author Jakub Ściślewski
 * @version 1.0
 */
public class Main {

  /**
   * This is the main method which creates als object and starts runAlsAlgorithm method
   * @param args unused
   */
  public static void main(String[] args) {
    ALS als = new ALS(3, 0.1);
    try {
      als.runAlsAlgorithm();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
