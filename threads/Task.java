package threads;

import functions.Function;

public class Task {
    private Function function;        // Интегрируемая функция
    private double leftBorder;       // Левая граница интегрирования
    private double rightBorder;      // Правая граница интегрирования
    private double step;             // Шаг дискретизации
    private int tasksCount;          // Количество выполняемых заданий
    
    // Конструктор
    public Task() {
        this.function = null;
        this.leftBorder = 0;
        this.rightBorder = 0;
        this.step = 0;
        this.tasksCount = 0;
    }
    
    // Геттеры и сеттеры
    public Function getFunction() {
        return function;
    }
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public double getLeftBorder() {
        return leftBorder;
    }
    
    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }
    
    public double getRightBorder() {
        return rightBorder;
    }
    
    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }
    
    public double getStep() {
        return step;
    }
    
    public void setStep(double step) {
        this.step = step;
    }
    
    public int getTasksCount() {
        return tasksCount;
    }
    
    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }
}