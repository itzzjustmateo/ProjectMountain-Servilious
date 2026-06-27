package com.servilious.projmtn.util;

import com.servilious.projmtn.renderer.Camera;
import com.servilious.projmtn.world.Terrain;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    private Vector3f currentRay;
    private Matrix4f projectionMat;
    private Matrix4f viewMat;
    private Camera camera;
    private Terrain terrain;
    private Vector3f currentTerrainPoint;


    public MousePicker(Camera camera, Matrix4f projectionMat, Terrain terrain) {
        this.camera = camera;
        this.projectionMat = projectionMat;
        this.terrain = terrain;
        this.viewMat = MathHelper.createViewMatrix(camera);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void tick() {
        viewMat = MathHelper.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }

    }

    private Vector3f calculateMouseRay() {
        float mx = Mouse.getX();
        float my = Mouse.getY();
        Vector2f normalisedCoords = convertNormalizedDeviceCoords(mx, my);
        Vector4f clipCoords = new Vector4f(normalisedCoords.x, normalisedCoords.y, -1F, 1F);
        Vector4f viewCoords = inverseProjectionMatrix(clipCoords);
        Vector3f worldRay = inverseViewMatrix(viewCoords);
        return worldRay;
    }

    private Vector3f inverseViewMatrix(Vector4f viewCoords) {
        Matrix4f invertedViewMat = Matrix4f.invert(viewMat, null);
        Vector4f worldCoords = Matrix4f.transform(invertedViewMat, viewCoords, null);
        Vector3f mouseRay = new Vector3f(worldCoords.x, worldCoords.y, -worldCoords.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f inverseProjectionMatrix(Vector4f clipCoords) {
        Matrix4f invertedProjectionMat = Matrix4f.invert(projectionMat, null);
        Vector4f viewCoords = Matrix4f.transform(invertedProjectionMat, clipCoords, null);
        return new Vector4f(viewCoords.x, viewCoords.y, -1F, 0F);
    }

    private Vector2f convertNormalizedDeviceCoords(float mx, float my) {
        float x = (2F * mx) / Display.getWidth() - 1;
        float y = (2F * my) / Display.getHeight() - 1F;
        return new Vector2f(mx, my);
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPos();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }


    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }


    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }



}
