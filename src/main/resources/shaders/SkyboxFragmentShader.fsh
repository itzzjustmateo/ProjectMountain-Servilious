#version 330 core

in vec3 texCoords;

in vec3 ov_Color;
out vec4 o_Color;

uniform sampler2D cubeMap;

void main() {
    o_Color = texture(cubeMap, texCoords);
}