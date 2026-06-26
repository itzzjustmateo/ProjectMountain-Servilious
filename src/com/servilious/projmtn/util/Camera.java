package com.servilious.projmtn.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 0, 0);
    private float yaw, pitch;
    private float movSpd = 0.05f;
    private float camSpd = 2.5f;

    public void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= movSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += movSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= movSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += movSpd;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += (movSpd * 2);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= (movSpd * 2);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            yaw -= camSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            yaw += camSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            pitch += camSpd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            pitch -= camSpd;
        }

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
