package pl.ug.edu;

public class Gauss <T extends Numeric<T>>{
    static int N=5;
    GenericMath<T> gm = new GenericMath<>();

    void redukujMacierz(T[][] A, T[] B, int i, int j) {
        T wspolczynnik;
        for (int k = i + 1; k < N; k++) {
            wspolczynnik = (T) gm.add(A[k][j], A[i][j]);
            for (int l = j; l < N; l++) {
                T tmp = A[k][l];
                A[k][l] = (T) gm.subtract(tmp, gm.multiply(wspolczynnik, A[i][l]));
            }
            B[k] = (T) gm.subtract(B[k], gm.multiply(wspolczynnik, B[i]));
        }
    }

    T[] PG(T[][] A, T[] B) {
        T max;
        int p, j;
        for (int i = 0; i < N; i++) {
            j = i;
            max = (T) gm.abs(A[i][j]);
            p = i;
            for (int k = i + 1; k < N; k++) {
                if (gm.isGreaterThan(gm.abs(A[k][j]), max)) {
                    max = (T) gm.abs(A[k][j]);
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

    T[] dajWynik(T[][] A, T[] B) {
        T[] wynik = (T[]) java.lang.reflect.Array.newInstance(A.getClass(),N);
        T[] tmp = (T[]) java.lang.reflect.Array.newInstance(A.getClass(),N);
        for (int i = N - 1; i >= 0; i--) {
            for (int j = N - 1; j > i; j--) {
                tmp[j - 1] = (T) gm.multiply(A[i][j], wynik[j]);
                B[i] = (T) gm.subtract(B[i], tmp[j - 1]);
            }
            wynik[i] = (T) gm.divide(B[i], A[i][i]);
        }
        return wynik;
    }
}
