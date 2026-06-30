package com.servilious.projmtn.renderer;

import com.servilious.projmtn.GlobalConstants;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.renderer.model.textures.TextureData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class VertexLoader {
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public BaseModel loadToVAO(float[] positions, float[] texCoords, float[] normals, int[] indices) { //This is to load General Models / vertices
        int vaoId = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, texCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new BaseModel(vaoId, indices.length);
    }

    public BaseModel loadToVAO(float[] positions, int dimensions) {//This is for GUI
        int vaoId = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new BaseModel(vaoId, positions.length / dimensions);
    }

    public BaseModel loadToVAO(float[] positions, float texCoords[], int dimensions) { //This is for GUI
        int vaoId = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        this.storeDataInAttributeList(1, dimensions, texCoords);
        unbindVAO();
        return new BaseModel(vaoId, positions.length / dimensions);
    }


    public int loadTexture(String resourcePath) {
        IntBuffer w, h, a;
        ByteBuffer data = null;
        int width = 0, height = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            w = stack.mallocInt(1);
            h = stack.mallocInt(1);
            a = stack.mallocInt(1);

            data = STBImage.stbi_load(GlobalConstants.getResourcePath() + "/textures/" + resourcePath + ".png", w, h, a, 4);

            width = w.get();
            height = h.get();


        } catch (Exception e) {
            System.err.println("[ERROR-8] Failed to load Texture, Either it's null or Incorrect Path or it Doesn't Exist!");
            e.printStackTrace();
            System.exit(-8);
        }
        int texId = glGenTextures();
        textures.add(texId);
        glBindTexture(GL_TEXTURE_2D, texId);

        if (data !=null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D); //mip mapping code, well prob change where it is in the future
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4F);
        }
        STBImage.stbi_image_free(data);
        return texId;
    }


    public int loadCubemap(String[] cubeMapTextures) { //Doesn't work properly yet
        int texId = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texId);
        IntBuffer w, h, a;
        ByteBuffer data;
        int width = 0, height = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            w = stack.mallocInt(1);
            h = stack.mallocInt(1);
            a = stack.mallocInt(1);
            for (int i = 0; i < cubeMapTextures.length; i++) {
                data = STBImage.stbi_load(GlobalConstants.getResourcePath() + "/textures/cubemap/" + cubeMapTextures[i] + ".png", w, h, a, 4);  // decodeTexFile("resources/textures/cubemap/" + cubeMapTextures[i] + ".png");
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            }
            width = w.get();
            height = h.get();
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        textures.add(texId);
        glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return texId;
    }

    public void clearData() {
         for (int vaoId : vaos) {
             glDeleteVertexArrays(vaoId);
         }
         for (int vboId : vbos) {
             glDeleteBuffers(vboId);
         }
         for (int texId : textures) {
             glDeleteTextures(texId);
         }
    }

    private int createVAO() {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        return vaoId;
    }

    private void storeDataInAttributeList(int attributeId, int coordSize, float[] data) {
        int vboId = glGenBuffers();
        vbos.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        FloatBuffer fb = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeId, coordSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
   }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = glGenBuffers();
        vbos.add(vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer ib = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer ib = BufferUtils.createIntBuffer(data.length);
        ib.put(data);
        ib.flip();
        return ib;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }
}
