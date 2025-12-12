package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Синхронизация: ждем, пока задание не будет готово
                synchronized (SimpleGenerator.lock) {
                    while (!SimpleGenerator.taskReady || SimpleGenerator.taskCompleted) {
                        SimpleGenerator.lock.wait();
                    }
                }
                
                // Получаем данные задания
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getStep();
                
                try {
                    // Вычисляем интеграл
                    double result = Functions.integrate(
                        task.getFunction(), left, right, step);
                    
                    // Выводим результат
                    System.out.printf("Result %.4f %.4f %.4f %.8f (task=%d)%n", 
                        left, right, step, result, i+1);
                    
                } catch (IllegalArgumentException e) {
                    System.out.printf("Ошибка в задании %d: %s%n", i+1, e.getMessage());
                }
                
                // Синхронизация: помечаем задание как выполненное
                synchronized (SimpleGenerator.lock) {
                    SimpleGenerator.taskCompleted = true;
                    SimpleGenerator.taskReady = false;
                    SimpleGenerator.lock.notifyAll();
                }
                
                // Небольшая пауза для наглядности
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Интегратор прерван");
            Thread.currentThread().interrupt();
        }
    }
}