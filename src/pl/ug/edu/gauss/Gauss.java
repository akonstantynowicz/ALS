package pl.ug.edu.gauss;

import pl.ug.edu.generic.Numeric;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.List;

public class Gauss<T extends Numeric<T>> {
    static int M = 5;
    static int N = 5;

    public Gauss(int N, int M) {
        this.M= M;
        this.N=N;

    }

    public Gauss() {
    }

    private void redukujMacierz(T[][] A, T[] B, int i, int j) {
        T wspolczynnik;
        for (int k = i + 1; k < M; k++) {
            wspolczynnik = A[k][j].divide(A[i][j]);
            for (int l = j; l < N; l++) {
                T tmp = A[k][l];
                A[k][l] = tmp.subtract(wspolczynnik.multiply(A[i][l]));
            }
            B[k] = B[k].subtract(wspolczynnik.multiply(B[i]));
        }
    }

    public List<T> PG(T[][] A, T[] B) {
        T max;
        int p, j;
        for (int i = 0; i < M; i++) {
            j = i;
            max = A[i][j].abs();
            p = i;
            for (int k = i + 1; k < M; k++) {
                if (A[k][j].abs().isGreaterThan(max)) {
                    max = A[k][j].abs();
                    p = k;
                }
            }
            if (A[i][j] != max) {
                for (int l = 0; l < N; l++) {
                    T tmp = A[i][l];
                    A[i][l] = A[p][l];
                    A[p][l] = tmp;
                }
                T tmp = B[i];
                B[i] = B[p];
                B[p] = tmp;
            }
            redukujMacierz(A, B, i, j);
        }
        return dajWynik(A, B);
    }

    public List<T> dajWynik(T[][] A, T[] B) {
        List<T> wynik = initList(M, B[0].getZero());
        List<T> tmp = initList(M - 1, B[0].getZero());
        for (int i = M - 1; i >= 0; i--) {
            for (int j = N - 1; j > i; j--) {
                tmp.set(j - 1, A[i][j].multiply(wynik.get(j)));
                B[i] = B[i].subtract(tmp.get(j - 1));
            }
            wynik.set(i, B[i].divide(A[i][i]));
        }
        return wynik;
    }

    public List<T> initList(int size, T t) {
        List<T> list = new ArrayList<>();
        while (list.size() < size) list.add(t);
        return list;
    }
}
