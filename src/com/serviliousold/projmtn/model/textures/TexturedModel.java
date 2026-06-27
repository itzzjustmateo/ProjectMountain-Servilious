package com.serviliousold.projmtn.model.textures;

import com.serviliousold.projmtn.model.BaseModel;

public class TexturedModel {
    private BaseModel model;
    private BaseModelTexture tex;

    public TexturedModel(BaseModel model, BaseModelTexture tex) {
        this.model = model;
        this.tex = tex;
    }

    public BaseModel getModel() {
        return model;
    }

    public BaseModelTexture getTex() {
        return tex;
    }
}
