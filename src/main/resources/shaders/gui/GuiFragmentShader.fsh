#version 400 core

in vec2 texCoords;
in vec3 ov_ColorHL;

out vec4 o_Color;

uniform sampler2D guiTex;

void main(void) {
    o_Color = texture(guiTex, texCoords) ; //* vec4(ov_ColorHL, 1.0);
}