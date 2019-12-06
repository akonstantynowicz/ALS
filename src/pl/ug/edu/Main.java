package pl.ug.edu;

import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Macierz;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args){
       /* Gauss gauss = new Gauss();
        Macierz m = new Macierz(5,5);
        m.drukuj();
        m.transponujMacierz();
        m.obliczWektor();
        System.out.println();
        m.drukuj();
        System.out.println(gauss.PG(m.macierz, m.wektor));
        for (int i=0;i<5;i++){
            System.out.print(m.wektorX[i] + " ");
        }
        System.out.println("\n");
        m.drukuj();*/

        Parser p = new Parser();

        try {
            List<Review> list = p.readData("sample.txt");

            for (Review r : list) {
                System.out.println(r);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
