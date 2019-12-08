package pl.ug.edu.gauss;

import pl.ug.edu.generic.Double;

import java.util.Random;

public class Macierz {
    static int M;
    static int N;
    public Double[][] macierz;
    public Double[] wektorX;
    public Double[] wektor;

    public void drukujWszystko() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(macierz[i][j] + " *" + wektorX[j]);
            }
            System.out.print(" | " + wektor[i] + "\n");
        }
    }

    public void drukuj(){
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(macierz[i][j]);
            }
            System.out.println();
        }
    }

    public Macierz(int M,int N) {
        this.M = M;
        this.N = N;
        int size;
        if(M>N){
            size = M;
        }else {
            size = N;
        }
        macierz = new Double[size][size];
        wektorX = new Double[size];
        wektor = new Double[size];

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
            wektor[i] = Double.valueOf(0);
        }
    }

    public void losujMacierz() {
        int r;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                r = losujR();
                macierz[i][j] = Double.valueOf(r).divide(Double.valueOf(65536));
            }
        }
    }

    public void losujWektorX() {
        int r;
        for (int i = 0; i < M; i++) {
            r = losujR();
            wektorX[i] = Double.valueOf(r).divide(Double.valueOf(65536));
        }
    }

    public void obliczWektor() {
        zerujWektor();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                wektor[i] = wektor[i].add(macierz[i][j].multiply(wektorX[j]));
            }
        }
    }

    public void transponujMacierz(){
        int size = macierz.length;
        Double[][] macierz_transponowana = new Double[size][size];
        for (int i=0;i<M;i++){
            for (int j=0; j<N;j++){
                macierz_transponowana[j][i]=macierz[i][j];
            }
        }
        size=N;
        this.N=M;
        this.M=size;
        this.macierz = macierz_transponowana;
    }

}
