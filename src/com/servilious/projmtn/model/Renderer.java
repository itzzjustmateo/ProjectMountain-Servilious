package com.servilious.projmtn.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;

public class Renderer {


    public void clearGlBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public void render(BaseModel model) {
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
   //     glDrawArrays(GL_TRIANGLES, 0, model.getVerticesCount());
        glDrawElements(GL_TRIANGLES,  model.getVerticesCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }


}
