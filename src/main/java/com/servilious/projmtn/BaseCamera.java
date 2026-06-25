package com.servilious.projmtn;

import imgui.ImGui;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class BaseCamera { //This is used for testing until i figure how to add a proper 3d camera with mouse movement
    private Vector3f position = new Vector3f(0, 0, 0);
    private float yaw;
    private float pitch;
    private float movSpd = 0.02f; //Movement Speed


    public void move(long window) {
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            position.z -= movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            position.z += movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            position.x -= movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            position.x += movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            position.y += movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            position.y -= movSpd;
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            yaw -= 0.025f;
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            yaw += 0.025f;
        }

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            pitch += 0.025f;
        }

        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
            pitch -= 0.025f;
        }
    }

    public void drawCamStats() {
        ImGui.textColored(30, 105, 255, 255, "Player XYZ: " + position.x + ", " + position.y + ", " + position.z);

    }

    public Vector3f getPos() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
