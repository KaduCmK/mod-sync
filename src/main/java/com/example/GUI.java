package com.example;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI {

    private JTextArea console;
    public JButton downloadButton;


    public GUI() {
        JFrame frame = new JFrame("mod-manager");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setSize(500, 800);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.console = new JTextArea();
        console.setEditable(false);
        JScrollPane scroll = new JScrollPane(console);

        this.downloadButton = new JButton("Baixar");

        panel.add(scroll);
        panel.add(downloadButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void println(String text) {
        console.append(text + "\n");
    }
}
