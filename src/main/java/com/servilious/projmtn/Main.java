package com.servilious.projmtn;

import com.servilious.projmtn.shaders.BaseShader;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL33.*;

public class Main implements Runnable {
    private Window window;
    private ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 implGl3 = new ImGuiImplGl3();
    private String glslVer = "#version 330 core";
    private BaseShader shader;
    private BaseCamera camera;
    private Terrain terrain;

    private int vao, vbo, ebo;
    private Skybox skybox = new Skybox();
    private float positions[] = {
        -255, -2.0f, -255,
        255, -2.0f, -255,
            255, -2.0f, 255,
        -255, -2.0f, 255,
    };

    int indices[] = {
            0, 1, 2,
            2, 3, 0
    };



    public void setupQuad() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        FloatBuffer fb = BufferUtils.createFloatBuffer(positions.length);
        fb.put(positions);
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
        ib.put(indices);
        ib.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glEnableVertexAttribArray(0);
    }

    private void initImGUI() {
        ImGui.init();
        implGlfw.init(window.getWindow(), true);
        implGl3.init(glslVer);
    }


    public void main() {
        (new Thread(new Main())).start();
    }

    @Override
    public void run() {
        init();
        while (!window.shouldDestroyWindow()) {
            update();
        }
        clearMem(); 
    }

    private void update() {
        Matrix4f transform = new Matrix4f().identity().translate(new Vector3f(-1, 0.0f, 0.0f));
        Matrix4f view = new Matrix4f().identity();

        view.invert();
        view.rotateX(camera.getPitch());  //Have to switch around the pitch and yaw because it doesn't rotate properly if set to normal
        view.invert();

        view.rotateY(camera.getYaw());
        view.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        Matrix4f proj = new Matrix4f().identity().perspective(45, (float) window.getWidth() / window.getHeight(), 0.01f, 1000.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1.0f);
        implGl3.newFrame();
        implGlfw.newFrame();


        ImGui.newFrame();
        ImGui.begin("Debug");
        ImGui.setWindowSize(new ImVec2(400, 200));
        ImGui.text("Hello World!");
        camera.drawCamStats();
        ImGui.end();
        shader.start();
        shader.setProj(proj);
        shader.setView(view);
        shader.setTransformation(transform);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
       // glBindVertexArray(terrain.getTvao());
     //   glDrawArrays(GL_TRIANGLES, 0, 999);
        shader.stop();
        camera.move(window.getWindow());
        ImGui.render();
        implGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupCurrentContext = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupCurrentContext);
        }
        window.updateWindow();
    }

    private void init() {
        ImGui.createContext();
        camera = new BaseCamera();
        window = new Window(800, 640);
        window.createWindow();
        initImGUI();
        shader = new BaseShader();
        terrain = new Terrain();
        terrain.generateTerrain(0 ,0);
        setupQuad();
        skybox.loadSkybox();
    }

    private void clearMem() {
        implGl3.shutdown();
        implGlfw.shutdown();
        window.clearMem();
        shader.clearMem();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }
}
