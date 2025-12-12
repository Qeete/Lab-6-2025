package functions.basic;

// Класс для вычисления косинуса
public class Cos extends TrigonometricFunction {
    
    // Вычисляет значение косинуса в точке x
    public double getFunctionValue(double x) {
        return Math.cos(x);
    }
}