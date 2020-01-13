//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.gauss;

import java.util.ArrayList;
import java.util.List;

public class Matrix {

  public int M;

  public int N;

  public double[][] matrix;

  public double[] vector;

  public Matrix(int M, int N) {
    this.M = M;
    this.N = N;
    matrix = new double[M][N];
    vector = new double[M];
  }

  public static double[][] cloneMatrix(Matrix matrix) {
    Matrix newMatrix = new Matrix(matrix.M, matrix.N);
    for (int i = 0; i < matrix.M; i++) {
      for (int j = 0; j < matrix.N; j++) {
        newMatrix.matrix[i][j] = matrix.matrix[i][j];
      }
    }
    return newMatrix.matrix;
  }

  public void print() {
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        System.out.format("%.2f ", matrix[i][j]);
      }
      System.out.println();
    }
  }

  public void generateRandomMatrix() {
    double max = 0.4;
    double min = 0.1;
    double range = max - min;
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        matrix[i][j] = (Math.random() * range) + min;
      }
    }
  }

  public void generateUnitMatrix() {
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (i == j) {
          matrix[i][j] = 1d;
        } else {
          matrix[i][j] = 0;
        }
      }
    }
  }

  public void generateUnitMatrix(double reg) {
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (i == j) {
          matrix[i][j] = reg;
        } else {
          matrix[i][j] = 0;
        }
      }
    }
  }


  public void calculateVector(List<Integer> userRatings, ArrayList<Integer> ratedProductIds,
      Matrix P) {
    for (int i = 0; i < M; i++) {
      vector[i] = 0;
      for (int id : ratedProductIds) {
        vector[i] += Double.valueOf(userRatings.get(id)) * P.matrix[i][id];
      }
    }
  }

  public void calculateVector(List<Integer> productRatings, List<Integer> raitingUsersIds, Matrix U,
      int p) {
    for (int i = 0; i < M; i++) {
      vector[i] = 0;
      for (int id : raitingUsersIds) {
        vector[i] += Double.valueOf(productRatings.get(id)) * U.matrix[i][id];
      }
    }
  }

  public Matrix transpose() {
    Matrix transposedMatrix = new Matrix(N, M);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        transposedMatrix.matrix[j][i] = matrix[i][j];
      }
    }

    return transposedMatrix;
  }

  public Matrix multiply(Matrix m) {
    Matrix product = new Matrix(this.M, m.N);
    for (int i = 0; i < product.M; i++) {
      for (int j = 0; j < product.N; j++) {
        product.matrix[i][j] = 0;
        for (int k = 0; k < m.M; k++) {
          product.matrix[i][j] += this.matrix[i][k] * m.matrix[k][j];
        }
      }
    }
    return product;
  }

  public void multiply(Double number) {
    for (int i = 0; i < this.M; i++) {
      for (int j = 0; j < this.N; j++) {
        this.matrix[i][j] = this.matrix[i][j] * number;
      }
    }
  }

  public Matrix add(Matrix m) {
    Matrix sum = new Matrix(this.M, this.N);
    for (int i = 0; i < sum.M; i++) {
      for (int j = 0; j < sum.N; j++) {
        sum.matrix[i][j] = this.matrix[i][j] + m.matrix[i][j];
      }
    }
    return sum;
  }

  public void swapWithSolution(List<java.lang.Double> solution, int columnIndex) {
    for (int i = 0; i < M; i++) {
      matrix[i][columnIndex] = solution.get(i);
    }
  }


}
