package com.servilious.projmtn.renderer;

import com.servilious.projmtn.renderer.model.Model;
import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import com.servilious.projmtn.window.GameWindowManager;
import com.servilious.projmtn.world.Terrain;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Model {
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_STRENGTH = 30;
    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardMomentum = 0;

    private boolean isInAir = false;
    private GameWindowManager w_Manager;



    public Player(TexturedModel texModel, Vector3f pos, float rx, float ry, float rz, float scale, GameWindowManager manager) {
        super(texModel, pos, rx, ry, rz, scale);
        this.w_Manager = manager;
    }

    public void move(Terrain terrain, boolean isDevMode) {
        checkInput(isDevMode);
        super.increaseRot(0, currentTurnSpeed * w_Manager.getFrameTimeSeconds(), 0);
        float dist = currentSpeed * w_Manager.getFrameTimeSeconds();
        float dx = dist * (float) Math.sin(Math.toRadians(super.getRotY()));
        float dz = dist * (float) Math.cos(Math.toRadians(super.getRotY()));
        super.increasePos(dx, 0, dz);
        upwardMomentum += GRAVITY * w_Manager.getFrameTimeSeconds();
        super.increasePos(0, upwardMomentum * w_Manager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(super.getPos().x, super.getPos().z);
        if (super.getPos().y < terrainHeight) {
            upwardMomentum = 0;
            isInAir = false;
            super.getPos().y = terrainHeight;
        }
    }

    private void jump() {
        if (!isInAir) {
            this.upwardMomentum = JUMP_STRENGTH;
            isInAir = true;
        } else {
            ; //do nothing
        }
    }

    private void checkInput(boolean devMode) {
        if (!devMode) {
            if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
                this.currentSpeed = RUN_SPEED;
            } else if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
                this.currentSpeed = -RUN_SPEED;
            } else {
                this.currentSpeed = 0;
            }
            if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) { //player movement: this speed is temporary till  i redo it
                this.currentSpeed = 100;
            }
            if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
                this.currentTurnSpeed = -TURN_SPEED;
            } else if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
                this.currentTurnSpeed = TURN_SPEED;
            } else {
                this.currentTurnSpeed = 0;
            }
            if (glfwGetKey(w_Manager.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
                jump();
            }
        }
    }
}
