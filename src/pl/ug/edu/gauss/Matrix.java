package pl.ug.edu.gauss;

import pl.ug.edu.generic.Double;

import java.util.Random;

public class Matrix {
    private int M;
    private int N;
    public Double[][] matrix;
    public Double[] wektorX;
    public Double[] wektor;

    public void drukujWszystko() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + " *" + wektorX[j]);
            }
            System.out.print(" | " + wektor[i] + "\n");
        }
    }

    public void print() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        matrix = new Double[M][N];
//        wektorX = new Double[size];
//        wektor = new Double[size];

        getRandomMatrix();
//        losujWektorX();
//        obliczWektor();
    }

    private static int losujR() {
        Random ran = new Random();
        int r = ran.nextInt(131071) - 65536;
        return r;
    }

    private void zerujWektor() {
        for (int i = 0; i < N; i++) {
            wektor[i] = Double.valueOf(0);
        }
    }

    private void getRandomMatrix() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Double.valueOf(Math.random());
            }
        }
    }

    private void losujWektorX() {
        int r;
        for (int i = 0; i < M; i++) {
            r = losujR();
            wektorX[i] = Double.valueOf(r).divide(Double.valueOf(65536));
        }
    }

    private void obliczWektor() {
        zerujWektor();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                wektor[i] = wektor[i].add(matrix[i][j].multiply(wektorX[j]));
            }
        }
    }

    public void transpose() {
        int temp = N;
        this.N = M;
        this.M = temp;

        Double[][] transposedMatrix = new Double[M][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        this.matrix = transposedMatrix;
    }

}
