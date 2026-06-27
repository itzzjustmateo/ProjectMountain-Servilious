package com.servilious.projmtn.util;

import com.servilious.projmtn.renderer.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MathHelper {

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0F - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }


    public static Matrix4f setupTransformationMatrix(Vector2f trans, Vector2f scale) {
        Matrix4f mat4f = new Matrix4f();
        mat4f.setIdentity();
        Matrix4f.translate(trans, mat4f, mat4f);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), mat4f, mat4f);
        return mat4f;
    }

    public static Matrix4f setupTransformationMatrix(Vector3f trans, float rx, float ry, float rz, float scale) {
        Matrix4f mat4f = new Matrix4f();
        mat4f.setIdentity();
        Matrix4f.translate(trans, mat4f, mat4f);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), mat4f, mat4f);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), mat4f, mat4f);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), mat4f, mat4f);
        Matrix4f.scale(new Vector3f(scale, scale, scale), mat4f, mat4f);
        return mat4f;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0),viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0),viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPos();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}
