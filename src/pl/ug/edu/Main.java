package pl.ug.edu;

import java.io.IOException;
import java.util.List;
import pl.ug.edu.als.ALS;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;

public class Main {

  public static void main(String[] args) {
//        Gauss gauss = new Gauss();
//        Macierz m = new Macierz(5);
//        m.drukuj();
//        System.out.println(gauss.PG(m.macierz, m.wektor));
//        m.drukuj();
    ALS als = new ALS(3, 0.1);
    Parser parser = new Parser();
    try {
      List<Review> list = parser.readData("sample.txt");
      for (Review r : list) {
        System.out.println(r);
        als.addReview(r);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    als.generatePMatrix();
    als.generateUMatrix();
  }
}
