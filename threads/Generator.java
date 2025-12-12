package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private Random random = new Random();
    
    private volatile boolean running = true;
    
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                if (!running || isInterrupted()) {
                    System.out.println("Генератор: получен сигнал прерывания");
                    break;
                }
                
                // Генерируем параметры
                double base = 1 + random.nextDouble() * 9;
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();
                
                if (left <= 0) left = 0.001;
                
                // Захватываем семафор для записи (только один поток)
                semaphore.acquire();
                
                try {
                    // Устанавливаем задание
                    task.setFunction(new Log(base));
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setStep(step);
                } finally {
                    // Освобождаем семафор
                    semaphore.release();
                }
                
                System.out.printf("Generator: Source %.4f %.4f %.4f (base=%.4f, task=%d)%n", 
                    left, right, step, base, i+1);
                
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.out.println("Генератор: прерван");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Генератор: завершил работу");
        }
    }
    
    public void stopRunning() {
        running = false;
        this.interrupt();
    }
}