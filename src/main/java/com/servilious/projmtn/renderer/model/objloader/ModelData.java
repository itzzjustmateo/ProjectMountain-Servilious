package com.servilious.projmtn.renderer.model.objloader;

public class ModelData {
    private float[] vertices;
    private float[] texCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;

    public ModelData(float[] vertices, float[] texCoords, float[] normals, int[] indices, float furthestPoint) {
        this.vertices = vertices;
        this.texCoords = texCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public float getFurthestPoint() {
        return furthestPoint;
    }
}
