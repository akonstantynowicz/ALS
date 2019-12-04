
public class Double implements Numeric<Double>{
    final private double number;

    @Override
    public String toString() {
        return String.format("%9.5f", number);
    }

    public Double(double number) {
        this.number = number;
    }

    static Double of(double d) {
        return new Double(d);
    }

    @Override
    public Double add(Double other) {
        return Double.of(this.number + other.number);
    }

    @Override
    public Double subtract(Double other) {
        return Double.of(this.number - other.number);
    }

    @Override
    public Double multiply(Double other) {
        return Double.of(this.number * other.number);
    }

    @Override
    public Double divide(Double other) {
        return Double.of(this.number / other.number);
    }

    @Override
    public Double abs() {
        return Double.of(Math.abs(this.number));
    }

    @Override
    public Boolean isGreaterThan(Double other) {
        return (this.number > other.number);
    }
}
