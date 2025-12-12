package functions.basic;

import functions.Function;


 //Класс для вычисления экспоненциальной функции e^x

public class Exp implements Function {

     //Возвращает значение левой границы области определения (минус бесконечность)
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    //Возвращает значение правой границы области определения (плюс бесконечность)

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    //Вычисляет значение экспоненты в точке x

    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
}