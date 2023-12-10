package org.example.Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.util.Map;

public class CreatedClosed extends Histogram {

    public CreatedClosed(String title, Map<String, Integer> dataCreated,
                         Map<String, Integer> dataClosed, Map<String, Integer> dataAccumulated) {
        super(title);

        // Создаем датасет с данными
        DefaultCategoryDataset dataset = createDataset(dataCreated, dataClosed, dataAccumulated);

        // Создаем гистограмму
        JFreeChart chart = createChart(dataset, title);

        // Создаем панель для графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(Map<String, Integer> dataCreated, Map<String, Integer> dataClosed,
                                                 Map<String, Integer> dataAccumulated) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry: dataCreated.entrySet()) {
            dataset.addValue(entry.getValue(), "Created", entry.getKey());
        }
        for (Map.Entry<String, Integer> entry: dataClosed.entrySet()) {
            dataset.addValue(entry.getValue(), "Closed", entry.getKey());
        }

        for (Map.Entry<String, Integer> entry: dataAccumulated.entrySet()) {
            dataset.addValue(entry.getValue(), "Accumulated", entry.getKey());
        }

        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset, String title) {
        return ChartFactory.createBarChart(
                title,
                "Users",
                "Issue",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );
    }
}