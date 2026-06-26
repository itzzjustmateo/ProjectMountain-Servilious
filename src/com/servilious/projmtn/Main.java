package com.servilious.projmtn;

import com.servilious.projmtn.model.*;
import com.servilious.projmtn.model.textures.BaseModelTexture;
import com.servilious.projmtn.model.textures.TexturedModel;
import com.servilious.projmtn.shader.BaseShader;
import com.servilious.projmtn.util.Camera;
import org.lwjgl.util.vector.Vector3f;


public class Main {


    public static void main(String[] args) { //Well  change  this a bit soon but for now well not care much about organization until around episode 10 with the OBJ models

        DisplayManager display = new DisplayManager();
        display.createDisplay();


//        float positions[] = {
//            -1, -1, -1,
//            1, -1, -1,
//            1, 1, -1,
//            -1, 1, -1,
//
//                -1, -1, 1,
//                1, -1, 1,
//                1, 1, 1,
//                -1, 1, 1,
//        };
//
//        int indices[] = {
//            0, 1, 2, 2, 3, 0,
//            4, 5, 6, 6, 7, 4,
//        };
//
//        float texCoords[] = {
//                0, 0,
//                1, 0,
//                1, 1,
//                0, 1,
//
//                0, 0,
//                1, 0,
//                1, 1,
//                0, 1,
//        };
        BaseShader shader = new BaseShader();

        Renderer renderer = new Renderer(shader);
        VertexLoader loader = new VertexLoader();
       //String resPath = "/logo/logo.png";
        //  String resPath = "/textures/grass.png";
        String resPath = "/textures/tree/TreeTex.png";


        BaseModel baseModel  = ModelLoader.loadModel("/trees/tree.obj", loader);

        BaseModelTexture tex = new BaseModelTexture(loader.loadTexture(resPath));
        TexturedModel texturedModel = new TexturedModel(baseModel, tex);
        Model model = new Model(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1);


        System.out.println(baseModel.getVerticesCount());
        Camera camera = new Camera();
        while (!display.isDestroyed()) {
         //   model.increasePos(0, 0.0f, -0.5f);
       //     model.increaseRot(0, 0, 0.0f);
            renderer.clearGlBuffers();
            camera.update();
            shader.begin();
            shader.loadView(camera);
            renderer.render(model, shader);
            shader.end();
            display.updateDisplay();
        }
        loader.clearMem();
        shader.clearMem();
        display.destroyDisplay();
    }
}
