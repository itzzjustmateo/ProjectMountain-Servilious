#version 400 core

layout (location = 0) in vec2 v_Pos;

out vec2 texCoords;

uniform mat4 transform;

void main(void) {
    gl_Position = transform * vec4(v_Pos, 0.0, 1.0);
    texCoords = vec2((v_Pos.x + 1.0) / 2.0, 1 - (v_Pos.y + 1.0) / 2.0);
}