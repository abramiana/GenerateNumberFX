package org.example.my.task.generatenumberfx;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляє генератор випадкових чисел за допомогою алгоритму Lagged Fibonacci.
 */
public class LaggedFibonacciGenerator implements RandomNumberGenerator {
    private final List<Long> sequence;
    private int currentIndex;
    private final int lag1 = 3; // Параметр відставання 1
    private final int lag2 = 2; // Параметр відставання 2
    private final int mod = 100; // Модуль

    /**
     * Конструює генератор Lagged Fibonacci з використанням вказаного початкового значення.
     *
     * @param seed Початкове значення (насіння) для генератора.
     */
    public LaggedFibonacciGenerator(long seed) {
        sequence = new ArrayList<>();
        sequence.add(seed);
        for (int i = 1; i < Math.max(lag1, lag2); i++) {
            long next = (sequence.get(i - 1) + sequence.get((i - 1 + mod - 1) % (mod - 1))) % mod;
            sequence.add(next);
        }
        currentIndex = Math.max(lag1, lag2) - 1;
    }

    /**
     * Генерує наступне випадкове число в послідовності.
     *
     * @return наступне випадкове число
     */
    @Override
    public float generate() {
        currentIndex++;
        long next;
        if (currentIndex >= sequence.size()) {
            next = (sequence.get(currentIndex - lag1) - sequence.get(currentIndex - lag2)) % mod;
            sequence.add(next);
        } else {
            next = (sequence.get(currentIndex - lag1) - sequence.get(currentIndex - lag2)) % mod;
            sequence.set(currentIndex, next);
        }
        // Забезпечуємо, щоб результат генерації завжди був додатнім
        return Math.abs(next);
    }
}