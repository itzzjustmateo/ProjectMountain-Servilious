package com.servilious.projmtn.window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WindowIconHandler {
    public static BufferedImage icon = null;
    public static BufferedImage icon16 = null;
    public static BufferedImage icon64 = null;

    public static ByteBuffer[] loadIcon(String resPath) {//a loadIcon Method (i did not create this just copy pasted from a 15 year old lwjgl forum post lol)
        try {
            icon = ImageIO.read(new File("resources" + resPath));
            icon16 = ImageIO.read(new File("resources" + resPath));
            icon64 = ImageIO.read(new File("resources" + resPath));
        } catch (IOException e) {
            System.err.println("[ERROR+2]Failed to Load Icon Image either its a Invalid Image File, Not in the Specified Location or its Null. Aborting Program");
            e.printStackTrace();
            System.exit(2);
        }
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = loadIconInstance(icon, 32);
        buffers[1] = loadIconInstance(icon16, 16);
        buffers[2] = loadIconInstance(icon64, 64);
        return buffers;
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension) {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio = 1;
        if (image.getWidth() > scaledIcon.getWidth()) {
            ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        } else {
            ratio = (int) (scaledIcon.getWidth() / image.getWidth());
        }
        if (image.getHeight() > scaledIcon.getHeight()) {
            double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
            if (r2 < ratio) {
                ratio = r2;
            }
        } else {
            double r2 = (int) (scaledIcon.getHeight() / image.getHeight());
            if (r2 < ratio) {
                ratio = r2;
            }
        }
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) (width), (int) (height), null);
        g.dispose();


        byte[] imageBuffer = new byte[dimension * dimension * 4];
        int counter = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
                imageBuffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
                imageBuffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
                imageBuffer[counter + 3] = (byte) (colorSpace >> 24);
                counter += 4;
            }
        }
        return ByteBuffer.wrap(imageBuffer);
    }
}
