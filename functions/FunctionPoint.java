package functions;

import java.io.*;

public class FunctionPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public double x;
    public double y;
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    public FunctionPoint(){
        this.x = 0.0;
        this.y = 0.0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    // Лабораторная №5

    // Переопределение метода ToString()
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
    // Переопределение метода equals()
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;
        return Math.abs(this.x - that.x) < 1e-10 && Math.abs(this.y - that.y) < 1e-10;
    }

    // Переопределние метода hashCode()
    @Override
    public int hashCode() {
        // Преобразуем double в long биты
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int x1 = (int)(xBits & 0xFFFFFFFFL);      // Младшие 32 бита x
        int x2 = (int)(xBits >>> 32);             // Старшие 32 бита x
        int y1 = (int)(yBits & 0xFFFFFFFFL);      // Младшие 32 бита y
        int y2 = (int)(yBits >>> 32);             // Старшие 32 бита y

        return x1 ^ x2 ^ y1 ^ y2;
    }

    // Переопределение метода clone()
    @Override
    public Object clone() {
        FunctionPoint clonePoint = new FunctionPoint(this.x, this.y);
        return clonePoint;
    }

}
