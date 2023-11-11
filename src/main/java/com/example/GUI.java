package com.example;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class GUI {

    JFrame frame;

    private JTextArea console;
    public JButton downloadButton;
    public JButton saveButton;
    public JButton manifestButton;
    public String path = System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\mods";
    JLabel pathLabel;

    JFileChooser fileChooser;


    public GUI() {
        frame = new JFrame("Mod Manager");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setSize(500, 800);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.console = new JTextArea();
        console.setEditable(false);
        JScrollPane scroll = new JScrollPane(console);
        DefaultCaret caret = (DefaultCaret)console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.downloadButton = new JButton("Baixar Mods");
        downloadButton.setEnabled(false);

        this.saveButton = new JButton("Alterar pasta de mods");

        this.manifestButton = new JButton("Baixar Manifesto");

        pathLabel = new JLabel("Caminho da pasta de mods:  " + path);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(path));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        panel.add(scroll);
        panel.add(pathLabel);
        panel.add(saveButton);
        panel.add(downloadButton);
        panel.add(manifestButton);

        frame.add(panel);

        frame.setVisible(true);
    }

    public void println(String text) {
        console.append(text + "\n");
    }

    public void pathChooser() {
        
        if (fileChooser.showOpenDialog(console) == JFileChooser.APPROVE_OPTION) {
            this.path = fileChooser.getSelectedFile().getAbsolutePath();
            this.pathLabel.setText("Caminho da pasta de mods: " + path);
        };
        
    }
}
