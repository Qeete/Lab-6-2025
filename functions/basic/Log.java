package functions.basic;

import functions.Function;

// Класс для вычисления логарифма по заданному основанию
public class Log implements Function {
    private double base;
    
    // Конструктор логарифма
    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1");
        }
        this.base = base;
    }
    
    // Возвращает значение левой границы области определения (0, не включая)
    public double getLeftDomainBorder() {
        return 0.0;
    }
    
    // Возвращает значение правой границы области определения (плюс бесконечность)
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    // Вычисляет значение логарифма в точке x
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        return Math.log(x) / Math.log(base);
    }
    
    // Возвращает основание логарифма
    public double getBase() {
        return base;
    }
}