package com.servilious.projmtn.shaders;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.util.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class BaseShader extends BaseShaderProgram {
    private static final int MAX_LIGHT = 15;

    private static final String VERTEX_SHADER_FILE = "/shaders/VertexShader.vsh";
    private static final String FRAGMENT_SHADER_FILE = "/shaders/FragmentShader.fsh";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos[];
    private int location_lightColor[];
    private int location_attenuation[];
    private int location_shineFactor;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numOfRows;
    private int location_offset;

    public BaseShader() {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void setUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transform");
        location_projectionMatrix = super.getUniformLocation("proj");
        location_viewMatrix = super.getUniformLocation("view");
        location_shineFactor = super.getUniformLocation("shineFactor");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_numOfRows = super.getUniformLocation("numOfRows");
        location_offset = super.getUniformLocation("offset");
        location_lightPos = new int[MAX_LIGHT];
        location_lightColor = new int[MAX_LIGHT];
        location_attenuation = new int[MAX_LIGHT];
        for (int i = 0; i < MAX_LIGHT; i++) {
            location_lightPos[i] = super.getUniformLocation("lightPos[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "texCoords");
        super.bindAttrib(2, "normal");
    }

    public void loadNumOfRows(int numOfRows) {
        super.setFloat(location_numOfRows, numOfRows);
    }

    public void loadOffset(float x, float y) {
        super.setVec2(location_offset, new Vector2f(x, y));
    }

    public void loadSkyColor(float r, float g, float b) {
        super.setVec3(location_skyColor, new Vector3f(r, g, b));
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.setBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadAdditionalLightingFeatures(float reflectivity, float shineFactor) {
        super.setFloat(location_reflectivity, reflectivity);
        super.setFloat(location_shineFactor, shineFactor);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHT; i++) {
            if (i < lights.size()) {
                super.setVec3(location_lightPos[i], lights.get(i).getPos());
                super.setVec3(location_lightColor[i], lights.get(i).getColor());
                super.setVec3(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.setVec3(location_lightPos[i], new Vector3f(0, 0, 0));
                super.setVec3(location_lightColor[i], new Vector3f(0, 0, 0));
                super.setVec3(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f mat4f) {
        super.setMat4(location_transformationMatrix, mat4f);
    }

    public void loadProjectionMatrix(Matrix4f mat4f) {
        super.setMat4(location_projectionMatrix, mat4f);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MathHelper.createViewMatrix(camera);
        super.setMat4(location_viewMatrix, viewMatrix);
    }
}
