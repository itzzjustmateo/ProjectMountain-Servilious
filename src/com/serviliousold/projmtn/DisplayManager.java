package com.serviliousold.projmtn;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
    private String title = "Project Mountain : Servilious";
    private int width = 800;
    private int height = 640;

    public void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 3).withProfileCore(true).withForwardCompatible(true);
        try {
            System.out.println("Creating Display..");
            Display.setTitle(title);
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void updateDisplay() {
        Display.update();
        Display.sync(60);
    }

    public void destroyDisplay() {
        Display.destroy();
    }

    public boolean isDestroyed() {
        return Display.isCloseRequested();
    }
}
