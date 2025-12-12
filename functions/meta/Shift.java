package functions.meta;

import functions.Function;

// Класс для сдвига функции вдоль осей координат
public class Shift implements Function {
    private Function f;
    private double shiftX;
    private double shiftY;
    
    // Конструктор получает функцию и величины сдвига
    public Shift(Function f, double shiftX, double shiftY) {
        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }
    
    // Область определения сдвигается вдоль оси X
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + shiftX;
    }
    
    // Область определения сдвигается вдоль оси X
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + shiftX;
    }
    
    // Значение функции сдвигается вдоль оси Y
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Для вычисления значения нужно сдвинуть аргумент обратно
        double originalX = x - shiftX;
        return f.getFunctionValue(originalX) + shiftY;
    }
    
    // Возвращает величину сдвига по X
    public double getShiftX() {
        return shiftX;
    }
    
    // Возвращает величину сдвига по Y
    public double getShiftY() {
        return shiftY;
    }
}