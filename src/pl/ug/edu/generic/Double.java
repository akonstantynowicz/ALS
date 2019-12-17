//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.generic;

public class Double implements Numeric<Double> {

  final private double number;

  public Double(double number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return String.format("%9.5f", number);
  }

  public static Double valueOf(double d) {
    return new Double(d);
  }

  public double doubleValue() {
    return this.number;
  }

  @Override
  public Double add(Double other) {
    return Double.valueOf(this.number + other.number);
  }

  @Override
  public Double subtract(Double other) {
    return Double.valueOf(this.number - other.number);
  }

  @Override
  public Double multiply(Double other) {
    return Double.valueOf(this.number * other.number);
  }

  @Override
  public Double divide(Double other) {
    return Double.valueOf(this.number / other.number);
  }

  @Override
  public Double abs() {
    return Double.valueOf(Math.abs(this.number));
  }

  @Override
  public Double getZero() {
    return Double.valueOf(0);
  }

  @Override
  public Boolean isGreaterThan(Double other) {
    return (this.number > other.number);
  }

}
