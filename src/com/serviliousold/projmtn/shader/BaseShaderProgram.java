package com.serviliousold.projmtn.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class BaseShaderProgram {
    private int vertexId, fragmentId, programId;
    private FloatBuffer mb4x4 = BufferUtils.createFloatBuffer(16);
    private FloatBuffer mb3x3 = BufferUtils.createFloatBuffer(9);


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

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }

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


    public void setFloat(int location, float value) {
        glUniform1f(location, value);
    }

    public void setInt(int location, int value) {
        glUniform1i(location, value);
    }

    public void setBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        glUniform1f(location, toLoad);
    }

    public void setVec2f(int location, Vector2f vec2) {
        glUniform2f(location, vec2.x, vec2.y);
    }

    public void setVec3f(int location, Vector3f vec3) {
        glUniform3f(location, vec3.x, vec3.y, vec3.z);
    }

    public void setVec4f(int location, Vector4f vec4) {
        glUniform4f(location, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void setMat4f(int location, Matrix4f mat4) {
        mat4.store(mb4x4); //In LWJGL 3 this should be replaced by .get();
        mb4x4.flip(); //In LWJGL 3 this line should be removed
        glUniformMatrix4(location, false, mb4x4);
    }


    public void setMat3f(int location, Matrix3f mat3) {
        mat3.store(mb3x3);//In LWJGL 3 this should be replaced by .get();
        mb3x3.flip();//In LWJGL 3 this line should be removed
        glUniformMatrix4(location, false, mb3x3);
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
