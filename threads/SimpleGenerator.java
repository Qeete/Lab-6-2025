package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    private Random random = new Random();
    
    // Флаг и объект для синхронизации (делаем public)
    public static final Object lock = new Object();
    public static boolean taskReady = false;
    public static boolean taskCompleted = false;
    
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Генерируем случайные параметры
                double base = 1 + random.nextDouble() * 9; // [1, 10)
                double left = random.nextDouble() * 100; // [0, 100)
                double right = 100 + random.nextDouble() * 100; // [100, 200)
                double step = random.nextDouble(); // [0, 1)
                
                // Проверяем, что левая граница > 0 для логарифма
                if (left <= 0) left = 0.001;
                
                // Синхронизация: ждем, пока предыдущее задание не будет обработано
                synchronized (lock) {
                    while (taskReady && !taskCompleted) {
                        lock.wait();
                    }
                    
                    // Устанавливаем задание
                    task.setFunction(new Log(base));
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setStep(step);
                    
                    // Помечаем задание как готовое
                    taskReady = true;
                    taskCompleted = false;
                    
                    // Уведомляем интегратор
                    lock.notifyAll();
                }
                
                // Выводим информацию
                System.out.printf("Source %.4f %.4f %.4f (base=%.4f, task=%d)%n", 
                    left, right, step, base, i+1);
                
                // Небольшая пауза для наглядности
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Генератор прерван");
            Thread.currentThread().interrupt();
        }
    }
}