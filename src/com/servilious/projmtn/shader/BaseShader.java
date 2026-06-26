package com.servilious.projmtn.shader;

public class BaseShader extends BaseShaderProgram {
   private static final String vsh = "resources/shaders/VertexShader.vsh";
    private static final String fsh = "resources/shaders/FragmentShader.fsh";

    public BaseShader() {
        super(vsh, fsh);
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "Position");
    }

    @Override
    protected void getAllUniformLocations() {

    }
}
