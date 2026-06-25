package com.servilious.projmtn;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL14.*;

public class Skybox {
    private String[] skyboxPaths = {
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png",
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png",
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png",
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png",
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png",
            GlobalConstants.getResourcePath() + "/skybox/skybox_test.png"
    };



    public int loadSkybox() {
        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        IntBuffer w;
        IntBuffer h;
        IntBuffer a;
        ByteBuffer data = null;
        int wi, hi;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            w = stack.mallocInt(1);
            h = stack.mallocInt(1);
            a = stack.mallocInt(1);
            wi = w.get();
            hi = h.get();
            for (int i = 0; i < 5; i++) {
                data = STBImage.stbi_load(skyboxPaths[i], w, h, a, 4);
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,0, GL_RGBA,wi,hi,0,GL_RGBA,GL_UNSIGNED_BYTE,data);
            }

        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        return texID;
    }
}
