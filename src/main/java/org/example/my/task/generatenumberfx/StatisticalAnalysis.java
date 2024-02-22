package org.example.my.task.generatenumberfx;

import java.util.List;

public class StatisticalAnalysis {
    // Метод для обчислення середнього значення послідовності
    public static double calculateMean(List<Long> sequence) {
        double sum = 0;
        for (Long num : sequence) {
            sum += num;
        }
        return sum / sequence.size();
    }

    // Метод для обчислення середньоквадратичного відхилення послідовності
    public static double calculateStandardDeviation(List<Long> sequence) {
        double mean = calculateMean(sequence);
        double sumSquaredDiff = 0;
        for (Long num : sequence) {
            sumSquaredDiff += Math.pow(num - mean, 2);
        }
        return Math.sqrt(sumSquaredDiff / sequence.size());
    }

    // Метод для обчислення дисперсії послідовності
    public static double calculateVariance(List<Long> sequence) {
        double mean = calculateMean(sequence);
        double sumSquaredDiff = 0;
        for (Long num : sequence) {
            sumSquaredDiff += Math.pow(num - mean, 2);
        }
        return sumSquaredDiff / sequence.size();
    }
}