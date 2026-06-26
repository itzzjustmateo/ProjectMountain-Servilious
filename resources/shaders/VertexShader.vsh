#version 330 core

layout (location = 0) in vec3 v_Pos;
layout (location = 1) in vec2 v_TexCoords;

out vec2 o_TexCoords;

uniform mat4 transform;
uniform mat4 view;
uniform mat4 proj;

void main() {
    gl_Position = proj * view * transform * vec4(v_Pos, 1.0);
    o_TexCoords = vec2(v_TexCoords.x, -v_TexCoords.y);
}