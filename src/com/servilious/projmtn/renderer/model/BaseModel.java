package com.servilious.projmtn.renderer.model;

public class BaseModel {
    private int vaoId;
    private int vertexCount;

    public BaseModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
