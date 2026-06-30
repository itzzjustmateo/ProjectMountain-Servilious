package com.servilious.projmtn.renderer;

import com.servilious.projmtn.window.GameWindowManager;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private float distFromPlayer = 50;
    private float angleSphericalOfPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 12;
    private float yaw;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public float speed = 0.05f;
    public float lookSpeed = 0.3f;
    public int lym, lxm;
    public boolean beenRightClicking = false;
    private GameWindowManager manager;
    private boolean isMouseLocked = true;

    //this class like 90% of classes i plan on to refactor in the future. this is just so it doesn't get too hard following the tutorials by having a completely different code!

    public void move(boolean devMode, GameWindowManager windowManager) {
        this.manager = windowManager;


        if (!isMouseLocked) {
            glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        } else {
            glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }

        if (devMode) {
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_W) == GLFW_PRESS ) {
                position.z -= Math.cos(Math.toRadians(yaw)) * speed;
                position.x += Math.sin(Math.toRadians(yaw)) * speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
                position.z += Math.cos(Math.toRadians(yaw)) * speed;
                position.x -= Math.sin(Math.toRadians(yaw)) * speed;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
                position.x -= Math.cos(Math.toRadians(yaw)) * speed;
                position.z -= Math.sin(Math.toRadians(yaw)) * speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
                position.x += Math.cos(Math.toRadians(yaw)) * speed;
                position.z += Math.sin(Math.toRadians(yaw)) * speed;
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
                speed += 1f;
            } else {
                speed = 0.05f;
            }


            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                position.y -= speed;
            }
            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
                position.y += speed;
            }



            if (isMouseLocked) {
                glfwSetCursorPosCallback(windowManager.getWindow(), new GLFWCursorPosCallback() {
                    @Override
                    public Descriptor getDescriptor() {
                        return super.getDescriptor();
                    }

                    @Override
                    public void callback(long ret, long args) {
                        super.callback(ret, args);
                    }

                    @Override
                    public void invoke(long window, double xpos, double ypos) {
                        int cym = (int) ypos;
                        int cxm = (int) xpos;

                        Vector2f deltaMouse = new Vector2f(cxm - lxm, cym - lym);

                        pitch += deltaMouse.y * lookSpeed;
                        yaw += deltaMouse.x * lookSpeed;

                        lym = (int) ypos;
                        lxm = (int) xpos;

                        lym = (int) ypos;
                        lxm = (int) xpos;

                        if (pitch > 90.0) {
                            pitch = 89.9f;
                        }
                        if (pitch < -90.0) {
                            pitch = -89.9f;
                        }
                    }
                });
            }
        } else if (!devMode) {
          calculateZoom();
          calculatePitch();
          calculateAngleSphericalFromPlayer();
          float horizDist = calculateHorizontalDist();
          float vertDist = calculateVerticelDist();
          calculateCamPos(horizDist, vertDist);
          this.yaw = 180 - (player.getRotY() + angleSphericalOfPlayer);
        }
    }

    private void calculateCamPos(float horizDist, float vertDist) {
        float theta = player.getRotY() + angleSphericalOfPlayer;
        float offsetX = horizDist * (float) Math.sin(Math.toRadians(theta));
        float offsetZ = horizDist * (float) Math.cos(Math.toRadians(theta));
        position.x = player.getPos().x - offsetX;
        position.z = player.getPos().z - offsetZ;
        position.y = player.getPos().y + vertDist;
    }

    private float calculateHorizontalDist() {
        return distFromPlayer * (float) Math.cos(Math.toRadians(pitch));
    }

    private float calculateVerticelDist() {
        return distFromPlayer * (float) Math.sin(Math.toRadians(pitch));
    }


    private void calculateZoom() {
        glfwSetScrollCallback(manager.getWindow(), new GLFWScrollCallback() {
                @Override
                public void invoke(long window, double xoffset, double yoffset) {
                    double zoomLevel = 0;;
                    zoomLevel = -(yoffset * 20f);
                    distFromPlayer += zoomLevel;
                }
            });

    }

    private void calculatePitch() {
        if (glfwGetMouseButton(manager.getWindow(), GLFW_MOUSE_BUTTON_2) == GLFW_PRESS) {

            glfwSetCursorPosCallback(manager.getWindow(), new GLFWCursorPosCallback() {
                @Override
                public void invoke(long window, double xpos, double ypos) {
                    float pitchChange = (float) (ypos * 20f);  // Mouse.getDY() * 0.1f;
                    pitch += pitchChange;
                }
            });
        } else {
            glfwSetCursorPosCallback(manager.getWindow(), null);
        }
    }

    private void calculateAngleSphericalFromPlayer() {
        if (glfwGetMouseButton(manager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
            glfwSetCursorPosCallback(manager.getWindow(), new GLFWCursorPosCallback() {

                @Override
                public void invoke(long window, double xpos, double ypos) {
                    float angleChange = (float) -(xpos * 0.001f);
                    angleSphericalOfPlayer += angleChange;
                }
            });
        } else {
            glfwSetCursorPosCallback(manager.getWindow(), null);
        }
    }

    public Vector3f getPos() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setPos(Vector3f pos) {
        this.position = pos;
    }
}