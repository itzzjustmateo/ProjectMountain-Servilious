package com.servilious.projmtn.renderer;

import com.servilious.projmtn.renderer.model.Model;
import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import com.servilious.projmtn.window.GameWindowManager;
import com.servilious.projmtn.world.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

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

    public Player(TexturedModel texModel, Vector3f pos, float rx, float ry, float rz, float scale) {
        super(texModel, pos, rx, ry, rz, scale);
    }

    public void move (Terrain terrain, boolean isDevMode) {
        checkInput(isDevMode);
        super.increaseRot(0, currentTurnSpeed * GameWindowManager.getFrameTimeSeconds(), 0);
        float dist = currentSpeed * GameWindowManager.getFrameTimeSeconds();
        float dx = dist * (float) Math.sin(Math.toRadians(super.getRotY()));
        float dz = dist * (float) Math.cos(Math.toRadians(super.getRotY()));
        super.increasePos(dx, 0, dz);
        upwardMomentum += GRAVITY * GameWindowManager.getFrameTimeSeconds();
        super.increasePos(0, upwardMomentum * GameWindowManager.getFrameTimeSeconds(), 0);
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
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                this.currentSpeed = RUN_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                this.currentSpeed = -RUN_SPEED;
            } else {
                this.currentSpeed = 0;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) { //player movement: this speed is temporary till  i redo it
                this.currentSpeed = 100;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                this.currentTurnSpeed = -TURN_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                this.currentTurnSpeed = TURN_SPEED;
            } else {
                this.currentTurnSpeed = 0;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                jump();
            }
        }
    }
}
