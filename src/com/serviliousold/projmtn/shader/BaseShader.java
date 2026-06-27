package com.serviliousold.projmtn.shader;

import com.serviliousold.projmtn.light.Light;
import com.serviliousold.projmtn.util.Camera;
import com.serviliousold.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;

public class BaseShader extends BaseShaderProgram {
    private static final String vsh = "resources/shaders/VertexShader.vsh";
    private static final String fsh = "resources/shaders/FragmentShader.fsh";

    private int locaTransform;
    private int locaView;
    private int locaProj;
    private int locaLightPos;
    private int locaLightCol;

    public BaseShader() {
        super(vsh, fsh);
  //      bindAttribs();
      //  getAllUniformLocations();
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "Position");
        super.bindAttrib(1, "TextureCoords");
        super.bindAttrib(2, "Normals");
    }

    @Override
    protected void getAllUniformLocations() {
        locaTransform = super.getUniformLocation("transform");
        locaView = super.getUniformLocation("view");
        locaProj = super.getUniformLocation("proj");
        locaLightPos = super.getUniformLocation("lightPos");
        locaLightCol = super.getUniformLocation("lightColor");
    }

    public void loadTransformation(Matrix4f transform) {
        super.setMat4f(locaTransform, transform);
    }

    public void loadLightToShader(Light light) {
        super.setVec3f(locaLightPos, light.getLightPos());
        super.setVec3f(locaLightCol, light.getLightColor());
    }

    public void loadView(Camera camera) {
        Matrix4f view = MathHelper.setupView(camera);
        super.setMat4f(locaView, view);
    }

    public void loadProjection(Matrix4f proj) {
        super.setMat4f(locaProj, proj);
    }
}
