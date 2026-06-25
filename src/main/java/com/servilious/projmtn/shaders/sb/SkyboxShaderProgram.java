package com.servilious.projmtn.shaders.sb;

import com.servilious.projmtn.GlobalConstants;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public abstract class SkyboxShaderProgram {
    private int vertexID, fragmentID, programID;
    private FloatBuffer mb4x4 = BufferUtils.createFloatBuffer(16); //Create an 4 by 4 Matrix Buffer to store 4 by 4 matrix

    protected SkyboxShaderProgram(String vsPath, String fsPath) {
        vertexID = GlobalConstants.loadShader(vsPath, GL_VERTEX_SHADER);
        fragmentID = GlobalConstants.loadShader(fsPath, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);
        bindAttribs(); //Yes I know putting abstract methods inside of constructor is a very, Very bad practice but no other way I can think of that works.
        glLinkProgram(programID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
        glValidateProgram(programID);
        setUniformLocations();
    }

    protected abstract void bindAttribs();

    protected abstract void setUniformLocations();

    public int getUniformLocation(String location) {
        return glGetUniformLocation(programID, location);
    }

    public void bindAttrib(int attribId, String attribName) {
        glBindAttribLocation(programID, attribId, attribName);
    }

    protected void setVec2(int location, Vector2f vec2) {
        glUniform2f(location, vec2.x, vec2.y);
    }

    protected void setVec3(int location, Vector3f vec3) {
        glUniform3f(location, vec3.x, vec3.y, vec3.z);
    }

    protected void setMat4(int location, Matrix4f mat4) {
        mat4.get(mb4x4);
        glUniformMatrix4fv(location, false, mb4x4);
    }



    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void clearMem() {
        glDeleteProgram(programID);
    }
}
