package org.example.Histograms;

import javax.swing.*;
import java.awt.*;

public abstract class Histogram extends JFrame {

    public Histogram(String title) {
        super(title);
    }

    public void draw() {
        SwingUtilities.invokeLater(() -> {
            super.setSize(800, 600);
            super.setLocationRelativeTo(null);
            super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            super.setVisible(true);
            super.setBackground(Color.black);
        });
    }
}