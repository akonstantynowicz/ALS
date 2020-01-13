package pl.ug.edu.als;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Matrix;

//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

public class ALS {

  private static final int NUMBER_OF_ATTEMPTS = 30;
  private static final double MILISECONDS_IN_SECOND = 100000;
  private static final int NUMBER_OF_ITERATIONS = 1000000;
  private List<Review> reviewsList;
  private List<Review> testReviewsList = new ArrayList<>();
  private BiMap<Integer, Integer> customers;
  private BiMap<Integer, Integer> products;
  private Matrix p;
  private Matrix u;
  private Matrix initialMatrix;
  private Matrix testMatrix;
  private Matrix resultMatrix;
  private int n;
  private int d;
  private double lambda;
  private int deletedCells;

  public ALS(List<Review> reviewsList, int n) {
    this.reviewsList = reviewsList;
    this.n = n;
    this.testMatrix = new Matrix(n, n);
  }

  public ALS(List<Review> reviewsList, int n, int d, double lambda) {
    this.n = n;
    this.d = d;
    this.lambda = lambda;
    this.reviewsList = reviewsList;
  }

  public int getD() {
    return d;
  }

  public void setD(int d) {
    this.d = d;
  }

  public double getLambda() {
    return lambda;
  }

  public void setLambda(double lambda) {
    this.lambda = lambda;
  }

  public void prepareAlgorithm() {
    prepareInitialData();
    prepareTestData();
  }


  private void prepareTestData() {
    int randomN;
    int randomM;
    testMatrix.matrix = Matrix.cloneMatrix(initialMatrix);
    testMatrix.N=initialMatrix.N;
    testMatrix.M=initialMatrix.M;
    deletedCells = n * n / 8;
    Random random = new Random();
    int counter = 0;
    while (counter < deletedCells) {
      randomN = random.nextInt(initialMatrix.N);
      randomM = random.nextInt(initialMatrix.M);
      if (initialMatrix.matrix[randomM][randomN] != 0) {
        testMatrix.matrix[randomM][randomN] = 0;
        counter++;
        System.out.println("\nUsuwanie z pozycji[" + randomM + "][" + randomN + "]");
      }
    }
    System.out.println("\nTest matrix:");
    testMatrix.print();
  }

  private void prepareInitialData() {
    customers = HashBiMap.create();
    products = HashBiMap.create();

    for (Review r : reviewsList) {
      if (!customers.containsValue(r.getCustomer().getId())) {
        customers.put(customers.size(), r.getCustomer().getId());
      }
      if (!products.containsValue(r.getProduct().getId())) {
        products.put(products.size(), r.getProduct().getId());
      }
    }

    double[][] matrix = new double[customers.size()][products.size()];
    for (Review r : reviewsList) {
      matrix[customers.inverse().get(r.getCustomer().getId())]
          [products.inverse().get(r.getProduct().getId())] = r.getRating();
    }
    initialMatrix = new Matrix(customers.size(), products.size());
    initialMatrix.matrix = matrix;
    System.out.println("\nInitial matrix:");
    initialMatrix.print();
  }

  void runAlgorithm() {
    long startTime;
    long endTime;
    double regularizationParameter;
    double currentGoalFunction;
    double previousGoalFunction;
    double goalFunctionDifference = 0.0;
    double sumGoalFunction = 0.0;
    double timeSum = 0.0;
    int neededIterations = 0;
    for (int attempt = 0; attempt < NUMBER_OF_ATTEMPTS; attempt++) {
      p = generateRandomMatrixOfSize(products.size());
      u = generateRandomMatrixOfSize(customers.size());
      //System.out.println("\np\n");
      //p.print();
      //System.out.println("\nu\n");
      //u.print();
      currentGoalFunction = 0.0;
      for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
        startTime = System.currentTimeMillis();
        calculatePMatrix();
        calculateUMatrix();
        resultMatrix = generateResultMatrix();
        endTime = System.currentTimeMillis();
        previousGoalFunction = currentGoalFunction;
        currentGoalFunction = calculateCurrentGoalFunction();
        regularizationParameter = calculateRegularizationParameter();
        currentGoalFunction += regularizationParameter;
        goalFunctionDifference = Math.abs(currentGoalFunction - previousGoalFunction);
        if (goalFunctionDifference < 0.01) {
          neededIterations = i;
          sumGoalFunction += currentGoalFunction;
          timeSum += (endTime - startTime);
          break;
        }
      }
    }
    printAlgorithmRunInfo(sumGoalFunction, timeSum, neededIterations);
  }

  private void printAlgorithmRunInfo(double sumGoalFunction, double timeSum, int neededIterations) {
    System.out.println("\nd = " + d);
    System.out.println("lambda = " + lambda);
    System.out.println("Srednia ilosc iteracji do osiagniecia roznicy < 0.01: "
        + neededIterations / NUMBER_OF_ATTEMPTS);
    System.out.println("Srednia funkcja celu: " + sumGoalFunction / NUMBER_OF_ATTEMPTS);
    System.out
        .println("Sredni czas: " + (timeSum / NUMBER_OF_ATTEMPTS) / MILISECONDS_IN_SECOND + "s ");
    System.out.println("\nResult matrix: ");
    resultMatrix.print();
    System.out
        .println("\nPoprawność implementacji(Suma różnica między macierzą wynikową a testową:\n"
            + calculateError());
  }

  private double calculateCurrentGoalFunction() {
    double currentGoalFunction;
    currentGoalFunction = 0.0;
    for (int n = 0; n < customers.size(); n++) {
      for (int m = 0; m < products.size(); m++) {
        if (this.initialMatrix.matrix[n][m] != 0.0) {
          currentGoalFunction += Math
              .pow(this.initialMatrix.matrix[n][m] - resultMatrix.matrix[n][m],
                  2);
        }
      }
    }
    return currentGoalFunction;
  }

  private double calculateRegularizationParameter() {
    double regularizationParameter;
    regularizationParameter = 0.0;
    for (int n = 0; n < d; n++) {
      for (int m = 0; m < products.size(); m++) {
        regularizationParameter += Math.pow(u.matrix[n][m], 2);
      }
    }
    for (int n = 0; n < d; n++) {
      for (int m = 0; m < products.size(); m++) {
        regularizationParameter += Math.pow(p.matrix[n][m], 2);
      }
    }
    regularizationParameter = lambda * regularizationParameter;
    return regularizationParameter;
  }

  private double calculateError() {
    double result = 0;
    for (int i = 0; i < initialMatrix.M; i++) {
      for (int j = 0; j < initialMatrix.N; j++) {
        if (initialMatrix.matrix[i][j] != 0 && testMatrix.matrix[i][j] == 0) {
          result += Math.abs(resultMatrix.matrix[i][j] - initialMatrix.matrix[i][j]);
          result = result / deletedCells;
        }
      }
    }
    return result;
  }

  private Matrix generateResultMatrix() {
    return u.transpose().multiply(p);
  }

  private void calculateUMatrix() {
    List<Integer> IU;
    for (int u = 0; u < customers.size(); u++) {
      IU = new ArrayList<>();

      for (int i = 0; i < products.size(); i++) {
        if (initialMatrix.matrix[u][i] != 0) {
          IU.add(i);
        }
      }

      Matrix PIu = calculateXIy(IU, p);
      Matrix PIuT = PIu.transpose();
      Matrix AU = calculateAX(PIu, PIuT);
      calculateAUVector(IU, initialMatrix.matrix[u], PIu, AU);
      replaceColumns(u, AU, this.u);
    }
  }

  private void calculatePMatrix() {
    List<Integer> IP;
    for (int p = 0; p < products.size(); p++) {
      IP = new ArrayList<>();

      for (int i = 0; i < customers.size(); i++) {
        if (initialMatrix.matrix[i][p] != 0) {
          IP.add(i);
        }
      }

      Matrix UIp = calculateXIy(IP, u);
      Matrix UIpT = UIp.transpose();
      Matrix BU = calculateAX(UIp, UIpT);
      calculateBUVector(IP, p, UIp, BU);
      replaceColumns(p, BU, this.p);
    }

  }

  private Matrix calculateXIy(List<Integer> IX, Matrix y) {
    Matrix XIy = new Matrix(d, IX.size());
    for (int i = 0; i < d; i++) {
      int j = 0;
      for (int idx : IX) {
        XIy.matrix[i][j] = y.matrix[i][idx];
        j++;
      }
    }
    return XIy;
  }

  private Matrix calculateAX(Matrix PIu, Matrix PIuT) {
    Matrix E = new Matrix(d, d);
    E.generateUnitMatrix(lambda);

    return PIu.multiply(PIuT).add(E);
  }

  private void calculateAUVector(List<Integer> IU, double[] matrix, Matrix PIu, Matrix AU) {
    for (int i = 0; i < AU.M; i++) {
      AU.vector[i] = 0;
      int j = 0;
      for (int idx : IU) {
        AU.vector[i] += PIu.matrix[i][j] * matrix[idx];
        j++;
      }
    }
  }

  private void calculateBUVector(List<Integer> IP, int p, Matrix UIp, Matrix BU) {
    for (int i = 0; i < BU.M; i++) {
      BU.vector[i] = 0;
      int j = 0;
      for (int idx : IP) {
        BU.vector[i] += UIp.matrix[i][j] * initialMatrix.matrix[idx][p];
        j++;
      }
    }
  }

  private void replaceColumns(int u, Matrix AU, Matrix u2) {
    Gauss gauss = new Gauss(AU.M, AU.N);
    List<Double> solution = gauss.PG(AU.matrix, AU.vector);
    for (int i = 0; i < u2.M; i++) {
      u2.matrix[i][u] = solution.get(i);
    }
  }

  private Matrix generateRandomMatrixOfSize(int size) {
    Matrix m = new Matrix(d, size);
    m.generateRandomMatrix();
    return m;
  }
}


