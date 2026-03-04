package pl.line.counter;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class LineCounterWindow {

    private final Project project;
    private final JPanel mainPanel;

    public LineCounterWindow(Project project) {
        this.project = project;
        this.mainPanel = new JBPanel<>(new BorderLayout());

        // Tytuł
        JLabel title = new JLabel("Statystyki kodu");
        title.setBorder(JBUI.Borders.empty(10));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        mainPanel.add(title, BorderLayout.NORTH);

        // Miejsce na wykres - na razie puste
        JLabel placeholder = new JLabel("Wykres pojawi się tutaj", SwingConstants.CENTER);
        placeholder.setBorder(JBUI.Borders.empty(50));
        mainPanel.add(placeholder, BorderLayout.CENTER);

        // Przycisk do odświeżania
        JButton refreshButton = new JButton("Odśwież");
        refreshButton.addActionListener(e -> refreshStats());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JComponent getContent() {
        return mainPanel;
    }

    private void refreshStats() {
        // Tutaj później dodamy logikę liczenia
        JOptionPane.showMessageDialog(mainPanel, "Liczenie linii...");
    }
}
