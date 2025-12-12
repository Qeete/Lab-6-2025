package functions.meta;

import functions.Function;

// Класс для масштабирования функции вдоль осей координат
public class Scale implements Function {
    private Function f;
    private double scaleX;
    private double scaleY;
    
    // Конструктор получает функцию и коэффициенты масштабирования
    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    // Область определения масштабируется вдоль оси X
    public double getLeftDomainBorder() {
        if (scaleX >= 0) {
            return f.getLeftDomainBorder() * scaleX;
        } else {
            return f.getRightDomainBorder() * scaleX;
        }
    }
    
    // Область определения масштабируется вдоль оси X
    public double getRightDomainBorder() {
        if (scaleX >= 0) {
            return f.getRightDomainBorder() * scaleX;
        } else {
            return f.getLeftDomainBorder() * scaleX;
        }
    }
    
    // Значение функции масштабируется вдоль оси Y
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Для вычисления значения нужно "сжать" аргумент обратно
        double originalX = x / scaleX;
        return f.getFunctionValue(originalX) * scaleY;
    }
    
    // Возвращает коэффициент масштабирования по X
    public double getScaleX() {
        return scaleX;
    }
    
    // Возвращает коэффициент масштабирования по Y
    public double getScaleY() {
        return scaleY;
    }
}