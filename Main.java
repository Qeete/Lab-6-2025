import functions.*;
import functions.basic.*;
import java.util.Random;
import java.util.concurrent.Semaphore;
import threads.*;

public class Main {
    public static void main(String[] args){

        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА №6 - ИНТЕГРИРОВАНИЕ ===");
        complicatedThreads(); 
        simpleThreads();
        testIntegration();
        nonThread();
    }
    public static void complicatedThreads() {
        System.out.println("\n=== ВЕРСИЯ С СЕМАФОРОМ (java.util.concurrent.Semaphore) ===");
        
        // Создаем общее задание и семафор с 1 разрешением
        Task task = new Task();
        Semaphore semaphore = new Semaphore(1); // Только один поток может владеть
        task.setTasksCount(100);
        
        System.out.println("Количество заданий: " + task.getTasksCount());
        System.out.println("Запускаем потоки...\n");
        
        // Создаем потоки
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        
        // Эксперименты с приоритетами
        generator.setPriority(Thread.MAX_PRIORITY);  // Генератор имеет высший приоритет
        integrator.setPriority(Thread.MIN_PRIORITY); // Интегратор имеет низший приоритет
        
        // Запускаем потоки
        generator.start();
        integrator.start();
        
        try {
            // Ждем 50 миллисекунд
            Thread.sleep(50);
            
            System.out.println("\nОсновной поток: Прерываю рабочие потоки через 50мс...");
            
            // Прерываем потоки
            generator.stopRunning();
            integrator.stopRunning();
            
            // Ждем завершения с таймаутом
            generator.join(100);
            integrator.join(100);
            
            System.out.println("\nСтатус потоков:");
            System.out.println("Генератор жив: " + generator.isAlive());
            System.out.println("Интегратор жив: " + integrator.isAlive());
            
        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван");
        }
        
        System.out.println("\n=== ЗАВЕРШЕНО ===");
    }

    public static void simpleThreads() {
        System.out.println("\n=== МНОГОПОТОЧНАЯ ВЕРСИЯ ===");
        
        // Создаем общее задание
        Task task = new Task();
        task.setTasksCount(100);
        
        System.out.println("Количество заданий: " + task.getTasksCount());
        System.out.println("Запускаем потоки...\n");
        
        // Создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        
        // Устанавливаем приоритеты для экспериментов
        generatorThread.setPriority(Thread.NORM_PRIORITY);
        integratorThread.setPriority(Thread.NORM_PRIORITY);
        
        // Запускаем потоки
        generatorThread.start();
        integratorThread.start();
        
        try {
            // Ждем завершения потоков
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван");
        }
        
        System.out.println("\n=== ВСЕ ПОТОКИ ЗАВЕРШЕНЫ ===");
    }

    public static void nonThread() {
        System.out.println("\n=== ПОСЛЕДОВАТЕЛЬНАЯ ВЕРСИЯ (без потоков) ===");
        
        Random random = new Random();
        Task task = new Task();
        task.setTasksCount(100); // Минимум 100 заданий
        
        System.out.println("Количество заданий: " + task.getTasksCount());
        System.out.println("Начинаем выполнение...\n");
        
        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                // 1. Создаем логарифмическую функцию со случайным основанием (1-10)
                double base = 1 + random.nextDouble() * 9; // [1, 10)
                Log logFunction = new Log(base);
                task.setFunction(logFunction);
                
                // 2. Левая граница (0-100)
                double left = random.nextDouble() * 100; // [0, 100)
                task.setLeftBorder(left);
                
                // 3. Правая граница (100-200)
                double right = 100 + random.nextDouble() * 100; // [100, 200)
                task.setRightBorder(right);
                
                // 4. Шаг дискретизации (0-1)
                double step = random.nextDouble(); // [0, 1)
                task.setStep(step);
                
                // 5. Выводим информацию об источнике
                System.out.printf("Source %.4f %.4f %.4f (base=%.4f)%n", 
                    left, right, step, base);
                
                // 6. Вычисляем интеграл
                double result = Functions.integrate(task.getFunction(), 
                    task.getLeftBorder(), task.getRightBorder(), task.getStep());
                
                // 7. Выводим результат
                System.out.printf("Result %.4f %.4f %.4f %.8f%n", 
                    left, right, step, result);
                    
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при выполнении задания " + (i+1) + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка при выполнении задания " + (i+1) + ": " + e.getMessage());
            }
        }
        
        System.out.println("\n=== ВЫПОЛНЕНИЕ ЗАВЕРШЕНО ===");
    }

    public static void testIntegration() {
        try {
            functions.basic.Exp exp = new functions.basic.Exp();
            double theoretical = Math.E - 1; // e^1 - e^0
            
            System.out.println("Теоретическое значение: " + theoretical);
            System.out.println("\nШаг\t\tРезультат\t\tПогрешность");
            System.out.println("--------------------------------------------");
            
            double step = 0.1;
            double result;
            int iteration = 0;
            
            do {
                result = Functions.integrate(exp, 0, 1, step);
                double error = Math.abs(result - theoretical);
                
                System.out.printf("%.6f\t%.10f\t%.10f%n", 
                    step, result, error);
                
                if (error < 1e-7) {
                    System.out.println("\n✓ Достигнута точность 1e-7!");
                    System.out.printf("Требуемый шаг: %.8f%n", step);
                    break;
                }
                
                step /= 2;
                iteration++;
                
            } while (step > 1e-10 && iteration < 20);
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
