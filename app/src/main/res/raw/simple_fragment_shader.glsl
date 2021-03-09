precision mediump float;
// OpenGL 是累加的颜色模型，遵循光的基本属性
uniform vec4 u_Color;
void main(){
    gl_FragColor = u_Color;
}