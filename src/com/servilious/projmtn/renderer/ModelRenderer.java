package com.servilious.projmtn.renderer;

import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.renderer.model.Model;
import com.servilious.projmtn.renderer.model.textures.ModelTexture;
import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import com.servilious.projmtn.shaders.BaseShader;
import com.servilious.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ModelRenderer {
    private BaseShader shader;

    public ModelRenderer(BaseShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Model>> models) {
        for (TexturedModel model : models.keySet()) {
            prepareTexturedModel(model);
            List<Model> batch = models.get(model);
            for (Model model1 : batch) {
                prepareRenderInstance(model1);
                glDrawElements(GL_TRIANGLES, model.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texModels) {
        BaseModel baseModel = texModels.getModel();
        glBindVertexArray(baseModel.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        ModelTexture texture = texModels.getModelTex();
        shader.loadNumOfRows(texture.getNumOfRows());
        if (texture.isTransparent()) {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLighting(texture.shouldUseFakeLight());
        shader.loadAdditionalLightingFeatures(texture.getShineFactor(), texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texModels.getModelTex().getId());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareRenderInstance(Model model) {
        Matrix4f transformationMatrix = MathHelper.setupTransformationMatrix(model.getPos(), model.getRotX(), model.getRotY(), model.getRotZ(), model.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(model.getTexXOffset(), model.getTexYOffset());
    }
}