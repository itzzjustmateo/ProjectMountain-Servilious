package com.servilious.projmtn;

import com.servilious.projmtn.gui.Font;
import com.servilious.projmtn.gui.FontRenderer;
import com.servilious.projmtn.gui.GuiRenderer;
import com.servilious.projmtn.gui.GuiTexture;
import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.renderer.MasterRenderer;
import com.servilious.projmtn.renderer.Player;
import com.servilious.projmtn.renderer.VertexLoader;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.renderer.model.Light;
import com.servilious.projmtn.renderer.model.Model;
import com.servilious.projmtn.renderer.model.objloader.ModelData;
import com.servilious.projmtn.renderer.model.objloader.OBJLoader;
import com.servilious.projmtn.renderer.model.textures.ModelTexture;
import com.servilious.projmtn.renderer.model.textures.TerrainTexture;
import com.servilious.projmtn.renderer.model.textures.TerrainTexturePack;
import com.servilious.projmtn.renderer.model.textures.TexturedModel;
import com.servilious.projmtn.util.MousePicker;
import com.servilious.projmtn.window.GameWindowManager;
import com.servilious.projmtn.world.Terrain;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    //* Constants *//
    private static boolean isDevMode = false;
    private static boolean fullscreenEnabled = false;
    private static List<Model>allTrees;
    private static Random rand = new Random();
    private static boolean isPaused = false;
    private ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 implGl3 = new ImGuiImplGl3();
    private String glslVer = "#version 330 core";
    private GameWindowManager windowManager = new GameWindowManager();


    public void main(String[] args) {
        ImGui.createContext();
        for (int i = 0; i < args.length; i++) { //i copied this from classic 0.0.13a main method
            if (args[i].equalsIgnoreCase("-enableDevMode")) {
                isDevMode = true;
                System.out.println("Dev Mode is Enabled!");
            }
            if (args[i].equalsIgnoreCase("-enableFullscreen")) {
                fullscreenEnabled = true;
                System.out.println("Fullscreen Enabled");
            }
        }


        windowManager.createWindow();
        initImGUI();
        VertexLoader loader = new VertexLoader();

        TerrainTexture bgTex = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTex = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTex = new TerrainTexture(loader.loadTexture("rockpath"));
        TerrainTexture bTex = new TerrainTexture(loader.loadTexture("rockpath"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("map/blend_grassmap"));

        TerrainTexturePack terrainTexture = new TerrainTexturePack(bgTex, rTex, gTex, bTex);


        ModelData player = OBJLoader.loadObjFile("entity/player/player");
        ModelData treePine = OBJLoader.loadObjFile("trees/TreeModelTM");
        ModelData shortGrass = OBJLoader.loadObjFile("grassModel");
        ModelData lampPost = OBJLoader.loadObjFile("lamp/lampPost");

        BaseModel treeModel = loader.loadToVAO(treePine.getVertices(), treePine.getTexCoords(), treePine.getNormals(), treePine.getIndices());
        BaseModel shortGrassModel = loader.loadToVAO(shortGrass.getVertices(), shortGrass.getTexCoords(), shortGrass.getNormals(), shortGrass.getIndices());
        BaseModel playerModel = loader.loadToVAO(player.getVertices(), player.getTexCoords(), player.getNormals(), player.getIndices());
        BaseModel lampPostModel = loader.loadToVAO(lampPost.getVertices(), lampPost.getTexCoords(), lampPost.getNormals(), lampPost.getIndices());

        ModelTexture modelTex = new ModelTexture(loader.loadTexture("tree/TreeTexture"));
        ModelTexture modelTex1 = new ModelTexture(loader.loadTexture("plant/plantGrassShort"));
        modelTex1.setNumOfRows(2);
        ModelTexture playerModelTex = new ModelTexture(loader.loadTexture("entity/player/player"));
        ModelTexture lampModelTex = new ModelTexture(loader.loadTexture("lamp/lampPost"));

        modelTex1.setTransparent(true);

        TexturedModel texturedModel = new TexturedModel(treeModel, modelTex);
        TexturedModel texturedModel1 = new TexturedModel(shortGrassModel, modelTex1);
        TexturedModel texturedModel2 = new TexturedModel(playerModel, playerModelTex);
        TexturedModel texturedLampPostModel = new TexturedModel(lampPostModel, lampModelTex);

        Model lampModel = new Model(texturedLampPostModel, new Vector3f(185,-4.7f,-293),0,0,0,1);
        Player modelPlayer = new Player(texturedModel2, new Vector3f(-10, 5, 0), 0,0,0, 0.6f, windowManager);

        Model model2  = new Model(texturedModel1, rand.nextInt(4), new Vector3f(2,0,2),0,0,0,1);



        ModelTexture tex = texturedModel.getModelTex();
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("grass"));
        modelTexture.setShineFactor(1);
        modelTexture.setReflectivity(10000000);
        allTrees = new ArrayList<>();
        playerModelTex.setUseFakeLight(true);
        playerModelTex.setShineFactor(10);
        playerModelTex.setReflectivity(1000);
        tex.setUseFakeLight(true);
        tex.setShineFactor(10);
        tex.setReflectivity(1000);
        modelTex1.setUseFakeLight(true);
        modelTex1.setShineFactor(10);
        modelTex1.setReflectivity(1000);
        lampModelTex.setUseFakeLight(true);
        lampModelTex.setShineFactor(10);
        lampModelTex.setReflectivity(1000);

        Terrain terArrX[] = new Terrain[8];


        terArrX[0] = new Terrain(1,-1, loader, terrainTexture, blendMap, "flatmap");//negative quad positive X: 0, negative Z: 1
        terArrX[1] = new Terrain(-1,1, loader, terrainTexture, blendMap, "flatmap");//negative quad X: 1, Z: positive 1
        terArrX[2] = new Terrain(-0,-1, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 0, Z: 1
        terArrX[3] = new Terrain(-1,-0, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 1, Z: 0
        terArrX[4] = new Terrain(-1,-1, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 1, Z: 1

        terArrX[5] = new Terrain(0,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 0, Z: 0
        terArrX[6] = new Terrain(0,1, loader, terrainTexture, blendMap, "flatmap");//positive quad X: 0, Z: 1
        terArrX[7] = new Terrain(1,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 1, Z: 0



//        terArrX[0] = new Terrain(1,-1, loader, terrainTexture, blendMap, "heightmap_5");//negative quad positive X: 0, n
//        terArrX[1] = new Terrain(-1,1, loader, terrainTexture, blendMap, "heightmap_7");//negative quad X: 1, Z: positiv
//        terArrX[2] = new Terrain(-0,-1, loader, terrainTexture, blendMap, "heightmap_1"); //negative quad X: 0, Z: 1
//        terArrX[3] = new Terrain(-1,-0, loader, terrainTexture, blendMap, "heightmap_4"); //negative quad X: 1, Z: 0
//        terArrX[4] = new Terrain(-1,-1, loader, terrainTexture, blendMap, "heightmap_8"); //negative quad X: 1, Z: 1
//        terArrX[5] = new Terrain(0,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 0, Z: 0
//        terArrX[6] = new Terrain(0,1, loader, terrainTexture, blendMap, "hillymap_1");//positive quad X: 0, Z: 1
//        terArrX[7] = new Terrain(1,0, loader, terrainTexture, blendMap, "reactormap"); //positive quad X: 1, Z: 0

        for (int x = 0; x < 768; x++) {
            float xRand = (float) (rand.nextFloat() * x - 30 % 5 + Math.floorMod(90, 190) / 24 % 6 * Math.cos(75) + Math.sin(80) * 3) % 20 / 3 + x % 29 + (50 / 2 + Math.abs(x) % 2 * 10 / 2 +(float) Math.floor(x) / 2 % 3 * 90)  ; //idek bro
            float zRand = (float) ((float) rand.nextFloat() * x % 30 / 5 % 4 / 24 / 2 % 5 * 2 * Math.sin(75) + (Math.cos(90) * 3) / 2 % 8 * 2 + x + Math.pow(x, xRand) % 5 * 10 / 5 + 5 + Math.floor(x) / 2 / 3 % 1 / 1000);
            float y = terArrX[0].getHeightOfTerrain(xRand, zRand);
            allTrees.add(new Model(texturedModel, new Vector3f(xRand, y, zRand), 0, 0, 0, 1));
        }


        Light light = new Light(new Vector3f(200000, 1000000, 200000), new Vector3f(1f, 1f, 1f));
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
        lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));


     //   Mouse.setGrabbed(true);
    //    Mouse.setCursorPosition(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2);


        Camera camera = new Camera(modelPlayer);
        MasterRenderer renderer = new MasterRenderer(loader, windowManager);

        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
        GuiTexture hudGear = new GuiTexture(loader.loadTexture("gui/HUD_GearGUI"), new Vector2f(0.82F, -0.12F), new Vector2f(0.15f, 0.5F));
        GuiTexture hudBarHP = new GuiTexture(loader.loadTexture("gui/HUD_EmptyBarGUI"), new Vector2f(0.04F, -0.75F), new Vector2f(0.45f, 0.065f));
        GuiTexture hudBarStamina = new GuiTexture(loader.loadTexture("gui/HUD_EmptyBarGUI"), new Vector2f(0.04F, -0.90f), new Vector2f(0.45f, 0.065f));
        GuiTexture hudMinimapSlot = new GuiTexture(loader.loadTexture("gui/HUD_MiniMapSlotGUI"), new Vector2f(-0.745F, -0.6f), new Vector2f(0.25f, 0.35f));
        GuiTexture guiButton = new GuiTexture(loader.loadTexture("gui/Menu_ButtonGUI"), new Vector2f(0, 0), new Vector2f(0.25f, 0.35f));
        GuiTexture guiFont = new GuiTexture(loader.loadTexture("font/font"), new Vector2f(0, 0), new Vector2f(0.75f, 0.75f));

        guiTextures.add(hudGear);
        guiTextures.add(hudBarHP);
        guiTextures.add(hudBarStamina);
        guiTextures.add(hudMinimapSlot);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        FontRenderer fontRenderer = new FontRenderer(loader);

        glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terArrX[5]);
        Font font = new Font();
        font.loadFont( GlobalConstants.getResourcePath() +  "/font.afnt", GlobalConstants.getResourcePath() + "/textures/font/font.png");


        while (!windowManager.shouldDestroy()) {
            implGl3.newFrame();
            implGlfw.newFrame();
            ImGui.newFrame();
            ImGui.begin("Hello World");
            camera.move(isDevMode, windowManager);
            ImGui.setWindowSize(new ImVec2(200, 200));
            ImGui.textColored(255, 255, 0, 255, "Hello World ");

            ImGui.end();

            for (int i = 0; i < terArrX.length; i++) {
                modelPlayer.move(terArrX[i], isDevMode);
                renderer.processTerrain(terArrX[i]);
            }

            renderer.processModel(model2);
            renderer.processModel(modelPlayer);
            renderer.processModel(lampModel);

            for (Model model1 : allTrees) {
                renderer.processModel(model1);
            }
            renderer.render(lights, camera);

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_F1) != GLFW_PRESS) {
                guiRenderer.renderGUIs(guiTextures);
              //  fontRenderer.renderFont(guiFont);
            }

            if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS && !isPaused) {//if the key escape is down un grab the mouse
                isPaused = true;
                if (isPaused) {
                    guiRenderer.renderGUI(guiButton);
                }
                glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                // Mouse.setGrabbed(false);

            }

            if(glfwGetMouseButton(windowManager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                isPaused = false;
                glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            }

            ImGui.render();
            implGl3.renderDrawData(ImGui.getDrawData());
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupCurrentContext = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupCurrentContext);
            }
            windowManager.updateWindow();
        }
        guiRenderer.clear();
        renderer.clear();
        loader.clearData();
        windowManager.destroyWindow();
    }

    private void initImGUI() {
        ImGui.init();
        implGlfw.init(windowManager.getWindow(), true);
        implGl3.init(glslVer);
    }
}


///unused code
//        Terrain quad1m1 = new Terrain(1,-1, loader, terrainTexture, blendMap, "heightmap_5");//negative quad positive X: 0, negative Z: 1
//        Terrain quadm1p1 = new Terrain(-1,1, loader, terrainTexture, blendMap, "heightmap_7");//negative quad X: 1, Z: positive 1
//        Terrain mQuad01 = new Terrain(-0,-1, loader, terrainTexture, blendMap, "heightmap_1"); //negative quad X: 0, Z: 1
//        Terrain mQuad10 = new Terrain(-1,-0, loader, terrainTexture, blendMap, "heightmap_4"); //negative quad X: 1, Z: 0
//        Terrain mQuad11 = new Terrain(-1,-1, loader, terrainTexture, blendMap, "heightmap_8"); //negative quad X: 1, Z: 1
//
//        Terrain quad00 = new Terrain(0,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 0, Z: 0
//        Terrain quad01 = new Terrain(0,1, loader, terrainTexture, blendMap, "hillymap_1");//positive quad X: 0, Z: 1
//        Terrain quad10 = new Terrain(1,0, loader, terrainTexture, blendMap, "reactormap"); //positive quad X: 1, Z: 0
//        Terrain quad11 = new Terrain(1,1, loader, terrainTexture, blendMap, "hillymap_0"); //positive quad X: 1, Z: 1
/*
            //modelPlayer.move(quad00, isDevMode);

    //        picker.tick();
         //   Vector3f terrainPointer = picker.getCurrentRay();
            ///code to move lamp around using mouse position
//            if (terrainPointer != null) {
//                lampModel.setPos(terrainPointer);
//                light.setPos(new Vector3f(terrainPointer.x, terrainPointer.y + 10, terrainPointer.z));
//            }

      //      System.out.println(picker.getCurrentRay());


        //    ttf.drawString(100, 100, "Hello World");

//            renderer.processTerrain(mQuad11);
//            renderer.processTerrain(mQuad10);
//            renderer.processTerrain(mQuad01);
//            renderer.processTerrain(quad1m1);
//            renderer.processTerrain(quadm1p1);
//            renderer.processTerrain(quad00);
//            renderer.processTerrain(quad01);
//            renderer.processTerrain(quad10);
//            renderer.processTerrain(quad11);

//                for (int j = 0; j < terArrY.length; j++) {
//                    renderer.processTerrain(terArrY[j]);
//                }

 */