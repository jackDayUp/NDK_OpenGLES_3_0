package com.byteflow.app.qingqi_study;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.byteflow.app.R;
import com.byteflow.app.qingqi_study.util.MatrixHelper;
import com.byteflow.app.qingqi_study.util.ShaderHelper;
import com.byteflow.app.qingqi_study.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRender implements GLSurfaceView.Renderer {
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";
    private static final int COLOR_COMPONENT = 3;
    private static final int STRIDE = (COLOR_COMPONENT + POSITION_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;
    private int aPositionLocation;
    private final FloatBuffer vertexData;
    private final Context mContext;
    private int mProgram;
    private float[] projectionMatrix = new float[16];
    private int uMatrixLocation;

    private float[] modelMatrix = new float[16];

    public AirHockeyRender(Context context) {
        mContext = context;
        // 无论任何时候，如果我们想要表示一个openGL中的物体，都要考虑如何用点，直线以及三角形表示
        float[] tableVertices = {
//                0f,    0f,   0.0f,1.5f, 1f,   1f,   1f,
//                -0.5f, -0.8f,0.0f,1.0f, 0.7f, 0.7f, 0.7f,
//                0.5f,  -0.8f,0.0f,1.0f, 0.7f, 0.7f, 0.7f,
//                0.5f,  0.8f, 0.0f,2.0f, 0.7f, 0.7f, 0.7f,
//                -0.5f, 0.8f, 0.0f,2.0f, 0.7f, 0.7f, 0.7f,
//                -0.5f, -0.8f,0.0f,1.0f, 0.7f, 0.7f, 0.7f,
//                -0.5f, 0f,   0.0f,1.5f, 1f, 0f, 0f,
//                0.5f,  0f,   0.0f,1.5f, 0f, 0f, 1f,
//                0f,   -0.4f, 0.0f,1.25f, 0f, 0f, 1f,
//                0f,   0.4f,  0.0f,1.75f, 1f, 0f, 0f

                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 0f, 1f,
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        };

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT).order(
                ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVertices);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderResource);

        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        ShaderHelper.validateProgram(mProgram);

        glUseProgram(mProgram);

        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexData.position(0);
        // 关联属性和顶点数据的数组
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        // openGL知道去哪里找数据
        glEnableVertexAttribArray(aPositionLocation);

        // 当读取颜色的时候，要从颜色的位置开始读
        vertexData.position(POSITION_COMPONENT_COUNT);
        //将颜色数据和着色器中的a_Color中的数据联系起来， 要知道下一次读取的时候要跳过多少个字节读取颜色值
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT, GL_FLOAT, false, STRIDE, vertexData);

        glEnableVertexAttribArray(aColorLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
//        final float aspectRatio = width > height ? (float) width / height : (float) height / width;
//        if (width > height) {
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / height, 1f, 10f);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f,1f, 0f, 0f);
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);


    }
}






























