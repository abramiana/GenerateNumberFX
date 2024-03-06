package org.example.my.task.generatenumberfx;

import java.util.ArrayList;
import java.util.List;

// Клас, що реалізує Combined Tausworthe генератор випадкових чисел
public class CombinedTauswortheGenerator implements RandomNumberGenerator {
    private final List<Long> state; // Стан генератора
    private final int[] shifts; // Зсуви для кожного Tausworthe-генератора

    // Конструктор, що приймає початкове значення (насіння)
    public CombinedTauswortheGenerator(long seed) {
        state = new ArrayList<>();
        // Ініціалізація стану генератора за допомогою насіння
        for (int i = 0; i < 160; i++) {
            state.add((seed & (1L << i)) != 0 ? 1L : 0L);
        }
        // Ініціалізація зсувів для кожного Tausworthe-генератора
        this.shifts = new int[]{31, 62, 124};
    }

    // Метод для генерації випадкового числа
    @Override
    public float generate() {
        long result = 0;
        // Генерація числа шляхом здійснення операції XOR для кожного Tausworthe-генератора
        for (int i = 0; i < shifts.length; i++) {
            result ^= (state.get(i) << shifts[i]);
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