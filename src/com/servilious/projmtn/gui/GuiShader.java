package com.servilious.projmtn.gui;

import com.servilious.projmtn.shaders.AbstractShaderProgram;
import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends AbstractShaderProgram {
    private static final String VERTEX_PATH = "resources/shaders/gui/GuiVertexShader.vsh";
    private static final String FRAGMENT_PATH = "resources/shaders/gui/GuiFragmentShader.fsh";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_PATH, FRAGMENT_PATH);
    }

    public void loadTransformation(Matrix4f mat4) {
        super.loadMatrix(location_transformationMatrix, mat4);
    }


    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transform");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
