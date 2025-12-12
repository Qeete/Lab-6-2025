package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    // Голова списка (не содержит данных, всегда существует)
    private FunctionNode head;
    
    // Количество значащих элементов (без головы)
    private int pointsCount;
    
    // Вспомогательные поля для оптимизации доступа
    private transient FunctionNode lastAccessedNode;
    private transient int lastAccessedIndex;
    
    // Константа для сравнения вещественных чисел
    private static final double EPSILON = 1e-10;

    // Конструктор по умолчанию для Externalizable
    public LinkedListTabulatedFunction() {
        // Создаем голову, которая ссылается сама на себя
        head = new FunctionNode();
        head.prev = head;
        head.next = head;
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    // Реализация Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        
        // Записываем все точки
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Инициализируем список
        head = new FunctionNode();
        head.prev = head;
        head.next = head;
        pointsCount = 0;
        
        int count = in.readInt();
        
        // Читаем и добавляем точки
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, y);
        }
    }
    // Конструктор с параметрами (равномерная сетка)
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this(); // Вызываем конструктор по умолчанию для инициализации головы
        
        // Проверка условий конструктора
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        // Создаем точки равномерной сетки
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, 0);
        }
    }
    
    // Конструктор с параметрами (массив значений)
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this(); // Вызываем конструктор по умолчанию для инициализации головы
        
        // Проверка условий конструктора
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        // Создаем точки с заданными значениями
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        this(); // Вызываем конструктор по умолчанию для инициализации головы
        
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
        
        // Обеспечение инкапсуляции - создаем копии точек
        for (FunctionPoint point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Точка не может быть null");
            }
            addNodeToTail().point = new FunctionPoint(point);
        }
    }
    
    
    // Метод для получения узла по индексу с оптимизацией доступа
    private FunctionNode getNodeByIndex(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс: " + index);
        }
        
        // Оптимизация: если запрашиваем тот же элемент, что и в прошлый раз
        if (lastAccessedIndex == index) {
            return lastAccessedNode;
        }
        
        // Оптимизация: если запрашиваем следующий элемент
        if (lastAccessedIndex != -1 && lastAccessedIndex == index - 1) {
            lastAccessedNode = lastAccessedNode.next;
            lastAccessedIndex = index;
            return lastAccessedNode;
        }
        
        // Оптимизация: если запрашиваем предыдущий элемент
        if (lastAccessedIndex != -1 && lastAccessedIndex == index + 1) {
            lastAccessedNode = lastAccessedNode.prev;
            lastAccessedIndex = index;
            return lastAccessedNode;
        }
        
        // Поиск с ближайшего конца
        FunctionNode current;
        int currentIndex;
        
        if (index < pointsCount / 2) {
            // Ищем с начала
            current = head.next;
            currentIndex = 0;
            while (currentIndex < index) {
                current = current.next;
                currentIndex++;
            }
        } else {
            // Ищем с конца
            current = head.prev;
            currentIndex = pointsCount - 1;
            while (currentIndex > index) {
                current = current.prev;
                currentIndex--;
            }
        }
        
        // Сохраняем для будущей оптимизации
        lastAccessedNode = current;
        lastAccessedIndex = index;
        
        return current;
    }
    
    // Метод для добавления узла в конец списка
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        
        // Вставляем перед головой (что эквивалентно концу списка)
        newNode.prev = head.prev;
        newNode.next = head;
        
        head.prev.next = newNode;
        head.prev = newNode;
        
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = pointsCount - 1;
        
        return newNode;
    }
    
    // Метод для добавления узла по индексу
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс для вставки: " + index);
        }
        
        if (index == pointsCount) {
            // Вставка в конец
            return addNodeToTail();
        }
        
        // Находим узел, перед которым нужно вставить новый
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        
        // Создаем новый узел
        FunctionNode newNode = new FunctionNode();
        newNode.prev = prevNode;
        newNode.next = nextNode;
        
        // Обновляем ссылки соседних узлов
        prevNode.next = newNode;
        nextNode.prev = newNode;
        
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        
        return newNode;
    }
    
    // Метод для удаления узла по индексу
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Некорректный индекс для удаления: " + index);
        }
        
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }
        
        // Находим удаляемый узел
        FunctionNode nodeToDelete = getNodeByIndex(index);
        
        // Обновляем ссылки соседних узлов
        nodeToDelete.prev.next = nodeToDelete.next;
        nodeToDelete.next.prev = nodeToDelete.prev;
        
        pointsCount--;
        
        // Сбрасываем кэш, если удалили кэшированный элемент
        if (lastAccessedIndex == index) {
            lastAccessedNode = head;
            lastAccessedIndex = -1;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }
        
        return nodeToDelete;
    }
    
    // Методы табулированной функции (аналогичные TabulatedFunction)
    
    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.next.point.getX();
    }
    
    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.prev.point.getX();
    }
    
    public double getFunctionValue(double x) {
        if (pointsCount == 0 || x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        
        FunctionNode current = head.next;
        
        // Проверяем первую точку
        if (Math.abs(x - current.point.getX()) < EPSILON) {
            return current.point.getY();
        }
        
        // Ищем интервал
        for (int i = 0; i < pointsCount - 1; i++) {
            double curX = current.point.getX();
            double nextX = current.next.point.getX();
            
            if (Math.abs(x - nextX) < EPSILON) {
                return current.next.point.getY();
            }
            
            if (x > curX && x < nextX) {
                return linearInterpolation(current.point, current.next.point, x);
            }
            
            current = current.next;
        }
        
        return Double.NaN;
    }
    
    private double linearInterpolation(FunctionPoint p1, FunctionPoint p2, double x) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
    
    public int getPointsCount() {
        return pointsCount;
    }
    
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }
    
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        
        // Проверка упорядоченности точек
        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException("X координата точки должна быть больше предыдущей");
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException("X координата точки должна быть меньше следующей");
        }
        
        node.point = new FunctionPoint(point);
    }
    
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }
    
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        
        // Проверка упорядоченности точек
        if (index > 0 && x <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException("X координата должна быть больше предыдущей");
        }
        if (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException("X координата должна быть меньше следующей");
        }
        
        node.point.setX(x);
    }
    
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }
    
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }
    
    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }
    
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();
        
        // Проверяем, не существует ли уже точка с таким X
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(current.point.getX() - newX) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
            current = current.next;
        }
        
        // Находим позицию для вставки
        int insertIndex = 0;
        current = head.next;
        while (insertIndex < pointsCount && current.point.getX() < newX) {
            current = current.next;
            insertIndex++;
        }
        
        // Вставляем новую точку
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    // Лабораторная №5

    // Переопределение метода toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            sb.append(current.point.toString());
            if (i < pointsCount - 1) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    // Переопределение метода equals()
    @Override
    public boolean equals(Object o) {
        // 1. Проверка идентичности ссылок
        if (this == o) return true;
        
        // 2. Проверка на null и совместимость типов
        if (o == null || !(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;
        
        // 3. Проверка количества точек
        if (this.getPointsCount() != that.getPointsCount()) return false;
        
        // 4. Оптимизация для LinkedListTabulatedFunction
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction listThat = (LinkedListTabulatedFunction) o;
            
            // Прямое сравнение узлов списка
            FunctionNode thisCurrent = this.head.next;
            FunctionNode thatCurrent = listThat.head.next;
            
            for (int i = 0; i < pointsCount; i++) {
                if (!thisCurrent.point.equals(thatCurrent.point)) {
                    return false;
                }
                thisCurrent = thisCurrent.next;
                thatCurrent = thatCurrent.next;
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
        
        // Комбинируем хэш-коды всех точек с помощью XOR
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            hash ^= current.point.hashCode();
            current = current.next;
        }
        
        return hash;
    }

    // Переопределение метода clone()
    @Override
    public Object clone() {
        LinkedListTabulatedFunction clone = new LinkedListTabulatedFunction();
        FunctionNode current = this.head.next;
        while (current != this.head) {
            FunctionPoint newPoint = (FunctionPoint) current.point.clone();
            FunctionNode newNode = new FunctionNode(newPoint);
            FunctionNode last = clone.head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = clone.head;
            clone.head.prev = newNode;
            current = current.next;
        }
        clone.pointsCount = this.pointsCount;
        return clone;
    }
}
