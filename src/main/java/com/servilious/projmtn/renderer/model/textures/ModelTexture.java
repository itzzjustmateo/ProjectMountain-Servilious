package com.servilious.projmtn.renderer.model.textures;

public class ModelTexture {
    private int texId;
    private float shineFactor = 1;
    private float reflectivity = 0;

    private boolean isTransparent = false;
    private boolean useFakeLight = false;
    private int numOfRows = 1;

    public ModelTexture(int texId) {
        this.texId = texId;
    }

    /* Getter Methods */

    public boolean isTransparent() {
        return isTransparent;
    }

    public boolean shouldUseFakeLight() {
        return useFakeLight;
    }

    public int getId() {
        return this.texId;
    }

    public float getShineFactor() {
        return shineFactor;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    /* Setter Methods */

    public void setShineFactor(float sf) {
        this.shineFactor = sf;
    }

    public void setReflectivity(float reflec) {
        this.reflectivity = reflec;
    }

    public void setTransparent(boolean transparent) {
        this.isTransparent = transparent;
    }

    public void setUseFakeLight(boolean useFakeLight) {
        this.useFakeLight = useFakeLight;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }
}
