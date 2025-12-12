package functions.meta;

import functions.Function;

// Класс для возведения функции в степень
public class Power implements Function {
    private Function f;
    private double power;
    
    // Конструктор получает функцию и степень
    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }
    
    // Область определения совпадает с областью определения исходной функции
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }
    
    // Область определения совпадает с областью определения исходной функции
    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }
    
    // Значение функции - значение исходной функции в заданной степени
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return Math.pow(f.getFunctionValue(x), power);
    }
    
    // Возвращает степень
    public double getPower() {
        return power;
    }
}