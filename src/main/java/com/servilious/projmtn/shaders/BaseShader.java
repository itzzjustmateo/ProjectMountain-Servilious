package com.servilious.projmtn.shaders;

import com.servilious.projmtn.GlobalConstants;
import org.joml.Matrix4f;

public class BaseShader extends BaseShaderProgram {
    private static final String vsPath = GlobalConstants.getResourcePath() + "/shaders/VertexShader.vsh";
    private static final String fsPath = GlobalConstants.getResourcePath() +  "/shaders/FragmentShader.fsh";
    /**
     * Shader Locations
     */
    private int transformationLoc;
    private int viewLoc;
    private int projLoc;


    public BaseShader() {
        super(vsPath, fsPath);
    }


    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
    }

    @Override
    protected void setUniformLocations() {
        transformationLoc = super.getUniformLocation("transform");
        viewLoc = super.getUniformLocation("view");
        projLoc = super.getUniformLocation("proj");
    }

    public void setTransformation(Matrix4f transformation) {
        super.setMat4(transformationLoc, transformation);
    }

    public void setView(Matrix4f view) {
        super.setMat4(viewLoc, view);
    }

    public void setProj(Matrix4f proj) {
        super.setMat4(projLoc, proj);
    }
}
