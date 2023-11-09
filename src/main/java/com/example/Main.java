package com.example;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws Exception {

        // manifesto
        System.out.println("Baixando manifesto...");
        File f = new File("mods.json");
        FileUtils.copyURLToFile(new URL("https://pastebin.com/raw/hkf4UbdT"), f);
        JSONObject mods = new JSONObject(FileUtils.readFileToString(f, StandardCharsets.UTF_8));

        JSONArray modrinthMods = mods.getJSONArray("modrinth");
        JSONObject curseforgeMods = mods.getJSONObject("curseforge");
        
        try {
            // baixando do modrinth
            for (Object mod: modrinthMods) {
                String url = (String) mod;
                String name = FilenameUtils.getName(url);
                System.out.println("Modrinth: Baixando " + name + "...");
                File modfile = new File(name);
                if (modfile.isFile()) {
                    System.out.println(name + " já existe");
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
                System.out.println("CurseForge: baixando " + name + "...");
                File modfile = new File(name);
                if (modfile.isFile()) {
                    System.out.println(name + " já existe");
                }
                else {
                    FileUtils.copyURLToFile(new URL(url), modfile);
                }
            }

            f.delete();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}