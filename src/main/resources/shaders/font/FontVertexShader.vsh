#version 400 core

layout (location = 0) in vec2 v_Pos;
layout (location = 1) in vec2 v_TexCoords;

out vec2 texCoords;

uniform mat4 transform;

void main(void) {
    gl_Position = transform * vec4(v_Pos, 0.0, 1.0);
    texCoords = vec2(v_TexCoords.x, -v_TexCoords.y) / 32;  // vec2((v_Pos.x + 1.0) / 2.0, 1 - (v_Pos.y + 1.0) / 2.0);
}