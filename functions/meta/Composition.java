package functions.meta;

import functions.Function;

// Класс для композиции двух функций
public class Composition implements Function {
    private Function f1;
    private Function f2;
    
    // Конструктор получает две функции для композиции
    public Composition(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }
    
    // Область определения совпадает с областью определения первой функции
    public double getLeftDomainBorder() {
        return f1.getLeftDomainBorder();
    }
    
    // Область определения совпадает с областью определения первой функции
    public double getRightDomainBorder() {
        return f1.getRightDomainBorder();
    }
    
    // Значение функции - композиция f2(f1(x))
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double innerValue = f1.getFunctionValue(x);
        // Проверяем, что внутреннее значение попадает в область определения f2
        if (innerValue < f2.getLeftDomainBorder() || innerValue > f2.getRightDomainBorder()) {
            return Double.NaN;
        }
        return f2.getFunctionValue(innerValue);
    }
    
    // Возвращает первую функцию
    public Function getFirstFunction() {
        return f1;
    }
    
    // Возвращает вторую функцию
    public Function getSecondFunction() {
        return f2;
    }
}