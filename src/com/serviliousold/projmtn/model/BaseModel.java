package com.serviliousold.projmtn.model;

public class BaseModel {
    private int vaoID;
    private int verticesCount;

    public BaseModel(int vaoID, int verticesCount) {
        this.vaoID = vaoID;
        this.verticesCount = verticesCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVerticesCount() {
        return verticesCount;
    }
}
