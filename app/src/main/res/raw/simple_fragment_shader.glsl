precision mediump float;
// OpenGL 是累加的颜色模型，遵循光的基本属性
varying vec4 v_Color;
void main(){
    gl_FragColor = v_Color;
}