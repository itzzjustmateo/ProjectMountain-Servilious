package com.servilious.projmtn.gui;

import com.servilious.projmtn.GlobalConstants;
import com.servilious.projmtn.shaders.BaseShaderProgram;
import org.joml.Matrix4f;

public class GuiShader extends BaseShaderProgram {
    private static final String VERTEX_PATH = GlobalConstants.getResourcePath() + "/shaders/gui/GuiVertexShader.vsh";
    private static final String FRAGMENT_PATH = GlobalConstants.getResourcePath() + "/shaders/gui/GuiFragmentShader.fsh";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_PATH, FRAGMENT_PATH);
    }

    public void loadTransformation(Matrix4f mat4) {
        super.setMat4(location_transformationMatrix, mat4);
    }


    @Override
    protected void setUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transform");
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
    }
}
