package functions.basic;

import functions.Function;

// Базовый класс для тригонометрических функций
public abstract class TrigonometricFunction implements Function {
    
    // Тригонометрические функции определены на всей числовой прямой
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    // Тригонометрические функции определены на всей числовой прямой
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    // Абстрактный метод для вычисления значения функции
    public abstract double getFunctionValue(double x);
}