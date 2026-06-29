package com.servilious.projmtn.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Font { //I Absolutely hate fonts but they're also very important for games
    private int fontSize = 0;
    private String type = null;
    private int version = 0;
    private int x = 0, y = 0;
    private String strLine;

    public void loadFont(String fontPath, String fontImgPath) {
        StringBuilder sb = new StringBuilder();
        BufferedImage fontImg;
        BufferedReader br;
        String line;

        try {
            fontImg = ImageIO.read(new File(fontImgPath));
            br = new BufferedReader(new FileReader(fontPath));

            File fm = new File("FontMeta.txt");
            FileWriter fw = new FileWriter(new File(fm.toURI()));
            if (!fm.exists()) {
                fm.createNewFile();
            }

            while ((line = br.readLine()) != null) {
                fw.write(line);
                sb.append(line).append('\n');
                if (line.contains("_version")) {
                        for (int i = 0; i < line.length(); i++) {
                            char ln;
                            if (line.charAt(i) == '1') {
                                ln = line.charAt(i);
                                version = Integer.parseInt(String.valueOf(ln));
                                System.out.println("Version(Not Included) :" +version);
                                continue;
                            }
                        }
                }
               if (line.contains("_type")) {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.contains("font")) {
                            type = "font";
                        } else {
                            type = "null";
                        }
                    }
                    System.out.println("File Type(Not Included) :" + type);
                    continue;
                }
                if (line.contains("_fontSize")) {
                    for (int i = 0; i < line.length(); i++) {
                        char ln;
                        if (line.charAt(i) == '8') {
                            ln = line.charAt(i);
                            fontSize = Integer.parseInt(String.valueOf(ln));
                            System.out.println("Font Texture Size (Not Included) :" + fontSize);
                        }
                        continue;
                    }
                }
                if (line.startsWith("_comment")) {
                    continue;
                }

                if (line.contains("char:")) {
                    for (int c = 0; c < 128; c++) {
                        line.getChars(line.length() - 8, line.length(), line.toCharArray(), 8);

                        x++;
                        if (x >= 26) {
                            y++;
                            x = 0;
                        }
                        if (c == 128) {
                            continue;
                        }
                        strLine = line;
                        setupChars(c, x, y);
                    }
                }

                if (line.startsWith("//")) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setupChars(int idx, int x, int y) {
        int ln = 0;
        if (strLine.contains(String.valueOf(ln))) {
            ln = strLine.charAt(17);
            int xx = Integer.parseInt(String.valueOf(ln));
            System.out.println(xx);
        }

   //     System.out.println("Idx :" + idx + "X :" + x + "Y :" + y);
    }

}
//functional char searcher
//                    if (line.charAt(i) == '8') {
//                        ln = line.charAt(i);
//                        System.out.println(ln);
//                        continue;
//                    }