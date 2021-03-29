package com.byteflow.app.qingqi_study.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.byteflow.app.qingqi_study.util.ShaderHelper;
import com.byteflow.app.qingqi_study.util.TextResourceReader;

import androidx.annotation.IntegerRes;

public class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));

    }

    public void userProgram() {
        GLES20.glUseProgram(program);
    }
}
