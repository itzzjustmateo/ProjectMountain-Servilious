package com.servilious.projmtn.world;

import com.servilious.projmtn.GlobalConstants;
import com.servilious.projmtn.renderer.VertexLoader;
import com.servilious.projmtn.renderer.model.BaseModel;
import com.servilious.projmtn.renderer.model.textures.TerrainTexture;
import com.servilious.projmtn.renderer.model.textures.TerrainTexturePack;
import com.servilious.projmtn.util.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 256;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    private float[][] heights;


    private float x;
    private float z;
    private BaseModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    public Terrain(int gridX, int gridZ, VertexLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }


    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public BaseModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrain(float wx, float wz) {
        float tx = wx - this.x; //terrainX is equal to world x minus this.x;
        float tz = wz - this.z;  //terrainZ is equal to world z minus this.z;
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gx = (int) Math.floor(tx / gridSquareSize);
        int gz = (int) Math.floor(tz / gridSquareSize);
        if (gx >= heights.length - 1 || gz >= heights.length - 1 || gx < 0 || gz < 0) {
            return 0;
        }

        float xCoord = (tx % gridSquareSize) / gridSquareSize;
        float zCoord = (tz % gridSquareSize) / gridSquareSize;
        float answer;
        if (xCoord <= (1 - zCoord)) {
            answer = MathHelper.barryCentric(new Vector3f(0, heights[gx][gz], 0), new Vector3f(1, heights[gx + 1][gz],0), new Vector3f(0, heights[gx][gz + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = MathHelper.barryCentric(new Vector3f(1, heights[gx + 1][gz], 0), new Vector3f(1, heights[gx + 1][gz + 1], 1), new Vector3f(0, heights[gx][gz + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }


    private BaseModel generateTerrain(VertexLoader loader, String heightMap){

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File( GlobalConstants.getResourcePath() + "/world/maps/" + heightMap+".png"));
        } catch (IOException e) {
            System.err.println("[ERROR-10] Failed to find Heightmap Please Make sure it is in The Correct Directory");
            e.printStackTrace();
            System.exit(-10);
        }

        int VERTEX_COUNT = img.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeightMap(j, i, img);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calcNormals(j, i, img);

                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calcNormals(int x, int y, BufferedImage heightMap) {
        float hl = getHeightMap(x - 1, y, heightMap);
        float hr = getHeightMap(x + 1, y, heightMap);
        float hd = getHeightMap(x, y - 1, heightMap);
        float hu = getHeightMap(x, y + 1, heightMap);
        Vector3f normal = new Vector3f(hl - hr, 2F, hd - hu);
        normal.normalize();
        return normal;
    }


    private float getHeightMap(int x, int y, BufferedImage heightMap) {
        if (x < 0 || x >= heightMap.getHeight() || y < 0 || y >= heightMap.getHeight()) {
            return 0;
        }
        float height = heightMap.getRGB(x, y);
        height += MAX_PIXEL_COLOR / 2F;
        height /= MAX_PIXEL_COLOR / 2F;
        height *= MAX_HEIGHT;
        return height;

    }

}
