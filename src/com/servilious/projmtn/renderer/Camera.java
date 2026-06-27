package com.servilious.projmtn.renderer;

import com.servilious.projmtn.window.GameWindowManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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

    //this class like 90% of classes i plan on to refactor in the future. this is just so it doesn't get too hard following the tutorials by having a completely different code!

    public void move(boolean devMode) {
        if (devMode) {
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                position.z -= Math.cos(Math.toRadians(yaw)) * speed;
                position.x += Math.sin(Math.toRadians(yaw)) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                position.z += Math.cos(Math.toRadians(yaw)) * speed;
                position.x -= Math.sin(Math.toRadians(yaw)) * speed;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                position.x -= Math.cos(Math.toRadians(yaw)) * speed;
                position.z -= Math.sin(Math.toRadians(yaw)) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                position.x += Math.cos(Math.toRadians(yaw)) * speed;
                position.z += Math.sin(Math.toRadians(yaw)) * speed;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                speed += 1f;
            } else {
                speed = 0.05f;
            }


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                position.y -= speed;
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                position.y += speed;


            if (!beenRightClicking && Mouse.isGrabbed()) {
                lym = Mouse.getY();
                lxm = Mouse.getX();
                beenRightClicking = true;
            }


            if (Mouse.isGrabbed() && Mouse.getEventButton() == 0) {
                int cym = Mouse.getY();
                int cxm = Mouse.getX();

                Vector2f deltaMouse = new Vector2f(cxm - lxm, cym - lym);

                pitch -= deltaMouse.y * lookSpeed;
                yaw += deltaMouse.x * lookSpeed;

                lym = Mouse.getY();
                lxm = Mouse.getX();

                //0,0 left bottom corner
                //maxWidth,maxHeight right top corner
                if (Mouse.getX() < 0 || Mouse.getX() > GameWindowManager.width) {
                    Mouse.setCursorPosition(0, Mouse.getY());
                    System.out.println("Mouse is Not Inside Window");
                }

                if (Mouse.getY() < 0 || Mouse.getY() > GameWindowManager.height) {
                    Mouse.setCursorPosition(Mouse.getX(), 0);
                    System.out.println("Mouse is Not Inside Window");
                }
            } else beenRightClicking = false;
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
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distFromPlayer += zoomLevel;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch += pitchChange;
        }
    }

    private void calculateAngleSphericalFromPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleSphericalOfPlayer += angleChange;
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
}