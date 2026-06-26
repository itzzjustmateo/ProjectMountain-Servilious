#version 330 core

layout (location = 0) in vec3 v_Pos;

void main() {
    gl_Position = vec4(v_Pos, 1.0);
}