package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final long serialVersionUID = 1L;
    
    public FunctionPoint[] points;
    public int pointsCount;
    private static final double EPSILON = 1e-10;
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // Проверка условий конструктора
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        this.points = new FunctionPoint[pointsCount];
        this.pointsCount = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step; 
            points[i] = new FunctionPoint(x, 0);
        }
    }
    
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        // Проверка условий конструктора
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        this.points = new FunctionPoint[values.length];
        this.pointsCount = values.length;
    
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        // Проверка минимального количества точек
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        // Проверка упорядоченности точек по X
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null || points[i + 1] == null) {
                throw new IllegalArgumentException("Массив точек не должен содержать null");
            }
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }
        
        // Обеспечение инкапсуляции - создаем копию массива и копии точек
        this.points = new FunctionPoint[points.length];
        this.pointsCount = points.length;
        
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Точка не может быть null");
            }
            this.points[i] = new FunctionPoint(points[i]);
        }
    }
        
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    
    public double linearInterpolation(FunctionPoint p1, FunctionPoint p2, double x) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
    
    public double getFunctionValue(double x){
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            return Double.NaN;
        } 
        
        for(int i = 0; i < pointsCount - 1; i++){
            double curX = points[i].getX();
            double nextX = points[i+1].getX();
            
            if(Math.abs(x - curX) < EPSILON){
                return points[i].getY();
            }
            
            if(x > curX && x < nextX){
                return linearInterpolation(points[i], points[i+1], x);
            }
            
            if(Math.abs(x - nextX) < EPSILON){
                return points[i+1].getY();
            }
        }
        return points[pointsCount - 1].getY();
    }
    
    public int getPointsCount() {
        return pointsCount;
    }
    
    public FunctionPoint getPoint(int index){
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        
        // Проверка упорядоченности точек
        if (index > 0 && point.getX() <= points[index-1].getX()) {
            throw new InappropriateFunctionPointException("X координата точки должна быть больше предыдущей");
        }
        if (index < pointsCount - 1 && point.getX() >= points[index+1].getX()) {
            throw new InappropriateFunctionPointException("X координата точки должна быть меньше следующей");
        }
        
        points[index] = new FunctionPoint(point);
    }
    
    public double getPointX(int index){
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        
        // Проверка упорядоченности точек
        if (index > 0 && x <= points[index-1].getX()) {
            throw new InappropriateFunctionPointException("X координата должна быть больше предыдущей");
        }
        if (index < pointsCount - 1 && x >= points[index+1].getX()) {
            throw new InappropriateFunctionPointException("X координата должна быть меньше следующей");
        }
        
        points[index].setX(x);
    }

    public double getPointY(int index){
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y){
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        points[index].setY(y);
    }

    public void deletePoint(int index){
        // Проверка границ индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс точки: " + index);
        }
        
        // Проверка минимального количества точек
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }
        
        if (pointsCount - 1 - index >= 0) {
            System.arraycopy(points, index + 1, points, index, pointsCount - 1 - index);
        }
        pointsCount--;
        points[pointsCount] = null;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionPoint newPoint = new FunctionPoint(point);
        double newX = newPoint.getX();
        
        // Проверяем, не существует ли уже точка с таким X
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - newX) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }
        
        // Создаем новый массив на 1 элемент больше
        FunctionPoint[] newArray = new FunctionPoint[pointsCount + 1];
        
        // Находим позицию для вставки
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < newX) {
            insertIndex++;
        }
        
        // Копируем элементы до позиции вставки с помощью System.arraycopy
        if (insertIndex > 0) {
            System.arraycopy(points, 0, newArray, 0, insertIndex);
        }
        
        // Вставляем новую точку
        newArray[insertIndex] = newPoint;
        
        // Копируем элементы после позиции вставки с помощью System.arraycopy
        if (pointsCount - insertIndex > 0) {
            System.arraycopy(points, insertIndex, newArray, insertIndex + 1, pointsCount - insertIndex);
        }
        
        // Заменяем старый массив новым
        points = newArray;
        pointsCount++;
    }

    // Лаборатная №5
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            sb.append(points[i].toString()); // Используем toString() точки
            if (i < pointsCount - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Переопределение метода equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || !(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;
        
        if (this.getPointsCount() != that.getPointsCount()) return false;
        
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction arrayThat = (ArrayTabulatedFunction) o;
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(arrayThat.points[i])) {
                    return false;
                }
            }
        } else {
            // Общий случай для любой TabulatedFunction
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint thatPoint = that.getPoint(i);
                
                if (!thisPoint.equals(thatPoint)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    // Переопределение метода hashCode()
    @Override
    public int hashCode() {
        int hash = pointsCount; // Начинаем с количества точек
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        
        return hash;
    }

    // Переопределение метода clone()
    @Override
    public Object clone() {
        double leftX = points[0].getX();
        double rightX = points[pointsCount - 1].getX();
        double[] values = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            values[i] = points[i].getY();
        }
        ArrayTabulatedFunction cloneFunc = new ArrayTabulatedFunction(leftX, rightX, values);
        return cloneFunc;
    }
}