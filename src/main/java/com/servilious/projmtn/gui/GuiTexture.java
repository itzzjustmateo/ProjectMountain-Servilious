package com.servilious.projmtn.gui;


import org.joml.Vector2f;

public class GuiTexture {
    private int tex;
    private Vector2f pos;
    private Vector2f scale;


    public GuiTexture(int tex, Vector2f pos, Vector2f scale) {
        this.tex = tex;
        this.pos = pos;
        this.scale = scale;
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
