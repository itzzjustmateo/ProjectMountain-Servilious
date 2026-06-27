#version 400 core

in vec2 texCoords;

out vec4 o_Color;

uniform sampler2D guiTex;

void main(void) {
    o_Color = texture(guiTex, texCoords);
}