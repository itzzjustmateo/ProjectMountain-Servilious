#version 400 core

in vec3 texCoords;
out vec4 out_Color;

uniform samplerCube dayCubeMap;
uniform samplerCube nightCubeMap;
uniform float blendFactor;

uniform vec3 fogColor;

const float lowerLimit = 0.0;
const float higherLimit = 30.0;

void main(void) {
    vec4 dayTexture = texture(dayCubeMap, texCoords);
    vec4 nightTexture = texture(nightCubeMap, texCoords);
    vec4 finalColor = mix(dayTexture, nightTexture, blendFactor);

    float factor = (texCoords.y - lowerLimit) / (higherLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    out_Color = mix(vec4(fogColor, 1.0), finalColor, factor);
}