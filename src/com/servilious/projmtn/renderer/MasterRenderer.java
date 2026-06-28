package com.servilious.projmtn.renderer;

import com.servilious.projmtn.cubemap.CubemapRenderer;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.renderer.model.Model;
import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import com.servilious.projmtn.shaders.BaseShader;
import com.servilious.projmtn.shaders.TerrainShader;
import com.servilious.projmtn.world.Terrain;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 2000.0f;
    private static final float VALUE_RED = 0.5F;
    private static final float VALUE_GREEN = 0.8F;
    private static final float VALUE_BLUE = 1.0F;
    private Matrix4f projectionMatrix;
    private BaseShader shader = new BaseShader();
    private ModelRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private CubemapRenderer cubemapRenderer;
    private Map<TexturedModel, List<Model>> models = new HashMap<TexturedModel, List<Model>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MasterRenderer(VertexLoader loader){
        enableCulling();
        setupPerspective();
        renderer = new ModelRenderer(shader,projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
        cubemapRenderer = new CubemapRenderer(loader, projectionMatrix);
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void render(List<Light> lights, Camera cam) {
        prepare();
        shader.start();
        shader.loadSkyColor(VALUE_RED, VALUE_GREEN, VALUE_BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(cam);
        renderer.render(models);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(VALUE_RED, VALUE_GREEN, VALUE_BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(cam);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        cubemapRenderer.render(cam, VALUE_RED, VALUE_GREEN, VALUE_BLUE);
        terrains.clear();
        models.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processModel(Model model) {
        TexturedModel texModel = model.getTexModel();
        List<Model> batch = models.get(texModel);
        if (batch !=null) {
            batch.add(model);
        } else {
            List<Model> newBatch = new ArrayList<Model>();
            newBatch.add(model);
            models.put(texModel, newBatch);
        }
    }

    public void clear() {
        shader.clear();
        terrainShader.clear();
    }

    private void setupPerspective() { //Frustum method
        float aspectRatio = (float) Display.getWidth() / (float)Display.getHeight();
        float y_scale = (1F / (float)Math.tan(Math.toRadians(FOV / 2F))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void prepare() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(VALUE_RED, VALUE_GREEN, VALUE_BLUE, 1.0F);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}