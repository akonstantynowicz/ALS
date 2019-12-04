package pl.ug.edu;

import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Macierz;
import pl.ug.edu.generic.Double;

public class Main {

    public static void main(String[] args){
        Gauss gauss = new Gauss();
        Macierz m = new Macierz(5);
        m.drukuj();
        System.out.println(gauss.PG(m.macierz, m.wektor));
        m.drukuj();
    }
}
