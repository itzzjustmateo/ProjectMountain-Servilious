package com.servilious.projmtn.model;

import com.servilious.projmtn.model.textures.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Model {
    private TexturedModel tm;
    private Vector3f pos;
    private float rx, ry, rz;
    private float scale;


    public Model(TexturedModel tm, Vector3f pos, float rx, float ry, float rz, float scale) {
        this.tm = tm;
        this.pos = pos;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
    }


    public void increasePos(float dx, float dy, float dz) {
        this.pos.x += dx;
        this.pos.y += dy;
        this.pos.z += dz;
    }

    public void increaseRot(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
    }

    public TexturedModel getTm() {
        return tm;
    }

    public void setTm(TexturedModel tm) {
        this.tm = tm;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
