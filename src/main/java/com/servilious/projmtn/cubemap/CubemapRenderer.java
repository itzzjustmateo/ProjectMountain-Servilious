package com.servilious.projmtn.cubemap;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.VertexLoader;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.window.GameWindowManager;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

public class CubemapRenderer {
    private static float SIZE = 1000F;
    private static String[] DAY_SKYBOX_TEXTURES = {"day/right", "day/left", "day/top", "day/bottom", "day/back", "day/front"};
    private static String[] NIGHT_SKYBOX_TEXTURES = {"night/right", "night/left", "night/top", "night/bottom", "night/back", "night/front"};
    private BaseModel cube;
    private int dayTexture;
    private int nightTexture;
    private CubemapShader shader;

    private float time = 0;


    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    public CubemapRenderer(VertexLoader loader, Matrix4f projectionMatrix, GameWindowManager manager) {
        cube = loader.loadToVAO(VERTICES, 3);
        dayTexture = loader.loadCubemap(DAY_SKYBOX_TEXTURES);
        nightTexture = loader.loadCubemap(NIGHT_SKYBOX_TEXTURES);
        shader = new CubemapShader(manager);
        shader.start();
        shader.mergeTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, float r, float g, float b) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(r, g, b);
        glBindVertexArray(cube.getVaoId());
        glEnableVertexAttribArray(0);
        bindTextures();
        glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.stop();
    }


    private void bindTextures(){ //well change this as every thing else in the future
        time += GameWindowManager.getFrameTimeSeconds() * 1000;
        time %= 24000;
        int texture1 = 0;
        int texture2 = 0;
        float blendFactor;
        //Time
        if(time >= 0 && time < 5000){
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = (time - 0)/(5000 - 0);
        }else if(time >= 5000 && time < 8000){
            texture1 = nightTexture;
            texture2 = dayTexture;
            blendFactor = (time - 5000)/(8000 - 5000);
        }else if(time >= 8000 && time < 21000){
            texture1 = dayTexture;
            texture2 = dayTexture;
            blendFactor = (time - 8000)/(21000 - 8000);
        }else{
            texture1 = dayTexture;
            texture2 = nightTexture;
            blendFactor = (time - 21000)/(24000 - 21000);
        }


        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

}
