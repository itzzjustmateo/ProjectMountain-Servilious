package com.servilious.projmtn;

import com.servilious.projmtn.gui.*;
import com.servilious.projmtn.gui.Font;
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
import org.lwjgl.glfw.GLFWCursorPosCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

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

        ModelTexture modelTex = new ModelTexture(loader.loadTexture("tree/BaseTree"));
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

        Terrain terArrX[] = new Terrain[9];


        terArrX[0] = new Terrain(1,-1, loader, terrainTexture, blendMap, "hillymap_4");//negative quad positive X: 0, negative Z: 1
        terArrX[1] = new Terrain(-1,1, loader, terrainTexture, blendMap, "flatmap");//negative quad X: 1, Z: positive 1
        terArrX[2] = new Terrain(-0,-1, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 0, Z: 1
        terArrX[3] = new Terrain(-1,-0, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 1, Z: 0
        terArrX[4] = new Terrain(-1,-1, loader, terrainTexture, blendMap, "flatmap"); //negative quad X: 1, Z: 1

        terArrX[5] = new Terrain(0,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 0, Z: 0
        terArrX[6] = new Terrain(0,1, loader, terrainTexture, blendMap, "flatmap");//positive quad X: 0, Z: 1
        terArrX[7] = new Terrain(1,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 1, Z: 0
        terArrX[8] = new Terrain(1,1, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 1, Z: 0

//        terArrX[0] = new Terrain(1,-1, loader, terrainTexture, blendMap, "heightmap_5");//negative quad positive X: 0, n
//        terArrX[1] = new Terrain(-1,1, loader, terrainTexture, blendMap, "heightmap_7");//negative quad X: 1, Z: positiv
//        terArrX[2] = new Terrain(-0,-1, loader, terrainTexture, blendMap, "heightmap_1"); //negative quad X: 0, Z: 1
//        terArrX[3] = new Terrain(-1,-0, loader, terrainTexture, blendMap, "heightmap_4"); //negative quad X: 1, Z: 0
//        terArrX[4] = new Terrain(-1,-1, loader, terrainTexture, blendMap, "heightmap_8"); //negative quad X: 1, Z: 1
//        terArrX[5] = new Terrain(0,0, loader, terrainTexture, blendMap, "flatmap"); //positive quad X: 0, Z: 0
//        terArrX[6] = new Terrain(0,1, loader, terrainTexture, blendMap, "hillymap_1");//positive quad X: 0, Z: 1
//        terArrX[7] = new Terrain(1,0, loader, terrainTexture, blendMap, "reactormap"); //positive quad X: 1, Z: 0

        for (int x = 0; x < 1024; x++) {
            float randX = rand.nextInt(1024) / rand.nextFloat() * 0.2f;
            float randZ = rand.nextInt(1024) / rand.nextFloat() * 0.75f;
            int i = rand.nextInt(0, 15);
            float y = terArrX[0].getHeightOfTerrain(randX, randZ);
            allTrees.add(new Model(texturedModel, new Vector3f(250 - randX, y,300 - randZ), 0, 0, 0, 1));
        }
        float x = 200000;




        glfwSetCursorPos(windowManager.getWindow(), glfwGetVideoMode(glfwGetPrimaryMonitor()).width() / 2,glfwGetVideoMode(glfwGetPrimaryMonitor()).height() / 2);

        Camera camera = new Camera(modelPlayer);
        MasterRenderer renderer = new MasterRenderer(loader, windowManager);
        float r = 1, g =1, b = 1;
        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
        List<GuiTexture> mainMenuGui = new ArrayList<GuiTexture>();
        GuiTexture hudGear = new GuiTexture(loader.loadTexture("gui/HUD_GearGUI"), new Vector2f(0.82F, -0.12F), new Vector2f(0.15f, 0.5F), new Vector3f(1, 1, 1));
        GuiTexture hudBarHP = new GuiTexture(loader.loadTexture("gui/HUD_EmptyBarGUI"), new Vector2f(0.04F, -0.75F), new Vector2f(0.45f, 0.065f), new Vector3f(1, 1, 1));
        GuiTexture hudBarStamina = new GuiTexture(loader.loadTexture("gui/HUD_EmptyBarGUI"), new Vector2f(0.04F, -0.90f), new Vector2f(0.45f, 0.065f), new Vector3f(1, 1, 1));
        GuiTexture hudMinimapSlot = new GuiTexture(loader.loadTexture("gui/HUD_MiniMapSlotGUI"), new Vector2f(-0.745F, -0.6f), new Vector2f(0.25f, 0.35f), new Vector3f(1, 1, 1));
        GuiTexture guiButton[] = new GuiTexture[3];
        guiButton[0] = new GuiTexture(loader.loadTexture("gui/Menu_ButtonGUI"), new Vector2f(0, 0.25f), new Vector2f(0.35f, 0.15f), new Vector3f(r, g, b));
        guiButton[1] = new GuiTexture(loader.loadTexture("gui/Menu_ButtonGUI"), new Vector2f(0, -0.1f), new Vector2f(0.35f, 0.15f), new Vector3f(1, 1, 1));
        guiButton[2] = new GuiTexture(loader.loadTexture("gui/Menu_ButtonGUI"), new Vector2f(0, -0.45f), new Vector2f(0.35f, 0.15f), new Vector3f(1, 1, 1));
     //   guiButton[3] = new GuiTexture(loader.loadTexture("gui/Menu_ButtonGUI"), new Vector2f(0, -0.8f), new Vector2f(0.35f, 0.15f));
        GuiTexture guiLogo = new GuiTexture(loader.loadTexture("logo/logo"), new Vector2f(-0.035f, 0.65f), new Vector2f(0.45f, 0.25f), new Vector3f(1, 1, 1));



        guiTextures.add(hudGear);
        guiTextures.add(hudBarHP);
        guiTextures.add(hudBarStamina);
        guiTextures.add(hudMinimapSlot);

        mainMenuGui.add(guiLogo);
        mainMenuGui.add(guiButton[0]);
        mainMenuGui.add(guiButton[1]);
        mainMenuGui.add(guiButton[2]);


        GuiRenderer guiRenderer = new GuiRenderer(loader);



        boolean[] isMainMenu = {true}; //Testing purposes only
        boolean[] firstTick = {false}; //Testing purposes only
        GuiShader shader = new GuiShader();
        int colorHl = shader.getUniformLocation("colorHL");
        shader.setVec3(colorHl, new Vector3f(1,1 ,1));
        while (!windowManager.shouldDestroy()) {
            if (isMainMenu[0]) {
                firstTick[0] = false;
                glDisable(GL_DEPTH_TEST);
                glClear(GL_COLOR_BUFFER_BIT);
                implGl3.newFrame();
                implGlfw.newFrame();
                ImGui.newFrame();
                ImGui.begin("Main Menu Debug");
                ImGui.setWindowSize(new ImVec2(200, 300));
                float b0 = (float)  Math.clamp(Math.sin(glfwGetTime() % 1000), 0.0f, 1.0f);
                glClearColor(0, 0, b0, 1.0f);

                if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_ENTER) == GLFW_PRESS && isMainMenu[0]) {
                    glfwSetCursorPos(windowManager.getWindow(), GameWindowManager.getWidth() / 2, GameWindowManager.getHeight() / 2);
                    glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    glEnable(GL_DEPTH_TEST);
                    isMainMenu[0] = false;

                }
                double[] xp = new double[1];
                double[] xy = new double[1];
                r = 0;
                g = 0;
                b = 0;


                glfwSetCursorPosCallback(windowManager.getWindow(), new GLFWCursorPosCallback() {
                    @Override
                    public void invoke(long window, double xpos, double ypos) {
                        xp[0] = xpos;
                        xy[0] = ypos;
                        if (xpos > 414 && ypos > 213 && ypos < 864 && ypos < 324) {
                            if (glfwGetMouseButton(windowManager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                                isMainMenu[0] = false;
                                firstTick[0] = true;
                                System.out.println("Button Pressed");
                            }
                        }
                    }
                });

                ImGui.text("X Pos: " + xp[0]);
                ImGui.text("Y Pos: " + xy[0]);
                if (glfwGetMouseButton(windowManager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                    ImGui.text("Left Mouse Button Pressed ");
                }
                shader.start();
                guiRenderer.renderGUIs(mainMenuGui);
                shader.stop();
                ImGui.end();
                ImGui.render();
                implGl3.renderDrawData(ImGui.getDrawData());
                if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                    final long backupCurrentContext = glfwGetCurrentContext();
                    ImGui.updatePlatformWindows();
                    ImGui.renderPlatformWindowsDefault();
                    glfwMakeContextCurrent(backupCurrentContext);
                }
            }

            if (!isMainMenu[0]) {
                if (firstTick[0] == true) {
                   camera.setPos(new Vector3f(0, 0, 0));
                   camera.setYaw(0.0f);
                   camera.setPitch(0.0f);
                   firstTick[0] = false;
                }

                implGl3.newFrame();
                implGlfw.newFrame();
                ImGui.newFrame();
                if (!isPaused) {
                    ImGui.begin("Hello World");
                    camera.move(isDevMode, windowManager);
                    ImGui.setWindowSize(new ImVec2(300, 200));
                    ImGui.textColored(255, 255, 0, 255, "Player XYZ :" + camera.getPos().x + ", " + camera.getPos().y + ", " + camera.getPos().z);
                    ImGui.end();
                }


                for (int i = 0; i < terArrX.length; i++) {
                    modelPlayer.move(terArrX[i], isDevMode);
                    renderer.processTerrain(terArrX[i]);
                }
                renderer.processModel(model2);
                renderer.processModel(modelPlayer);
                renderer.processModel(lampModel);

                for (Model tree : allTrees) {
                    MasterRenderer.disableCulling();
                    renderer.processModel(tree);
                    MasterRenderer.enableCulling();
                }
                Light light = new Light(new Vector3f(x, 1000000, 200000), new Vector3f(1f, 1f, 1f));
                List<Light> lights = new ArrayList<Light>();
                lights.add(light);
                lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
                lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));

                renderer.render(lights, camera);

                if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_F1) != GLFW_PRESS) {
                    guiRenderer.renderGUIs(guiTextures);
                }

                if (glfwGetKey(windowManager.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS && !isPaused) {//if the key escape is down un grab the mouse
                    isMainMenu[0] = true;
                    //  isPaused = true;
//                    if (isPaused) {
//                        glfwSetInputMode(windowManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//                    //    guiRenderer.renderGUI(guiButton);
//                    }
                }

                if (glfwGetMouseButton(windowManager.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
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