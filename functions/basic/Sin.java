package functions.basic;

// Класс для вычисления синуса
public class Sin extends TrigonometricFunction {
    
    // Вычисляет значение синуса в точке x
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}