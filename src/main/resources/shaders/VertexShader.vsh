#version 400 core

layout (location = 0) in vec3 v_Pos;
layout (location = 1) in vec2 v_TexCoords;
layout (location = 2) in vec3 v_Normal;

out vec2 pass_texCoords;
out vec3 surfaceNormal;
out vec3 toLightVec[15];
out vec3 toCameraVec;
out float visibility;

uniform mat4 transform;
uniform mat4 proj;
uniform mat4 view;
uniform vec3 lightPos[15];
uniform float useFakeLighting;
uniform float numOfRows;
uniform vec2 offset;

//Fog related settingds
//const float density = 0.008;
//const float gradient = 1.5;

const float density = 0;
const float gradient = 10;

void main(void) {
   vec4 worldPos = transform * vec4(v_Pos, 1.0);
   vec4 posRelativeToCam = view * worldPos;
   gl_Position = proj * posRelativeToCam;
   pass_texCoords = (v_TexCoords / numOfRows) + offset;

   vec3 realNormal = v_Normal;
   if(useFakeLighting > 0.5) {
      realNormal = vec3(0.0, 1.0, 0.0);
   }


   surfaceNormal = (transform * vec4(realNormal, 0.0)).xyz;
   for (int i = 0; i < 15; i++) {
      toLightVec[i] = lightPos[i] - worldPos.xyz;
   }
   toCameraVec = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
   float dist = length(posRelativeToCam.xyz);
   visibility = exp(-pow((dist * density), gradient));
   visibility = clamp(visibility, 0.0, 1.0);
}