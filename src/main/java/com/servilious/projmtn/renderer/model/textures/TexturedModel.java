package com.servilious.projmtn.renderer.model.textures;

import com.servilious.projmtn.renderer.model.BaseModel;

public class TexturedModel {
    private BaseModel model;
    private ModelTexture tex;

    public TexturedModel(BaseModel model, ModelTexture tex) {
       this.model = model;
       this.tex = tex;
    }

    public BaseModel getModel() {
        return this.model;
    }

    public ModelTexture getModelTex() {
        return this.tex;
    }
}
