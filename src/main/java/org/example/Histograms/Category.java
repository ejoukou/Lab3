package org.example.Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.util.Map;

public class Category extends Histogram {

    public Category(String title, Map<String, Integer> data) {
        super(title);

        // Создаем датасет с данными
        DefaultCategoryDataset dataset = createDataset(data);

        // Создаем гистограмму
        JFreeChart chart = createChart(dataset, title);

        // Создаем панель для графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(Map<String, Integer> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry: data.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), entry.getKey());
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
