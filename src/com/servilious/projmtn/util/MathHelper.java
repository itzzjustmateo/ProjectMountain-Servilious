package com.servilious.projmtn.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MathHelper {
    private static Vector3f rotX = new Vector3f(1, 0, 0);
    private static Vector3f rotY = new Vector3f(0, 1, 0);
    private static Vector3f rotZ = new Vector3f(0, 0, 1);


    public static Matrix4f setupTransformation(Vector3f m_Position, float m_RotX, float m_RotY, float m_RotZ, float m_Scale) { //m_ = model so m_Position is a shorter version of modelPosition
        Matrix4f transform = new Matrix4f();
        transform.setIdentity();
        Matrix4f.translate(m_Position, transform, transform);
        Matrix4f.rotate((float) Math.toRadians(m_RotX), rotX, transform, transform);
        Matrix4f.rotate((float) Math.toRadians(m_RotY), rotY, transform, transform);
        Matrix4f.rotate((float) Math.toRadians(m_RotZ), rotZ, transform, transform);
        Matrix4f.scale(new Vector3f(m_Scale, m_Scale, m_Scale), transform, transform);
        return transform;
    }

    public static Matrix4f setupView(Camera camera) {
        Matrix4f view = new Matrix4f();
        view.setIdentity();
        Matrix4f.invert(Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), view, view), view)  ;
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), view, view);
        Vector3f camPos = camera.getPos();
        Vector3f negCamPos = new Vector3f(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f.translate(negCamPos, view, view);
        return view;
    }
}
