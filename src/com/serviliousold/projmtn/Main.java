package com.serviliousold.projmtn;

import com.serviliousold.projmtn.light.Light;
import com.serviliousold.projmtn.model.*;
import com.serviliousold.projmtn.model.objloader.ModelData;
import com.serviliousold.projmtn.model.objloader.OBJLoader;
import com.serviliousold.projmtn.model.textures.BaseModelTexture;
import com.serviliousold.projmtn.model.textures.TexturedModel;
import com.serviliousold.projmtn.shader.BaseShader;
import com.serviliousold.projmtn.util.Camera;
import org.lwjgl.util.vector.Vector3f;


public class Main {


    public static void main(String[] args) { //Well  change  this a bit soon but for now well not care much about organization until around episode 10 with the OBJ models

        DisplayManager display = new DisplayManager();
        display.createDisplay();


        BaseShader shader = new BaseShader();

        Renderer renderer = new Renderer(shader);
        VertexLoader loader = new VertexLoader();
       //String resPath = "/logo/logo.png";
        //  String resPath = "/textures/grass.png";
        String resPath = "/textures/tree/TreeTexture.png";
        Light light = new Light(new Vector3f(0, 20, -30), new Vector3f(1, 1, 1));

        ModelData tree =     OBJLoader.loadObjFile("trees/TreeModelTM.obj");


        BaseModel baseModel  =  ModelLoader.loadModel("trees/TreeModelTM", loader);   // loader.sendToVao(tree.getVertices(), tree.getTexCoords(), tree.getNormals(), tree.getIndices());   //ModelLoader.loadModel(resPath, loader);

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
            shader.loadLightToShader(light);
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
