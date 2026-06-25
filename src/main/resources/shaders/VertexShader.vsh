#version 330 core

layout (location = 0) in vec3 v_Pos;
//layout (location = 1) in vec3 v_TintCol;

out vec3 ov_Color;

uniform mat4 transform;
uniform mat4 view;
uniform mat4 proj;

void main() {
    gl_Position = proj * view * transform * vec4(v_Pos, 1.0);
  //  ov_Color = v_TintCol;
}