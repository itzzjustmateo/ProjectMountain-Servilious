package com.servilious.projmtn;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Terrain {
    private int terrainSize = 800;
    private float vertices[] = new float[999];
    private int tvao;

    ///
    ///
    /// // i well rewrite this
    public void generateTerrain(int gridX, int gridY) {
        for (int x = 0; x < terrainSize; x++) {
            for (int z = 0; z < terrainSize; z++) {
                for (int y = 0; y < terrainSize; y++) {
                    vertices[x] = -(terrainSize / 10);
                    vertices[x] = (terrainSize / 10);
                    vertices[x] = (terrainSize / 10);
                    vertices[x] = -(terrainSize / 10);

                    vertices[y] = -1;
                    vertices[y]  =-1;
                    vertices[y]  = -1;
                    vertices[y]  = -1;

                    vertices[z] = -(terrainSize / 10);
                    vertices[z]  = (terrainSize / 10);
                    vertices[z]  = (terrainSize / 10);
                    vertices[z]  = -(terrainSize / 10);
                }
            }
        }
        sendTerrainDataToBuffers(vertices);
    }

    void sendTerrainDataToBuffers(float vert[] ) {
        tvao = glGenVertexArrays();
        glBindVertexArray(tvao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vert.length);
        fb.put(vert);
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

//        IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
//        ib.put(indices);
//        ib.flip();
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glEnableVertexAttribArray(0);
    }

    public int getTvao() {
        return tvao;
    }
}


