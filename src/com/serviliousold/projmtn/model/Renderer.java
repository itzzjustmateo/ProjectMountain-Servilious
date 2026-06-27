package com.serviliousold.projmtn.model;

import com.serviliousold.projmtn.model.textures.TexturedModel;
import com.serviliousold.projmtn.shader.BaseShader;
import com.serviliousold.projmtn.util.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {
    //Perspective Stuff
    private float FOV = 45;
    private float aspect = Display.getDisplayMode().getWidth() / Display.getDisplayMode().getHeight();
    private float NEAR_PLANE = 0.01F;
    private float FAR_PLANE = 1000.0F;

    private Matrix4f proj;

    public Renderer(BaseShader shader) {
        setupPerspective();
        shader.begin();
        shader.loadProjection(proj);
        shader.end();
    }

    public void clearGlBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
    }



    public void render(Model model, BaseShader shader) {
        TexturedModel texturedModel = model.getTm();
        BaseModel baseModel = texturedModel.getModel();
       // glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindVertexArray(baseModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        Matrix4f transform = MathHelper.setupTransformation(model.getPos(), model.getRx(), model.getRy(), model.getRz(), model.getScale());
        shader.loadTransformation(transform);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTex().getTexId());
        glDrawElements(GL_TRIANGLES, baseModel.getVerticesCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    //    glDisable(GL_BLEND);
        glBindVertexArray(0);
    }


    private void setupPerspective() {
        float y_scale = (float) (1.0f / Math.tan(Math.toRadians(FOV / 2)) * aspect);
        float x_scale = y_scale / aspect;
        float frustum = FAR_PLANE - NEAR_PLANE;

        proj = new Matrix4f();
        proj.m00 = x_scale;
        proj.m11 = y_scale;
        proj.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum);
        proj.m23 = -1;
        proj.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustum);
        proj.m33 = 0;
    }

}
