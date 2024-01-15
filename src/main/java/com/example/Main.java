package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) throws Exception {

    
        if (args.length > 0 && args[0].equals("nogui")) {
            Downloader downloader = new Downloader();
            downloader.noguiDownload();
        }
        else {
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
                    gui.pathChooser(downloader);
                }
            });

            gui.manifestButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    downloader.manualManifestDownload();
                }
                
            });
        }
    }
}