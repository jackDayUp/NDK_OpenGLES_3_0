package com.byteflow.app.qingqi_study.objects;

import static com.byteflow.app.qingqi_study.Constants.BYTES_PER_FLOAT;

import android.hardware.display.VirtualDisplay;
import android.opengl.GLES20;

import com.byteflow.app.qingqi_study.data.VertexArray;
import com.byteflow.app.qingqi_study.programs.TextureShaderProgram;

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, S, T
            // t分量和y分量方向相反

            // Triangle Fan
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
    };

    private final VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
