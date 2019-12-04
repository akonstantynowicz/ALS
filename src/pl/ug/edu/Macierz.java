import java.util.Arrays;
import java.util.Random;

public class Macierz {
    public int N;
    public Double[][] macierz;
    public Double[] wektorX;
    public Double[] wektor;

    public void drukuj() {
        for (int i=0; i<N; i++){
            for (int j=0;j<N;j++){
                System.out.print(macierz[i][j] + " *" + wektorX[j]);
            }
            System.out.print(" | " + wektor[i] + "\n");
        }
    }

    public Macierz(int N) {
        this.N = N;
        macierz = new Double[N][N];
        wektorX = new Double[N];
        wektor = new Double[N];

        losujMacierz();
        losujWektorX();
        obliczWektor();
    }

    static int losujR() {
        Random ran = new Random();
        int r = ran.nextInt(131071) - 65536;
        return r;
    }

    public void zerujWektor() {
        for (int i = 0; i < N; i++) {
            wektor[i] = Double.of(0);
        }
    }

    public void losujMacierz() {
        int r;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                r = losujR();
                macierz[i][j] = Double.of(r).divide(Double.of(65536));
            }
        }
    }

    public void losujWektorX() {
        int r;
        for (int i = 0; i < N; i++) {
            r = losujR();
            wektorX[i] = Double.of(r).divide(Double.of(65536));
        }
    }

    public void obliczWektor() {
        zerujWektor();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                wektor[i] = wektor[i].add(macierz[i][j].multiply(wektorX[j]));
            }
        }
    }
}
