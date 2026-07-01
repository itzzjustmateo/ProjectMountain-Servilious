#version 400 core

layout (location = 0) in vec4 vertex; // <vec2 pos, vec2 tex>
out vec2 o_TexCoords;

uniform mat4 projection;

void main() {
    gl_Position = projection * vec4(vertex.xy, 0.0, 1.0);
    o_TexCoords = vertex.zw;
}