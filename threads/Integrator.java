package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;
    
    private volatile boolean running = true;
    
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                if (!running || isInterrupted()) {
                    System.out.println("Интегратор: получен сигнал прерывания");
                    break;
                }
                
                // Захватываем семафор для чтения
                semaphore.acquire();
                
                // Читаем данные
                double left, right, step;
                try {
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getStep();
                } finally {
                    // Освобождаем семафор
                    semaphore.release();
                }
                
                // Вычисляем интеграл
                try {
                    double result = Functions.integrate(
                        task.getFunction(), left, right, step);
                    
                    System.out.printf("Integrator: Result %.4f %.4f %.4f %.8f (task=%d)%n", 
                        left, right, step, result, i+1);
                    
                } catch (IllegalArgumentException e) {
                    System.out.printf("Integrator: Ошибка в задании %d: %s%n", 
                        i+1, e.getMessage());
                }
                
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.out.println("Интегратор: прерван");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Интегратор: завершил работу");
        }
    }
    
    public void stopRunning() {
        running = false;
        this.interrupt();
    }
}