package com.servilious.projmtn.renderer.model;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    private Vector3f pos;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    public Light(Vector3f pos, Vector3f color) {
        this.pos = pos;
        this.color = color;
    }

    public Light(Vector3f pos, Vector3f color, Vector3f attenuation) {
        this.pos = pos;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
