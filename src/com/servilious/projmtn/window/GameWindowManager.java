package com.servilious.projmtn.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.glViewport;

public class GameWindowManager {
    public static int width = 1280;
    public static int height = 720;
    private static int fps = 60;

    private static long lastFrameTime;
    private static float delta;


    public static void createWindow() {
        ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(), attribs);
          //  Display.setTitle("Adventure Studios 3D RPG 20251008");
            Display.setTitle("Project Mountain : Servilious");
            Display.setIcon(WindowIconHandler.loadIcon("/textures/icon/ARPG64.png"));
        } catch (LWJGLException e) {
            System.err.println("[ERROR] Failed to create Display!");
            e.printStackTrace();
        }

        glViewport(0,0, width, height);
        lastFrameTime = getCurrentTime();
    }

    public static void updateWindow() {
        Display.sync(fps);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000F;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void destroyWindow() {
        Display.destroy();
    }

    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution(); //add F front of 1000
    }









    //not perfect full screen code
    public void fullscreenWindow(boolean fullscreenEnabled) {
        if (fullscreenEnabled) {
            try {

                Display.setFullscreen(true);
                this.width = Display.getDisplayMode().getWidth();
                this.height = Display.getDisplayMode().getHeight();
                if (this.width <= 0) {
                    this.width = 1;
                }

                if (this.height <= 0) {
                    this.height = 1;
                }
            } catch (LWJGLException e) {
                throw new RuntimeException(e);
            }
          //  width = 1920;
          //  height = 1080;
            try {
                Display.setDisplayModeAndFullscreen(new DisplayMode(width, height));
            } catch (LWJGLException e) {
                System.err.println("[ERROR-1:1] Failed to update Display Dimensions");
                e.printStackTrace();
            }
            glViewport(0,0, width, height);
        }
    }


    public static void unFullscreenWindow(boolean fullscreenEnabled) {
        if (fullscreenEnabled) {
            try {
                width = 1280;
                height = 720;
                Display.setDisplayModeAndFullscreen(new DisplayMode(width, height));
                Display.setLocation(width / 2, height / 2);
            } catch (LWJGLException e) {
                System.err.println("[ERROR-1:1] Failed to update Display Dimensions");
                e.printStackTrace();
            }
            glViewport(0,0, width, height);
        }
    }







    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
