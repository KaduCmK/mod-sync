package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Downloader extends SwingWorker<Integer, String> {

    private int sleep = 0;
    
    private GUI gui;
    private JSONObject mods;
    private String path;

    private JSONArray modrinthMods;
    private JSONObject curseforgeMods;

    public Downloader() throws IOException {
        System.out.println("Baixando manifesto...");
        File modList = new File("mods.json");
        FileUtils.copyURLToFile(new URL("https://pastebin.com/raw/hkf4UbdT"), modList);
        mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

        modrinthMods = mods.getJSONArray("modrinth");
        curseforgeMods = mods.getJSONObject("curseforge");
        int modsQtd = modrinthMods.length() + curseforgeMods.length();
        System.out.println("Manifesto atualizado. " + modsQtd + " mods no total.");

        modList.delete();
    }
    
    public Downloader(GUI gui, String path) throws IOException {

        this.gui = gui;
        this.path = path;

        // manifesto
        gui.println("Baixando manifesto...");
        File modList = new File("mods.json");
        FileUtils.copyURLToFile(new URL("https://pastebin.com/raw/hkf4UbdT"), modList);
        mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

        modrinthMods = mods.getJSONArray("modrinth");
        curseforgeMods = mods.getJSONObject("curseforge");
        int modsQtd = modrinthMods.length() + curseforgeMods.length();
        gui.println("Manifesto atualizado. " + modsQtd + " mods no total.");

        modList.delete();
    }

    public Downloader(GUI gui, String path, int sleep) throws IOException {
        this(gui, path);
        this.sleep = sleep;
    }
    
    @Override
    protected Integer doInBackground() {
        
        try {
            // baixando do modrinth
            for (Object mod: modrinthMods) {
                String url = (String) mod;
                String name = FilenameUtils.getName(url);
                publish("Modrinth: Baixando " + name + "...");
                if (sleep != 0) {
                 Thread.sleep(sleep);   
                }
                else {
                    File modfile = new File(path + "\\" + name);
                    if (modfile.isFile()) {
                        gui.println(name + " já existe");
                    }
                    else {
                        FileUtils.copyURLToFile(new URL(url), modfile);
                    }
                }
            }

            // baixando do curseforge
            Iterator<String> cfModNames = curseforgeMods.keys();
            while(cfModNames.hasNext()) {
                String name = cfModNames.next();
                String url = curseforgeMods.getString(name);
                publish("CurseForge: baixando " + name + "...");
                if (sleep != 0) {
                 Thread.sleep(sleep);   
                }
                else {
                    File modfile = new File(path + "\\" + name);
                    if (modfile.isFile()) {
                        gui.println(name + " já existe");
                    }
                    else {
                        FileUtils.copyURLToFile(new URL(url), modfile);
                    }
                }
            }
        }
        catch (Exception e) {
            gui.println(e.toString());
        }

        return 42;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String chunk: chunks) {
            gui.println(chunk);
        }
    }

    @Override
    protected void done() {
        gui.println("Download completo.");
    }


    public void noguiDownload() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Pressione qualquer tecla para iniciar o download...");
        sc.nextLine();

        try {
            // baixando do modrinth
            for (Object mod: modrinthMods) {
                String url = (String) mod;
                String name = FilenameUtils.getName(url);
                System.out.println("Modrinth: Baixando " + name + "...");
                if (sleep != 0) {
                 Thread.sleep(sleep);   
                }
                else {
                    File modfile = new File(name);
                    if (modfile.isFile()) {
                        System.out.println(name + " já existe");
                    }
                    else {
                        FileUtils.copyURLToFile(new URL(url), modfile);
                    }
                }
            }

            // baixando do curseforge
            Iterator<String> cfModNames = curseforgeMods.keys();
            while(cfModNames.hasNext()) {
                String name = cfModNames.next();
                String url = curseforgeMods.getString(name);
                System.out.println("CurseForge: baixando " + name + "...");
                if (sleep != 0) {
                 Thread.sleep(sleep);   
                }
                else {
                    File modfile = new File(name);
                    if (modfile.isFile()) {
                        System.out.println(name + " já existe");
                    }
                    else {
                        FileUtils.copyURLToFile(new URL(url), modfile);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            sc.close();;
        }
    }
    
}
