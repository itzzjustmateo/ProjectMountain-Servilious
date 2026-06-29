package com.servilious.projmtn.cubemap;

import com.servilious.projmtn.GlobalConstants;
import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.shaders.BaseShaderProgram;
import com.servilious.projmtn.util.MathHelper;
import com.servilious.projmtn.window.GameWindowManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CubemapShader extends BaseShaderProgram {
    private static final String VERTEX_PATH = GlobalConstants.getResourcePath() + "/shaders/cubemap/cubemapVertex_shader.glsl";
    private static final String FRAGMENT_PATH = GlobalConstants.getResourcePath() + "/shaders/cubemap/CubemapFragmentShader.fsh";

    private static final float ROTATION_SPEED = 1F;
    private float rotation = 0;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_blendFactor;
    private int location_dayCubemap;
    private int location_nightCubemap;

    private GameWindowManager manager;

    public CubemapShader(GameWindowManager manager) {
        super(VERTEX_PATH, FRAGMENT_PATH);
        this.manager = manager;
    }

    @Override
    protected void setUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("proj");
        location_viewMatrix = super.getUniformLocation("view");
        location_fogColor = super.getUniformLocation("fogColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_dayCubemap = super.getUniformLocation("dayCubeMap");
        location_nightCubemap = super.getUniformLocation("nightCubeMap");
    }

    public void mergeTextureUnits() {
        super.setInt(location_dayCubemap, 0);
        super.setInt(location_nightCubemap, 1);
    }

    public void loadBlendFactor(float value) {
        super.setFloat(location_blendFactor, value);
    }

    public void loadFogColor(float r, float g, float b) {
        super.setVec3(location_fogColor, new Vector3f(r, g, b));
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f mat4 = MathHelper.createViewMatrix(camera);
        mat4.m30(0);
        mat4.m31(0);
        mat4.m32(0);
        rotation += ROTATION_SPEED * manager.getFrameTimeSeconds();
        mat4.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0));
        super.setMat4(location_viewMatrix, mat4);
    }

    public void loadProjectionMatrix(Matrix4f mat4) {
        super.setMat4(location_projectionMatrix, mat4);
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
    }
}
