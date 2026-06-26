package com.servilious.projmtn.shader;

import com.servilious.projmtn.util.Camera;
import com.servilious.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;

public class BaseShader extends BaseShaderProgram {
    private static final String vsh = "resources/shaders/VertexShader.vsh";
    private static final String fsh = "resources/shaders/FragmentShader.fsh";

    private int locaTransform;
    private int locaView;
    private int locaProj;

    public BaseShader() {
        super(vsh, fsh);
  //      bindAttribs();
      //  getAllUniformLocations();
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "Position");
    }

    @Override
    protected void getAllUniformLocations() {
        locaTransform = super.getUniformLocation("transform");
        locaView = super.getUniformLocation("view");
        locaProj = super.getUniformLocation("proj");
    }

    public void loadTransformation(Matrix4f transform) {
        super.setMat4f(locaTransform, transform);
    }

    public void loadView(Camera camera) {
        Matrix4f view = MathHelper.setupView(camera);
        super.setMat4f(locaView, view);
    }

    public void loadProjection(Matrix4f proj) {
        super.setMat4f(locaProj, proj);
    }
}
