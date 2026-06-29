package com.servilious.projmtn.gui;

import com.servilious.projmtn.renderer.VertexLoader;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.util.MathHelper;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class GuiRenderer {
    private final BaseModel quad;
    private GuiShader shader;


    public GuiRenderer(VertexLoader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    public void renderGUIs(List<GuiTexture> guis) {
        shader.start();
        glBindVertexArray(quad.getVaoId());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        for (GuiTexture gui : guis) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gui.getTex());
            Matrix4f mat = MathHelper.setupTransformationMatrix(gui.getPos(), gui.getScale());
            shader.loadTransformation(mat);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.stop();
    }

    public void renderGUI(GuiTexture gui) {
        shader.start();
        glBindVertexArray(quad.getVaoId());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
      //  for (GuiTexture gui : guis) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gui.getTex());
            Matrix4f mat = MathHelper.setupTransformationMatrix(gui.getPos(), gui.getScale());
            shader.loadTransformation(mat);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
     //   }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.stop();
    }

    public void clear() {
        shader.clearMem();
    }
}
