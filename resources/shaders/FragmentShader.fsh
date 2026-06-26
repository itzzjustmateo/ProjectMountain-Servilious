#version 330 core

out vec4 o_Color;

in vec2 o_TexCoords;

uniform sampler2D terrain;

void main() {
    o_Color = texture(terrain, o_TexCoords);  //vec4(1.0, 0.4, 0.0, 1.0);
}