package com.serviliousold.projmtn.model.objloader;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
    private static final int NO_INDEX = -1;
    private Vector3f pos;
    private int texIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicatedVertex = null;
    private int index;
    private float length;

    public Vertex(int index, Vector3f pos) {
        this.index = index;
        this.pos = pos;
        this.length = pos.length();
    }

    public int getIndex() {
        return index;
    }

    public float getLength() {
        return length;
    }

    public Vector3f getPos() {
        return pos;
    }

    public int getTexIndex() {
        return texIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public Vertex getDuplicatedVertex() {
        return duplicatedVertex;
    }

    public void setTexIndex(int texIndex) {
        this.texIndex = texIndex;
    }

    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    public void setDuplicatedVertex(Vertex duplicatedVertex) {
        this.duplicatedVertex = duplicatedVertex;
    }

    public boolean isSet() {
        return texIndex != NO_INDEX &&normalIndex != NO_INDEX;
    }

    public boolean hasSameTexAndNormal(int texIndexOther, int normalIndexOther) {
        return texIndexOther == texIndex && normalIndexOther == normalIndex;
    }

}
