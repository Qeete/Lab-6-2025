package functions.basic;

// Класс для вычисления тангенса
public class Tan extends TrigonometricFunction {
    
    // Вычисляет значение тангенса в точке x
    public double getFunctionValue(double x) {
        return Math.tan(x);
    }
}