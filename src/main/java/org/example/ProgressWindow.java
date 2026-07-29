package org.example;

import javax.swing.*;
import java.awt.*;

public final class ProgressWindow {
    private static JFrame frame;
    private static JLabel statusLabel;
    private static JLabel fileLabel;
    private static JProgressBar progressBar;

    private ProgressWindow() {
    }

    public static void show() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("PurMur Updater");

            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(420, 140);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            statusLabel = new JLabel("Проверка обновлений...");
            fileLabel = new JLabel(" ");

            progressBar = new JProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setStringPainted(true);

            panel.add(statusLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(progressBar);
            panel.add(Box.createVerticalStrut(10));
            panel.add(fileLabel);

            frame.setContentPane(panel);
            frame.setVisible(true);
        });
    }

    public static void setStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    public static void setFile(String file) {
        SwingUtilities.invokeLater(() -> fileLabel.setText(file));
    }

    public static void setProgress(int current, int total) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setMaximum(total);
            progressBar.setValue(current);
            progressBar.setString(current + " / " + total);
        });
    }

    public static void close() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose();
            }
        });
    }
}