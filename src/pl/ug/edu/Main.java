package pl.ug.edu;

public class Main {

    public static void main(String[] args){
        Double d = new Double(6.20);
        System.out.println(d);
        Macierz m = new Macierz(5);
        m.drukuj();
        Gauss gauss = new Gauss();
        System.out.println(gauss.PG(m.macierz,m.wektor));
    }
}
