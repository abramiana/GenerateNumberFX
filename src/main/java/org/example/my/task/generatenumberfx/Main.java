package org.example.my.task.generatenumberfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
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
    private TextField numOfNumbersField;
    private RandomNumberGenerator generator;

    @Override
    public void start(Stage primaryStage) {
        generator = new LinearCongruentialGenerator(System.currentTimeMillis());
        RadioButton lcgRadioButton = new RadioButton("Лінійний конгруентний генератор");
        RadioButton lfgRadioButton = new RadioButton("Відстаючий генератор Фібоначчі");
        RadioButton ctgRadioButton = new RadioButton("Об'єднаний генератор Таузворта");
        RadioButton mtRadioButton = new RadioButton("Об'єднаний генератор Мерсенна Твістера");

        toggleGroup = new ToggleGroup();
        lcgRadioButton.setToggleGroup(toggleGroup);
        lfgRadioButton.setToggleGroup(toggleGroup);
        ctgRadioButton.setToggleGroup(toggleGroup);
        mtRadioButton.setToggleGroup(toggleGroup);

        Label numOfNumbersLabel = new Label("Кількість згенерованих чисел:");
        numOfNumbersField = new TextField();
        numOfNumbersField.setText("10");

        Button startButton = new Button("Почати");
        startButton.setOnAction(event -> startGeneration());

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Генератор випадкових чисел");

        meanLabel = new Label("Середнє:");
        stdDeviationLabel = new Label("Стандартне відхилення:");
        varianceLabel = new Label("Дисперсія:");

        HBox firstRowBox = new HBox(10, lcgRadioButton, lfgRadioButton);
        HBox secondRowBox = new HBox(10, ctgRadioButton, mtRadioButton);
        VBox radioButtonsBox = new VBox(10, firstRowBox, secondRowBox);

        HBox.setMargin(startButton, new Insets(0, 0, 0, 10));
        HBox.setMargin(meanLabel, new Insets(10, 10, 0, 0));
        HBox.setMargin(stdDeviationLabel, new Insets(10, 10, 0, 0));
        HBox.setMargin(varianceLabel, new Insets(10, 10, 0, 0));

        VBox root = new VBox(10, radioButtonsBox, numOfNumbersLabel, numOfNumbersField, startButton, barChart, meanLabel, stdDeviationLabel, varianceLabel);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Генератор випадкових чисел");
        primaryStage.show();
    }

    private void startGeneration() {
        int numOfNumbers = Integer.parseInt(numOfNumbersField.getText());
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            barChart.getData().clear();
            List<Float> sequence = generateData(generator, numOfNumbers);
            Task<Void> statisticalTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    StatisticalAnalysis(sequence);
                    return null;
                }
            };
            statisticalTask.setOnSucceeded(event -> Platform.runLater(() -> addDataToChart(sequence)));
            statisticalTask.setOnFailed(event -> Platform.runLater(() -> showErrorAlert()));
            Thread thread = new Thread(statisticalTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private List<Float> generateData(RandomNumberGenerator generator, int numOfNumbers) {
        List<Float> sequence = new ArrayList<>();
        for (int i = 0; i < numOfNumbers; i++) {
            float number = generator.generate();
            sequence.add(number);
        }
        return sequence;
    }

    private void StatisticalAnalysis(List<Float> sequence) {
        double sum = 0;
        double sumSquare = 0;
        for (float number : sequence) {
            sum += number;
            sumSquare += number * number;
        }

        double mean = sum / sequence.size();
        double variance = (sumSquare / sequence.size()) - (mean * mean);
        double stdDeviation = Math.sqrt(variance);

        Platform.runLater(() -> {
            meanLabel.setText("Середнє: " + mean);
            stdDeviationLabel.setText("Стандартне відхилення: " + stdDeviation);
            varianceLabel.setText("Дисперсія: " + variance);
        });
    }

    private void addDataToChart(List<Float> sequence) {
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

        BarChart.Series<String, Number> series = new BarChart.Series<>();
        for (int i = 0; i < sequence.size(); i++) {
            String label = "Число " + (i + 1) + "\n" + sequence.get(i);
            BarChart.Data<String, Number> dataPoint = new BarChart.Data<>(label, sequence.get(i));
            series.getData().add(dataPoint);
        }
        barChart.getData().add(series);
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText("Помилка при обчисленні статистичних параметрів.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
