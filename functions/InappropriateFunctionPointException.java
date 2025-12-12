package functions;

public class InappropriateFunctionPointException extends Exception {

     // Конструктор по умолчанию
    public InappropriateFunctionPointException() {
        super();
    }
    
    // Конструктор с сообщением об ошибке
    public InappropriateFunctionPointException(String message) {
        super(message);
    }
    
    //Конструктор с сообщением и причиной исключения

    public InappropriateFunctionPointException(String message, Throwable cause) {
        super(message, cause);
    }
}