package org.example.my.task.generatenumberfx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Клас, що реалізує Combined Tausworthe генератор випадкових чисел
public class CombinedTauswortheGenerator implements RandomNumberGenerator {
    private final List<Long> state; // Стан генератора
    private final int[] shifts; // Зсуви для кожного Tausworthe-генератора
    private final ExecutorService executorService; // Пул потоків для багатопоточної генерації

    // Конструктор, що приймає початкове значення (насіння)
    public CombinedTauswortheGenerator(long seed) {
        state = new ArrayList<>();
        // Ініціалізація стану генератора за допомогою насіння
        for (int i = 0; i < 160; i++) {
            state.add((seed & (1L << i)) != 0 ? 1L : 0L);
        }
        // Ініціалізація зсувів для кожного Tausworthe-генератора
        this.shifts = new int[]{31, 62, 124};
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // Метод для генерації випадкового числа
    @Override
    public float generate() {
        long result = 0;
        List<Future<Long>> futures = new ArrayList<>();

        // Створення задач для генерації чисел у кожному потоці
        for (int i = 0; i < shifts.length; i++) {
            int finalI = i;
            futures.add(executorService.submit(() -> state.get(finalI) << shifts[finalI]));
        }

        // Отримання результатів з потоків та виконання операції XOR
        try {
            for (Future<Long> future : futures) {
                result ^= future.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateState(); // Оновлення стану

        // Ігноруємо знаковий біт та встановлюємо його в 0 для забезпечення додатності числа
        return result & Long.MAX_VALUE;
    }

    // Приватний метод для оновлення стану генератора
    private void updateState() {
        // Обчислення нового біту
        long newBit = (state.get(0) ^ state.get(63) ^ state.get(125)) & 1;
        state.remove(state.size() - 1); // Видалення останнього біту
        state.add(0, newBit); // Додавання нового біту в початок
    }
}
