package pl.ug.edu.gauss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pl.ug.edu.generic.Double;

public class Matrix {
    public int M;
    public int N;
    public Double[][] matrix;
    public Double[] vectorX;
    public Double[] vector;

    public void drukujWszystko() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + " *" + vectorX[j]);
            }
            System.out.print(" | " + vector[i] + "\n");
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
//        vectorX = new Double[size];
        vector = new Double[M];

//        getRandomMatrix();
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
            vector[i] = Double.valueOf(0);
        }
    }

    public void generateRandomMatrix() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Double.valueOf(Math.random());
            }
        }
    }

    public void generateUnitMatrix() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    matrix[i][j] = Double.valueOf(1);
                }
                else {
                    matrix[i][j] = Double.valueOf(0);
                }
            }
        }
    }

    private void losujWektorX() {
        int r;
        for (int i = 0; i < M; i++) {
            r = losujR();
            vectorX[i] = Double.valueOf(r).divide(Double.valueOf(65536));
        }
    }

    public void calculateVector(List<Integer> userRatings, ArrayList<Integer> ratedProductIds, Matrix P){
        for (int i=0;i<M;i++){
            vector[i]=Double.valueOf(0);
            for (int id : ratedProductIds) {
                vector[i] = vector[i].add(Double.valueOf(userRatings.get(id)).multiply(P.matrix[i][id]));
            }
        }
    }

  public void calculateVector(List<Integer> productRatings, List<Integer> raitingUsersIds, Matrix U,
                              int p) {
        for (int i=0;i<M;i++){
            vector[i]=Double.valueOf(0);
            for (int id : raitingUsersIds) {
                vector[i] = vector[i].add(Double.valueOf(productRatings.get(id)).multiply(U.matrix[i][id]));
            }
        }
    }

    public Matrix transpose() {
        Matrix transposedMatrix = new Matrix(N,M);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                transposedMatrix.matrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    public Matrix multiply(Matrix m) {
        Matrix product = new Matrix(this.M,m.N);
        for (int i=0;i<product.M;i++){
            for(int j=0;j<product.N;j++){
                product.matrix[i][j]=Double.valueOf(0);
                for (int k=0;k<m.M;k++){
                    product.matrix[i][j] = product.matrix[i][j].add(this.matrix[i][k].multiply(m.matrix[k][j]));
                }
            }
        }
        return product;
    }

    public void multiply(Double number){
        for (int i=0;i<this.M;i++){
            for (int j=0;j<this.N;j++){
                this.matrix[i][j] = this.matrix[i][j].multiply(number);
            }
        }
    }

    public Matrix add(Matrix m) {
        Matrix sum = new Matrix(this.M,this.N);
        for(int i=0;i<sum.M;i++){
            for (int j=0;j<sum.N;j++){
                sum.matrix[i][j] = this.matrix[i][j].add(m.matrix[i][j]);
            }
        }
        return sum;
    }

    public void swapWithSolution(List<Double> solution, int columnIndex){
        for(int i=0;i<M;i++){
            matrix[i][columnIndex]=solution.get(i);
        }
    }

}
