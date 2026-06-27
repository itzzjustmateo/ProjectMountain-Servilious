package com.servilious.projmtn.shaders;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainShader extends AbstractShaderProgram {
    private static final int MAX_LIGHT = 15;

    private static final String VERTEX_SHADER_FILE = "resources/shaders/terrain/TerrainVertexShader.vsh";
    private static final String FRAGMENT_SHADER_FILE = "resources/shaders/terrain/TerrainFragmentShader.fsh";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos[];
    private int location_lightColor[];
    private int location_attenuation[];
    private int location_shineFactor;
    private int location_reflectivity;
    private int location_skyColor;
    private int location_bgTex;
    private int location_rTex;
    private int location_gTex;
    private int location_bTex;
    private int location_blendMap;

    public TerrainShader() {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transform");
        location_projectionMatrix = super.getUniformLocation("proj");
        location_viewMatrix = super.getUniformLocation("view");
        location_shineFactor = super.getUniformLocation("shineFactor");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColor");
        location_bgTex = super.getUniformLocation("bgTex");
        location_rTex = super.getUniformLocation("rTex");
        location_gTex = super.getUniformLocation("gTex");
        location_bTex = super.getUniformLocation("bTex");
        location_blendMap = super.getUniformLocation("blendMap");
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

    public void connectTextureUnits() {
        super.loadInt(location_bgTex, 0);
        super.loadInt(location_rTex, 1);
        super.loadInt(location_gTex, 2);
        super.loadInt(location_bTex, 3);
        super.loadInt(location_blendMap, 4);
    }
    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
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
                super.loadVector(location_lightPos[i], new Vector3f(0, 0,0));
                super.loadVector(location_lightColor[i], new Vector3f(0, 0,0));
                super.loadVector(location_attenuation[i], new Vector3f(1, 0,0));
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
