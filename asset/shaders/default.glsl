#type vertex
#version 330 core

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in float aTexIndex;

out vec4 fColor;
out vec2 fTexCoord;
out float fTexIndex;

uniform mat4 uProjection;
uniform mat4 uView;

void main() {
    fColor = aColor;
    fTexCoord = aTexCoord;
    fTexIndex = aTexIndex;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}


#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoord;
in float fTexIndex;

out vec4 FragColor;

uniform sampler2D uTextures[8];

void main() {
    int index = int(fTexIndex);
    if (index >= 0 && index < 8) {
        FragColor = texture(uTextures[index], fTexCoord) * fColor;
    } else {
        FragColor = fColor;
    }
}

