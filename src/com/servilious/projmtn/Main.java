package com.servilious.projmtn;

import com.servilious.projmtn.model.BaseModel;
import com.servilious.projmtn.model.Renderer;
import com.servilious.projmtn.model.VertexLoader;
import com.servilious.projmtn.shader.BaseShader;


public class Main {


    public static void main(String[] args) { //Well  change  this a bit soon but for now well not care much about organization until around episode 10 with the OBJ models

        DisplayManager display = new DisplayManager();
        display.createDisplay();


        float positions[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
        };

        int indices[] = {
            0, 1, 2, 2, 3, 0
        };



        Renderer renderer = new Renderer();
        VertexLoader loader = new VertexLoader();
        BaseModel model = loader.sendToVao(positions, indices);

        BaseShader shader = new BaseShader();
        System.out.println(model.getVerticesCount());

        while (!display.isDestroyed()) {
            renderer.clearGlBuffers();
            shader.begin();
            renderer.render(model);
            shader.end();
            display.updateDisplay();
        }
        loader.clearMem();
        display.destroyDisplay();
    }
}
