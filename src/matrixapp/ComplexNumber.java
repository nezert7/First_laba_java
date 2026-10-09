package matrixapp;

public class ComplexNumber {
    private final double re;
    private final double im;

    public static final ComplexNumber ZERO = new ComplexNumber(0.0, 0.0);
    public static final ComplexNumber ONE = new ComplexNumber(1.0, 0.0);

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public ComplexNumber(double re) {
        this(re, 0.0);
    }

    public ComplexNumber() {
        this(0.0, 0.0);
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(this.re + other.re, this.im + other.im);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(this.re - other.re, this.im - other.im);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double realPart = this.re * other.re - this.im * other.im;
        double imagPart = this.re * other.im + this.im * other.re;
        return new ComplexNumber(realPart, imagPart);
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denom = other.re * other.re + other.im * other.im;
        if (Math.abs(denom) < 1e-12) {
            throw new ArithmeticException("Деление на комплексный ноль");
        }
        double realPart = (this.re * other.re + this.im * other.im) / denom;
        double imagPart = (this.im * other.re - this.re * other.im) / denom;
        return new ComplexNumber(realPart, imagPart);
    }

    public double absSquare() {
        return re * re + im * im;
    }

    @Override
    public String toString() {
        if (Math.abs(im) < 1e-9) {
            return String.format("%.2f", re);
        }
        if (Math.abs(re) < 1e-9) {
            return String.format("%.2fi", im);
        }
        if (im < 0) {
            return String.format("%.2f - %.2fi", re, Math.abs(im));
        }
        return String.format("%.2f + %.2fi", re, im);
    }

    public static ComplexNumber parse(String s) {
        String cleaned = s.replaceAll("\\s+", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка числа");
        }

        if (cleaned.equals("i") || cleaned.equals("+i")) return new ComplexNumber(0, 1);
        if (cleaned.equals("-i")) return new ComplexNumber(0, -1);

        if (!cleaned.contains("i")) {
            return new ComplexNumber(Double.parseDouble(cleaned), 0.0);
        }

        cleaned = cleaned.substring(0, cleaned.length() - 1);
        int lastSign = Math.max(cleaned.lastIndexOf('+'), cleaned.lastIndexOf('-'));

        if (lastSign <= 0) {
            double imVal = cleaned.isEmpty() || cleaned.equals("+") ? 1.0 :
                    cleaned.equals("-") ? -1.0 : Double.parseDouble(cleaned);
            return new ComplexNumber(0.0, imVal);
        }

        double reVal = Double.parseDouble(cleaned.substring(0, lastSign));
        String imSub = cleaned.substring(lastSign);
        double imVal = imSub.equals("+") ? 1.0 :
                imSub.equals("-") ? -1.0 : Double.parseDouble(imSub);

        return new ComplexNumber(reVal, imVal);
    }
}