package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

    // Конструктор по умолчанию
    public FunctionPointIndexOutOfBoundsException() { 
        super();
    }
    
    // Конструктор с сообщением об ошибке
    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }
    
    // Конструктор с указанием проблемного индекса
    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Точка выходит за границы: " + index);
    }
}