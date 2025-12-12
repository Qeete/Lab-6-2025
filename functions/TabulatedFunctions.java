package functions;

import java.io.*;
// Вспомогательный класс со статическими методами для работы с табулированными функциями
public class TabulatedFunctions {
    
    // Приватный конструктор чтобы нельзя было создать объект класса
    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать объект класса TabulatedFunctions");
    }
    
    // ... существующие методы остаются без изменений ...
    
    // Табулирует функцию на заданном отрезке с заданным количеством точек
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // ... предыдущая реализация ...
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }
        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    
    // Вывод табулированной функции в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        
        // Записываем количество точек
        dataOut.writeInt(function.getPointsCount());
        
        // Записываем координаты всех точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        
        // Не закрываем поток, чтобы вызывающий код мог продолжать использовать его
        dataOut.flush();
    }
    
    // Ввод табулированной функции из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        
        // Читаем количество точек
        int pointsCount = dataIn.readInt();
        
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        
        // Создаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }
    
    // Запись табулированной функции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter writer = new BufferedWriter(out);
        
        // Записываем количество точек
        writer.write(String.valueOf(function.getPointsCount()));
        writer.write(" ");
        
        // Записываем координаты всех точек через пробел
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.write(String.valueOf(function.getPointX(i)));
            writer.write(" ");
            writer.write(String.valueOf(function.getPointY(i)));
            if (i < function.getPointsCount() - 1) {
                writer.write(" ");
            }
        }
        
        // Не закрываем поток, чтобы вызывающий код мог продолжать использовать его
        writer.flush();
    }
    
    // Чтение табулированной функции из символьного потока
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        
        // Читаем количество точек
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось число (количество точек)");
        }
        int pointsCount = (int) tokenizer.nval;
        
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалось число (координата X)");
            }
            double x = tokenizer.nval;
            
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалось число (координата Y)");
            }
            double y = tokenizer.nval;
            
            points[i] = new FunctionPoint(x, y);
        }
        
        // Создаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }
}