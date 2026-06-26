package com.servilious.projmtn.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    public static BaseModel loadModel(String modelPath, VertexLoader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader("resources/model" + modelPath);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to find File " + e);
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float verticesFA[] = null;
        float texCoordsFA[] = null;
        float normalsFA[] = null;
        int indicesFA[] = null;

        try {
            while (true) {
                line = br.readLine();
                String curLine[] = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt ")) {
                    Vector2f texC = new Vector2f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]));
                    texCoords.add(texC);
                }
                else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
                    normals.add(normal);
                }
                else if (line.startsWith("f ")) {
                    texCoordsFA = new float[vertices.size() * 2];
                    normalsFA = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line !=null) {
                if (!line.startsWith("f ")) {
                    line = br.readLine();
                    continue;
                }
                String curLine[] = line.split(" ");
                String vertex1[] = curLine[1].split("/");
                String vertex2[] = curLine[2].split("/");
                String vertex3[] = curLine[3].split("/");

                processVertex(vertex1, indices, texCoords, normals, texCoordsFA, normalsFA);
                processVertex(vertex2, indices, texCoords, normals, texCoordsFA, normalsFA);
                processVertex(vertex3, indices, texCoords, normals, texCoordsFA, normalsFA);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesFA = new float[vertices.size() * 3];
        indicesFA = new int[indices.size()];

        int vertPtr = 0;
        for (Vector3f vertex : vertices) {
            verticesFA[vertPtr++] = vertex.x;
            verticesFA[vertPtr++] = vertex.y;
            verticesFA[vertPtr++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesFA[i] = indices.get(i);
        }
        return loader.sendToVao(verticesFA, texCoordsFA, indicesFA);
    }


    private static void processVertex(String vertexData[], List<Integer> indices, List<Vector2f> texCoords, List<Vector3f> normals, float texCoordsFA[], float normalsFA[]) {
        int curVertPtr = Integer.parseInt(vertexData[0]) - 1;
        indices.add(curVertPtr);
        Vector2f curTex = texCoords.get(Integer.parseInt(vertexData[1]) - 1);
        texCoordsFA[curVertPtr * 2] = curTex.x;
        texCoordsFA[curVertPtr * 2 + 1] = 1 - curTex.y;
        Vector3f curNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsFA[curVertPtr * 3] = curNorm.x;
        normalsFA[curVertPtr * 3 + 1] = curNorm.y;
        normalsFA[curVertPtr * 3 + 2] =  curNorm.z;
    }

}
