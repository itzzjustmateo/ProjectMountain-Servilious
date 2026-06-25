package com.servilious.projmtn;

import com.servilious.projmtn.shaders.BaseShader;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

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

    private int vao, vbo;
    private float positions[] = {
            //Bottom Left (BL) triangle | Position
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            //Upper Right (UR) triangle
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
    };

    public void setupQuad() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer fb = BufferUtils.createFloatBuffer(positions.length);
        fb.put(positions);
        fb.flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

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
        glClearColor(0.1f, 0.15f, 1.0f, 1.0f);
        implGl3.newFrame();
        implGlfw.newFrame();


        ImGui.newFrame();
        ImGui.begin("Debug");
        ImGui.text("Hello World!");
        ImGui.end();
        shader.start();
        shader.setProj(proj);
        shader.setView(view);
        shader.setTransformation(transform);
        glDrawArrays(GL_TRIANGLES, 0, 6);
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
        setupQuad();
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
