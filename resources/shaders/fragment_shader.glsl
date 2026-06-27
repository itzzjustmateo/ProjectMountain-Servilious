#version 400 core

in vec2 pass_texCoords;
in vec3 surfaceNormal;
in vec3 toLightVec[15];
in vec3 toCameraVec;
in float visibility;

out vec4 out_Color;

uniform sampler2D texSampler;
uniform vec3 lightColor[15];
uniform vec3 attenuation[15];
uniform float shineFactor;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVecToCamera = normalize(toCameraVec);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

 for (int i = 0; i < 15; i++) {
    float dist = length(toLightVec[i]);
    float attenFactor = attenuation[i].x + (attenuation[i].y * dist) + (attenuation[i].z * dist * dist);
    vec3 unitLightVec = normalize(toLightVec[i]);

    float nDotl = dot(unitNormal, unitLightVec);
    float brightness = max(nDotl, 0.0);
    vec3 lightDir = -unitLightVec;
    vec3 reflectedLightDir = reflect(lightDir, unitNormal);

    float specularFactor = dot(reflectedLightDir, unitVecToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineFactor);
    totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenFactor;
    totalSpecular = totalSpecular + (dampedFactor * lightColor[i]) / attenFactor;
    }
    vec4 texColor = texture(texSampler, pass_texCoords);
    if(texColor.a < 0.5) {
        discard;
    }
    totalDiffuse = max(totalDiffuse, 0.2);


    out_Color = vec4(totalDiffuse, 1.0) * texColor + vec4(totalSpecular, 1.0);
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}