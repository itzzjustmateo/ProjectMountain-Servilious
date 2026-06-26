package com.servilious.projmtn.model;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

public class VertexLoader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> texs = new ArrayList<>();


    public BaseModel sendToVao(float[] positions, float texCoords[], int indices[]) {
        int vaoID = createVAO();
        setupEBO(indices);

        setupVBOs(0, 3, positions);
        setupVBOs(1, 2, texCoords);
        unbindVAO();
        return new BaseModel(vaoID, indices.length);
    }

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    public int loadTexture(String resPath) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("resources" + resPath));
        } catch (IOException e) {
            System.err.println("Failed to load Texture");
        }
        int texId = texture.getTextureID();
        texs.add(texId);

        return texId;
    }

    private void setupVBOs(int attribId, int vecSize, float data[]) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer fb = storeToFB(data);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glVertexAttribPointer(attribId, vecSize, GL_FLOAT, false,0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void setupEBO(int indices[] ) {
        int eboID = glGenBuffers();
        vbos.add(eboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer ib = storeToIB(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);
    }

    private FloatBuffer storeToFB(float data[]) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    private IntBuffer storeToIB(int data[]) {
        IntBuffer ib = BufferUtils.createIntBuffer(data.length);
        ib.put(data);
        ib.flip();
        return ib;
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    public void clearMem() {
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
            System.out.println("Clearing All VAO of : " + vao);
        }
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
            System.out.println("Clearing All VBO / EBO of : " + vbo);
        }

        for (int tex : texs) {
            glDeleteTextures(tex);
            System.out.println("Clearing All Texture ID's of : " + tex);
        }
    }
}
