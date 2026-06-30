package com.servilious.projmtn.gui;


import org.joml.Vector2f;
import org.joml.Vector3f;

public class GuiTexture {
    private int tex;
    private Vector2f pos;
    private Vector2f scale;
    private Vector3f color;


    public GuiTexture(int tex, Vector2f pos, Vector2f scale, Vector3f color) {
        this.tex = tex;
        this.pos = pos;
        this.scale = scale;
        this.color = color;
    }

    public int getTex() {
        return tex;
    }

    public Vector2f getPos() {
        return pos;
    }

    public Vector2f getScale() {
        return scale;
    }
}
