package org.example.my.task.generatenumberfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private BarChart<String, Number> barChart;
    private ToggleGroup toggleGroup;
    private Label meanLabel;
    private Label stdDeviationLabel;
    private Label varianceLabel;
    private TextField numOfNumbersField; // Поле для введення кількості згенерованих чисел
    private RandomNumberGenerator generator; // Зберігає екземпляр генератора

    @Override
    public void start(Stage primaryStage) {
        // Ініціалізуємо генератори
        generator = new LinearCongruentialGenerator(System.currentTimeMillis());
        RadioButton lcgRadioButton = new RadioButton("Лінійний конгруентний генератор");
        RadioButton lfgRadioButton = new RadioButton("Відстаючий генератор Фібоначчі");
        RadioButton ctgRadioButton = new RadioButton("Об'єднаний генератор Таузворта");
        RadioButton mtRadioButton = new RadioButton("Об'єднаний генератор Мерсенна Твістера");

        // Група перемикачів для радіокнопок
        toggleGroup = new ToggleGroup();
        lcgRadioButton.setToggleGroup(toggleGroup);
        lfgRadioButton.setToggleGroup(toggleGroup);
        ctgRadioButton.setToggleGroup(toggleGroup);
        mtRadioButton.setToggleGroup(toggleGroup);

        // Поле для введення кількості згенерованих чисел
        Label numOfNumbersLabel = new Label("Кількість згенерованих чисел:");
        numOfNumbersField = new TextField();
        numOfNumbersField.setText("10"); // Значення за замовчуванням

        // Кнопка для початку генерації
        Button startButton = new Button("Почати");
        startButton.setOnAction(event -> startGeneration());

        // Створення графіка
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Генератор випадкових чисел");

        // Мітки для відображення статистичних даних
        meanLabel = new Label("Середнє:");
        stdDeviationLabel = new Label("Стандартне відхилення:");
        varianceLabel = new Label("Дисперсія:");

        // Радіокнопки, поле, кнопка та мітки
        // Перший рядок з двома радіокнопками
        HBox firstRowBox = new HBox(10, lcgRadioButton, lfgRadioButton);
        // Розташування другого рядка з двома радіокнопками
        HBox secondRowBox = new HBox(10, ctgRadioButton, mtRadioButton);
        // Всі радіокнопки розташовані у два рядки
        VBox radioButtonsBox = new VBox(10, firstRowBox, secondRowBox);

        // Відступи для кнопки та міток
        HBox.setMargin(startButton, new Insets(0, 0, 0, 10));
        HBox.setMargin(meanLabel, new Insets(10, 10, 0, 0));
        HBox.setMargin(stdDeviationLabel, new Insets(10, 10, 0, 0));
        HBox.setMargin(varianceLabel, new Insets(10, 10, 0, 0));

        // Кореневий вузол
        VBox root = new VBox(10, radioButtonsBox, numOfNumbersLabel, numOfNumbersField, startButton, barChart, meanLabel, stdDeviationLabel, varianceLabel);
        root.setPadding(new Insets(10));

        // Створення сцени
        Scene scene = new Scene(root, 800, 600);

        // Встановлення сцени та відображення вікна
        primaryStage.setScene(scene);
        primaryStage.setTitle("Генератор випадкових чисел");
        primaryStage.show();
    }

    private void startGeneration() {
        int numOfNumbers = Integer.parseInt(numOfNumbersField.getText());
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String generatorName = selectedRadioButton.getText();
            barChart.getData().clear(); // Очищення даних графіка
            List<Long> sequence = generateData(generator, numOfNumbers);
            StatisticalAnalysis(sequence);
            addDataToChart(sequence); // Додавання нових даних до графіка
        }
    }

    private List<Long> generateData(RandomNumberGenerator generator, int numOfNumbers) {
        List<Long> sequence = new ArrayList<>();
        for (int i = 0; i < numOfNumbers; i++) { // Генерація заданої користувачем кількості чисел
            long number = generator.generate();
            sequence.add(number);
        }
        return sequence;
    }

    private void StatisticalAnalysis(List<Long> sequence) {
        double mean = StatisticalAnalysis.calculateMean(sequence);
        double stdDeviation = StatisticalAnalysis.calculateStandardDeviation(sequence);
        double variance = StatisticalAnalysis.calculateVariance(sequence);

        meanLabel.setText("Середнє: " + mean);
        stdDeviationLabel.setText("Стандартне відхилення: " + stdDeviation);
        varianceLabel.setText("Дисперсія: " + variance);
    }

    private void addDataToChart(List<Long> sequence) {
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

        BarChart.Series<String, Number> series = new BarChart.Series<>();
        for (int i = 0; i < sequence.size(); i++) {
            String label = "Число " + (i + 1) + "\n" + sequence.get(i); // Комбінований текст для мітки
            BarChart.Data<String, Number> dataPoint = new BarChart.Data<>(label, sequence.get(i));
            series.getData().add(dataPoint);
        }
        barChart.getData().add(series);
    }

    public static void main(String[] args) {
        launch(args);
    }
}