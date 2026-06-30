package com.servilious.projmtn.window;

import com.servilious.projmtn.GlobalConstants;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;

public class GameWindowManager {
    public static int width = 1280;
    public static int height = 720;
    private static int fps = 60;
    private long window;
    private long fullScreenMonitor;
    private String title = "Project Mountain : Servilious";
    private boolean forceFullscreen = false;

    private static long lastFrameTime;
    private static float delta;


    public void createWindow() {
        createPrint(System.err).set();
        glfwInit();
        if (!glfwInit()) {
            throw new RuntimeException("Failed to Initialize GLFW!");
        }
        fullScreenMonitor = glfwGetPrimaryMonitor();

        window = glfwCreateWindow(width, height, title, forceFullscreen ? fullScreenMonitor : 0L, 0L);
        if (window == 0L) {
            throw new RuntimeException("Failed to create an GLFW Window!");
        }

        glfwDefaultWindowHints();

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);
        GL.createCapabilities();
        if (forceFullscreen) {
            glfwSetWindowMonitor(window, fullScreenMonitor, 0, 0, 1920, 1080, glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate());
        } else {
            glfwSetWindowMonitor(window, 0L, 1920 / 2 - 450, 1080 / 2 - 300, width, height, glfwGetVideoMode(glfwGetPrimaryMonitor()).refreshRate());
        }
//
    //     int w[] = new int[1], h[]= new int[1], a[]= new int[1];
    //    GLFWImage.Buffer buf = GLFWImage.create(1);
     //   GLFWImage icon256 = GLFWImage.create().set(w[0], h[0],STBImage.stbi_load(GlobalConstants.getResourcePath() + "/textures/icon/icon256.png", w, h, a, 4));
      //  icon256 = new GLFWImage(STBImage.stbi_load(GlobalConstants.getResourcePath() + "/textures/icon/icon256.png", w, h, a, 4));
    //    buf.put(icon256);
     //   icon256.free();

    //    glfwSetWindowIcon(window, buf);
    }

    public void updateWindow() {
        glfwSwapBuffers(window);
        glfwPollEvents();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000F;
        lastFrameTime = currentFrameTime;
    }


    public static float getFrameTimeSeconds() {
        return delta;
    }

    public void destroyWindow() {
        glfwWindowShouldClose(window);
    }

    public boolean shouldDestroy() {
        return glfwWindowShouldClose(window);
    }

    private long getCurrentTime() {
        return (long) (glfwGetTime() * 1000 / glfwGetTime()); //add F front of 1000Sys.getTimerResolution()
    }

    public long getWindow() {
        return window;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
