package com.servilious.projmtn.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class AbstractShaderProgram {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);


    public AbstractShaderProgram(String vertexFilepath, String fragmentFilepath) {
        vertexShaderId = loadShader(vertexFilepath, GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFilepath, GL_FRAGMENT_SHADER);
        programId = glCreateProgram();
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }


    public void start() {
        glUseProgram(programId);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void clear() {
        stop();
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attrib, String attribName) {
        glBindAttribLocation(programId, attrib, attribName);
    }

    protected void loadInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vec3) {
        glUniform3f(location, vec3.x, vec3.y, vec3.z);
    }

    protected void load2DVector(int location, Vector2f vec2) {
        glUniform2f(location, vec2.x, vec2.y);
    }

    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f mat4f) {
        mat4f.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(location, false, matrixBuffer);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) !=null) {
                shaderSource.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            System.err.println("[ERROR-2] Failed to read Shader File");
            e.printStackTrace();
            System.exit(-2);
        }
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(shaderId, 500));
            System.err.println("[ERROR-3]Could not Compile Shader");
            System.exit(-3);
        }
        return shaderId;
    }
}
