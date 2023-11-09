package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) throws Exception {

        GUI gui = new GUI();
        Downloader downloader = new Downloader(gui, gui.path);

        gui.downloadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                downloader.execute();
            }
            
        });

        gui.saveButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.pathChooser();
            }
        });
    }
}