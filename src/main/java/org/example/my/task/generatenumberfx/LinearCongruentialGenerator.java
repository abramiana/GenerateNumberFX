package org.example.my.task.generatenumberfx;

/**
 * Представляє генератор випадкових чисел за допомогою лінійного конгруентного методу.
 */
public class LinearCongruentialGenerator implements RandomNumberGenerator {
    private long seed; // Початкове значення (насіння)
    private final long a = 1664525; // Множник
    private final long c = 1013904223; // Приріст
    private final long m = (long) Math.pow(2, 32); // Модуль (2^32)

    /**
     * Ініціалізує генератор з заданим початковим значенням (насінням).
     *
     * @param seed Початкове значення (насіння)
     */
    public LinearCongruentialGenerator(long seed) {
        this.seed = seed;
    }

    /**
     * Генерує наступне випадкове число в послідовності.
     *
     * @return наступне випадкове число
     */
    @Override
    public long generate() {
        seed = (a * seed + c) % m;
        return seed - m / 2; // Повертає випадкове число від -m/2 до m/2
    }
}