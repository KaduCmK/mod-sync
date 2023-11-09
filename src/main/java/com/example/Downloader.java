package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Downloader {
    
    private GUI gui;
    private JSONObject mods;

    private JSONArray modrinthMods;
    private JSONObject curseforgeMods;

    public Downloader(GUI gui) throws IOException {

        this.gui = gui;

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
    
    public void download() {
        
        try {
            // baixando do modrinth
            for (Object mod: modrinthMods) {
                String url = (String) mod;
                String name = FilenameUtils.getName(url);
                gui.println("Modrinth: Baixando " + name + "...");
                File modfile = new File(name);
                if (modfile.isFile()) {
                    gui.println(name + " já existe");
                }
                else {
                    FileUtils.copyURLToFile(new URL(url), modfile);
                }
            }

            // baixando do curseforge
            Iterator<String> cfModNames = curseforgeMods.keys();
            while(cfModNames.hasNext()) {
                String name = cfModNames.next();
                String url = curseforgeMods.getString(name);
                gui.println("CurseForge: baixando " + name + "...");
                File modfile = new File(name);
                if (modfile.isFile()) {
                    gui.println(name + " já existe");
                }
                else {
                    FileUtils.copyURLToFile(new URL(url), modfile);
                }
            }
        }
        catch (Exception e) {
            gui.println(e.toString());
        }
    }
}
