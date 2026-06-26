package com.servilious.projmtn.shader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class BaseShaderProgram {
    private int vertexId, fragmentId, programId;


    public BaseShaderProgram (String vsh, String fsh) {
        vertexId = loadShader(vsh, GL_VERTEX_SHADER);
        fragmentId = loadShader(fsh, GL_FRAGMENT_SHADER);
        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        bindAttribs();
        glLinkProgram(programId);
        glValidateProgram(programId);
        getAllUniformLocations();
    }




    protected abstract void bindAttribs();

    protected abstract void getAllUniformLocations();

    protected void bindAttrib(int attribId, String attribName) {
        glBindAttribLocation(programId, attribId, attribName);
    }


    public void begin() {
        glUseProgram(programId);
    }

    public void end() {
        glUseProgram(0);
    }

    public void clearMem() {
            end();
            glDetachShader(programId, vertexId);
            glDetachShader(programId, fragmentId);
            glDeleteShader(vertexId);
            glDeleteShader(programId);
            glDeleteProgram(programId);
    }



    public int loadShader(String shaderPath, int shaderType) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(shaderPath));
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, sb);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to Compile " + (shaderType == GL_VERTEX_SHADER ? "Vertex " : "Fragment ") + "Shader \n" + glGetShaderInfoLog(shaderID, 512));
        }
        return shaderID;
    }
}
