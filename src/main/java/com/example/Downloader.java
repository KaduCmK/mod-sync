package com.example;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private JSONArray remover;
    private JSONArray modrinthMods;
    private JSONObject curseforgeMods;

    public Downloader() throws Exception {
        try {
            System.out.println("Baixando manifesto...");
            File modList = new File("mods.json");
            FileUtils.copyURLToFile(new URL("https://pastebin.com/raw/hkf4UbdT"), modList);
            mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

            remover = mods.getJSONArray("remove");
            modrinthMods = mods.getJSONArray("modrinth");
            curseforgeMods = mods.getJSONObject("curseforge");
            int modsQtd = modrinthMods.length() + curseforgeMods.length();
            System.out.println(modsQtd + " mods no total, pronto para baixar.");

            modList.delete();
        }
        catch (Exception ex) {
            File modList = new File("manifest.json");
            if (!modList.isFile()) {
                System.out.println("Erro ao baixar o manifesto.\nPressione qualquer tecla para abrir pelo seu navegador e clique em Download\ne rode o programa novamente");
                System.out.println("ATENÇÃO: O manifesto precisa estar no mesmo local que este programa!");
                Scanner sc = new Scanner(System.in);
                sc.nextLine();
                this.manualManifestDownload();
                sc.close();
            }
            else {
                System.out.println(path);
                mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

                modrinthMods = mods.getJSONArray("modrinth");
                curseforgeMods = mods.getJSONObject("curseforge");
                int modsQtd = modrinthMods.length() + curseforgeMods.length();
                System.out.println(modsQtd + " mods no total, pronto para baixar.");

                modList.delete();
            }
        }
    }
    
    public Downloader(GUI gui, String path) throws IOException, URISyntaxException, InterruptedException {

        this.gui = gui;
        this.path = path;

        try {
            // manifesto
            gui.println("Baixando manifesto...");
            File modList = new File("manifest.json");
            FileUtils.copyURLToFile(new URL("https://pastebin.com/raw/hkf4UbdT"), modList);
            mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

            remover = mods.getJSONArray("remove");
            modrinthMods = mods.getJSONArray("modrinth");
            curseforgeMods = mods.getJSONObject("curseforge");
            int modsQtd = modrinthMods.length() + curseforgeMods.length();
            gui.println(modsQtd + " mods no total, pronto para baixar.");

            gui.downloadButton.setEnabled(true);
            modList.delete();
        }
        catch (Exception ex) {
            File modList = new File("manifest.json");
            if (!modList.isFile()) {
                gui.println("Erro ao baixar o manifesto.\nUse o botão abaixo para acessar manualmente e clique em Download, e rode o programa novamente");
                gui.println("ATENÇÃO: O manifesto precisa estar no mesmo local que este programa!");
            }
            else {
                gui.println(path);
                mods = new JSONObject(FileUtils.readFileToString(modList, StandardCharsets.UTF_8));

                modrinthMods = mods.getJSONArray("modrinth");
                curseforgeMods = mods.getJSONObject("curseforge");
                int modsQtd = modrinthMods.length() + curseforgeMods.length();
                gui.println(modsQtd + " mods no total, pronto para baixar.");

                gui.downloadButton.setEnabled(true);
                modList.delete();
            }
        }
    }

    public Downloader(GUI gui, String path, int sleep) throws IOException, URISyntaxException, InterruptedException {
        this(gui, path);
        this.sleep = sleep;
    }
    
    @Override
    protected Integer doInBackground() {
        
        try {
            // limpando mods
            for (Object mod: remover) {
                String modname = (String) mod;
                File modfile = new File(path + File.separator + modname);
                if (modfile.exists()) {
                    if (modfile.delete()) {
                        publish("Removendo " + modname + "...");
                    }
                    else {
                        publish("ERRO - certifique-se de que o jogo está fechado antes de continuar");
                        return 42;
                    }
                }

            }

            // baixando do modrinth
            for (Object mod: modrinthMods) {
                String url = (String) mod;
                String name = FilenameUtils.getName(url);
                publish("Modrinth: Baixando " + name + "...");
                if (sleep != 0) {
                 Thread.sleep(sleep);   
                }
                else {
                    File modfile = new File(path + File.separator + name);
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
                    File modfile = new File(path + File.separator + name);
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

    public void manualManifestDownload() {
        try {
            Desktop.getDesktop().browse(new URI("https://pastebin.com/hkf4UbdT"));
        }
        catch (Exception e1) {
            gui.println("erro");
            e1.printStackTrace();
        }
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
