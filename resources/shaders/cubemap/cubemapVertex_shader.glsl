#version 400 core

layout (location = 0) in vec3 v_Pos;
out vec3 texCoords;

uniform mat4 proj;
uniform mat4 view;

void main(void) {
    gl_Position = proj * view * vec4(v_Pos, 1.0);
    texCoords = v_Pos;
}