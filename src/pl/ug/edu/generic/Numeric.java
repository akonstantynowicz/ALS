//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.generic;

public interface Numeric<T> {

    T add(T other);

    T subtract(T other);

    T multiply(T other);

    T divide(T other);

    T abs();

    T getZero();

    Boolean isGreaterThan(T other);
}
