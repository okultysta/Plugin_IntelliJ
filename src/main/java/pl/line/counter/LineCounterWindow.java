package pl.line.counter;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class LineCounterWindow {

    private final Project project;
    private final JPanel mainPanel;
    private final LineCounter counterService;
    private final ChartPanel chartPanel;
    private String currentFilter = "total";
    private List<Stats> currentStats;

    public LineCounterWindow(Project project) {
        this.project = project;
        this.counterService = new LineCounter();
        this.mainPanel = new JBPanel<>(new BorderLayout());
        this.chartPanel = new ChartPanel();

        JLabel title = new JLabel("Statystyki kodu");
        title.setBorder(JBUI.Borders.empty(10));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        chartPanel.setBorder(JBUI.Borders.empty(10));
        mainPanel.add(new JBScrollPane(chartPanel), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Odśwież");
        refreshButton.addActionListener(e -> refreshStats());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    public JComponent getContent() {
        return mainPanel;
    }


    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(JBUI.Borders.empty(5));

        JLabel filterLabel = new JLabel("Pokaż: ");
        filterLabel.setFont(filterLabel.getFont().deriveFont(Font.BOLD));
        filterPanel.add(filterLabel);

        ButtonGroup group = new ButtonGroup();


        JRadioButton totalButton = new JRadioButton("Wszystkie linie", true);
        totalButton.addActionListener(e -> {
            currentFilter = "total";
            updateChart();
        });
        group.add(totalButton);
        filterPanel.add(totalButton);


        JRadioButton codeButton = new JRadioButton("Kod", false);
        codeButton.addActionListener(e -> {
            currentFilter = "code";
            updateChart();
        });
        group.add(codeButton);
        filterPanel.add(codeButton);


        JRadioButton commentButton = new JRadioButton("Komentarze", false);
        commentButton.addActionListener(e -> {
            currentFilter = "comments";
            updateChart();
        });
        group.add(commentButton);
        filterPanel.add(commentButton);


        JRadioButton blankButton = new JRadioButton("Puste linie", false);
        blankButton.addActionListener(e -> {
            currentFilter = "blanks";
            updateChart();
        });
        group.add(blankButton);
        filterPanel.add(blankButton);

        return filterPanel;
    }


    private void updateChart() {
        System.out.println("Aktualizacja wykresu z filtrem: " + currentFilter);

        if (currentStats != null) {
            System.out.println("Liczba statystyk: " + currentStats.size());

            chartPanel.setStats(currentStats, currentFilter);
        } else {
            System.out.println("currentStats = null!");
        }
    }



    private void refreshStats() {
        System.out.println("=== Rozpoczynam liczenie ===");
        currentStats = counterService.countLinesInProject(project);

        System.out.println("Znaleziono plików: " + (currentStats != null ? currentStats.size() : "null"));

        if (currentStats != null && !currentStats.isEmpty()) {
            System.out.println("Pierwszy plik: " + currentStats.get(0).getFileName());
        }

        updateChart();

        System.out.println("=== Aktualizacja wysłana ===");
    }



    private class ChartPanel extends JPanel {
        private List<Stats> stats;
        private String filter = "total";

        public void setStats(List<Stats> stats, String filter) {
            this.stats = stats;
            this.filter = filter;
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            System.out.println("Rozpoczynam malowanie komponentu. Rozmiar: " + getWidth() + "x" + getHeight());

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (stats == null || stats.isEmpty()) {
                g2.setColor(UIUtil.getLabelForeground());
                g2.drawString("Brak danych. Kliknij Odśwież.", 20, 50);
                return;
            }

            Map<String, Integer> totals = new HashMap<>();
            for (Stats stat : stats) {
                String type = stat.getFileType();
                int value = getValueByFilter(stat, filter);
                totals.put(type, totals.getOrDefault(type, 0) + value);
            }


            drawChart(g2, totals);
            System.out.println("Koniec malowania komponentu.");
        }


        private int getValueByFilter(Stats stat, String filter) {
            switch (filter) {
                case "code": return stat.getCodeLines();
                case "comments": return stat.getCommentLines();
                case "blanks": return stat.getBlankLines();
                default: return stat.getTotalLines();
            }
        }


        private void drawChart(Graphics2D g2, Map<String, Integer> totals) {
            System.out.println("Rozpoczynam rysowanie nowego wykresu.");

            int x = 50;
            int y = 50;
            int barWidth = 40;
            int maxHeight = 200;
            int spacing = 20;

            int maxLines = totals.values().stream()
                    .max(Integer::compareTo)
                    .orElse(1);


            g2.setColor(UIUtil.getLabelForeground());
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            for (Map.Entry<String, Integer> entry : totals.entrySet()) {
                int height = (int) ((double) entry.getValue() / maxLines * maxHeight);


                g2.setColor(getBarColor(filter));
                g2.fillRect(x, y + maxHeight - height, barWidth, height);


                g2.setColor(UIUtil.getLabelForeground());
                g2.drawRect(x, y + maxHeight - height, barWidth, height);


                g2.setColor(UIUtil.getLabelForeground());
                g2.drawString(entry.getKey(), x + 5, y + maxHeight + 20);


                String valueStr = String.valueOf(entry.getValue());
                int stringWidth = g2.getFontMetrics().stringWidth(valueStr);
                g2.drawString(valueStr,
                        x + (barWidth - stringWidth) / 2,
                        y + maxHeight - height - 5);

                x += barWidth + spacing;
            }


            g2.setColor(UIUtil.getLabelForeground());
            g2.drawLine(30, y, 30, y + maxHeight + 10); // Oś Y
            g2.drawLine(30, y + maxHeight, x + 50, y + maxHeight); // Oś X

            System.out.println("Koniec rysowanie wykresu.");
        }


        private Color getBarColor(String filter) {
            switch (filter) {
                case "code":
                    return new Color(66, 133, 244); // niebieski
                case "comments":
                    return new Color(52, 168, 83);  // zielony
                case "blanks":
                    return new Color(251, 188, 5);  // żółty
                case "total":
                    return new Color(234, 67, 53);  // czerwony
                default:
                    return UIUtil.getLabelForeground();
            }
        }
    }
}
