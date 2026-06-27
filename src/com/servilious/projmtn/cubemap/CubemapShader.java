package com.servilious.projmtn.cubemap;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.shaders.AbstractShaderProgram;
import com.servilious.projmtn.util.MathHelper;
import com.servilious.projmtn.window.GameWindowManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class CubemapShader extends AbstractShaderProgram {
    private static final String VERTEX_PATH = "resources//shaders/cubemap/cubemapVertex_shader.glsl";
    private static final String FRAGMENT_PATH = "resources//shaders/cubemap/CubemapFragmentShader.fsh";

    private static final float ROTATION_SPEED = 1F;
    private float rotation = 0;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_blendFactor;
    private int location_dayCubemap;
    private int location_nightCubemap;

    public CubemapShader() {
        super(VERTEX_PATH, FRAGMENT_PATH);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("proj");
        location_viewMatrix = super.getUniformLocation("view");
        location_fogColor = super.getUniformLocation("fogColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_dayCubemap = super.getUniformLocation("dayCubeMap");
        location_nightCubemap = super.getUniformLocation("nightCubeMap");
    }

    public void mergeTextureUnits() {
        super.loadInt(location_dayCubemap, 0);
        super.loadInt(location_nightCubemap, 1);
    }

    public void loadBlendFactor(float value) {
        super.loadFloat(location_blendFactor, value);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector(location_fogColor, new Vector3f(r, g, b));
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f mat4 = MathHelper.createViewMatrix(camera);
        mat4.m30 = 0;
        mat4.m31 = 0;
        mat4.m32 = 0;
        rotation += ROTATION_SPEED * GameWindowManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), mat4, mat4);
        super.loadMatrix(location_viewMatrix, mat4);
    }

    public void loadProjectionMatrix(Matrix4f mat4) {
        super.loadMatrix(location_projectionMatrix, mat4);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
