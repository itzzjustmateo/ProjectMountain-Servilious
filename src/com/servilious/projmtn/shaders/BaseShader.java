package com.servilious.projmtn.shaders;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class BaseShader extends AbstractShaderProgram {
    private static final int MAX_LIGHT = 15;

    private static final String VERTEX_SHADER_FILE = "resources/shaders/vertex_shader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "resources/shaders/fragment_shader.glsl";

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
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
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
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadNumOfRows(int numOfRows) {
        super.loadFloat(location_numOfRows, numOfRows);
    }

    public void loadOffset(float x, float y) {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadAdditionalLightingFeatures(float reflectivity, float shineFactor) {
        super.loadFloat(location_reflectivity, reflectivity);
        super.loadFloat(location_shineFactor, shineFactor);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHT; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPos[i], lights.get(i).getPos());
                super.loadVector(location_lightColor[i], lights.get(i).getColor());
                super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(location_lightPos[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f mat4f) {
        super.loadMatrix(location_transformationMatrix, mat4f);
    }

    public void loadProjectionMatrix(Matrix4f mat4f) {
        super.loadMatrix(location_projectionMatrix, mat4f);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MathHelper.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
