package functions.meta;

import functions.Function;

// Класс для суммы двух функций
public class Sum implements Function {
    private Function f1;
    private Function f2;
    
    // Конструктор получает две функции для сложения
    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }
    
    // Левая граница области определения - максимум левых границ
    public double getLeftDomainBorder() {
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    
    // Правая граница области определения - минимум правых границ
    public double getRightDomainBorder() {
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }
    
    // Значение функции - сумма значений двух функций
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}