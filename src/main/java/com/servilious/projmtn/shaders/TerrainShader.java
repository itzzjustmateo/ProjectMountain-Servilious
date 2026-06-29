package com.servilious.projmtn.shaders;

import com.servilious.projmtn.GlobalConstants;
import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.util.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class TerrainShader extends BaseShaderProgram {
    private static final int MAX_LIGHT = 15;

    private static final String VERTEX_SHADER_FILE = GlobalConstants.getResourcePath() + "/shaders/terrain/TerrainVertexShader.vsh";
    private static final String FRAGMENT_SHADER_FILE = GlobalConstants.getResourcePath() + "/shaders/terrain/TerrainFragmentShader.fsh";

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
    protected void setUniformLocations() {
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
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "texCoords");
        super.bindAttrib(2, "normal");
    }

    public void connectTextureUnits() {
        super.setInt(location_bgTex, 0);
        super.setInt(location_rTex, 1);
        super.setInt(location_gTex, 2);
        super.setInt(location_bTex, 3);
        super.setInt(location_blendMap, 4);
    }
    public void loadSkyColor(float r, float g, float b) {
        super.setVec3(location_skyColor, new Vector3f(r, g, b));
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
                super.setVec3(location_lightPos[i], new Vector3f(0, 0,0));
                super.setVec3(location_lightColor[i], new Vector3f(0, 0,0));
                super.setVec3(location_attenuation[i], new Vector3f(1, 0,0));
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
