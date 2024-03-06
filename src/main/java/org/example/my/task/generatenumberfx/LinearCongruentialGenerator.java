package org.example.my.task.generatenumberfx;

import java.util.concurrent.atomic.AtomicLong;

public class LinearCongruentialGenerator implements RandomNumberGenerator {
    private AtomicLong seed; // Початкове значення (насіння)
    private final long a = 1664525; // Множник
    private final long c = 1013904223; // Приріст
    private final long m = (long) Math.pow(2, 32); // Модуль (2^32)

    /**
     * Ініціалізує генератор з заданим початковим значенням (насінням).
     *
     * @param seed Початкове значення (насіння)
     */
    public LinearCongruentialGenerator(long seed) {
        this.seed = new AtomicLong(seed);
    }

    /**
     * Генерує наступне випадкове додатне число в послідовності.
     *
     * @return наступне випадкове додатне число
     */
    @Override
    public float generate() {
        long currentSeed = seed.getAndUpdate(prev -> (a * prev + c) % m);
        return Math.abs(currentSeed - m / 2); // Повертає випадкове додатне число від 0 до m/2
    }
}
