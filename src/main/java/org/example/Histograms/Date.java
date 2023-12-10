package org.example.Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.util.Map;

public class Date extends Histogram {
    public Date(String title, Map<Integer, Integer> data) {
        super(title);

        // Создаем датасет с данными
        IntervalXYDataset dataset = createDataset(data);

        // Создаем гистограмму
        JFreeChart chart = createChart(dataset, title);

        // Создаем панель для графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        setContentPane(chartPanel);
    }

    private IntervalXYDataset createDataset(Map<Integer, Integer> data) {
        XYSeries series = new XYSeries("Issue");
        for (Map.Entry<Integer, Integer> entry: data.entrySet()) {
            series.add(entry.getKey(), entry.getValue());
        }

        return new XYSeriesCollection(series);
    }

    private JFreeChart createChart(IntervalXYDataset dataset, String title) {
        return ChartFactory.createXYBarChart(
                title,
                "Time in hours",
                false,
                "Issues",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
    }
}
