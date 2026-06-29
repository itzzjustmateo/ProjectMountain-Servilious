package com.servilious.projmtn.renderer.model;

import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import org.joml.Vector3f;


public class Model {
    private TexturedModel texturedModel;
    private Vector3f pos;
    private float rotX, rotY, rotZ;
    private float scale;
    private int texIndex = 0;

    public Model(TexturedModel texModel, Vector3f pos, float rx, float ry, float rz, float scale) {
        this.texturedModel = texModel;
        this.pos = pos;
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
        this.scale = scale;
    }

    public Model(TexturedModel texModel, int index, Vector3f pos, float rx, float ry, float rz, float scale) {
        this.texIndex = index;
        this.texturedModel = texModel;
        this.pos = pos;
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
        this.scale = scale;
    }

    public float getTexXOffset() {
        int column = texIndex % texturedModel.getModelTex().getNumOfRows();
        return (float) column / (float) texturedModel.getModelTex().getNumOfRows();
    }

    public float getTexYOffset() {
        int row = texIndex / texturedModel.getModelTex().getNumOfRows();
        return (float) row / (float) texturedModel.getModelTex().getNumOfRows();
    }


    public void increasePos(float dx, float dy, float dz) {
        this.pos.x += dx;
        this.pos.y += dy;
        this.pos.z += dz;
    }

    public void increaseRot(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /* Getter Methods*/

    public TexturedModel getTexModel() {
        return this.texturedModel;
    }

    public Vector3f getPos() {
        return this.pos;
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public float getScale() {
        return this.scale;
    }

    /* Setter Methods */

    public void setTexModel(TexturedModel texModel) {
        this.texturedModel = texModel;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setRotX(float rx) {
        this.rotX = rx;
    }

    public void setRotY(float ry) {
        this.rotY = ry;
    }

    public void setRotZ(float rz) {
        this.rotZ = rz;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
