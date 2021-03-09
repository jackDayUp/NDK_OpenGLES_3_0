package com.byteflow.app.qingqi_study.util;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

import android.util.Log;

public class ShaderHelper {
    public static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int shaderType, String shaderCode) {
        final int shaderObjectId = glCreateShader(shaderType);

        if (shaderObjectId == 0) {
            Log.w(TAG, "compileShader: could not create new shader");
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        Log.i(TAG, "compileShader: Result of compiling source:" + shaderCode + "," + glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            Log.w(TAG, "compileShader: compile shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    // 一个OpenGL程序就是把一个顶点着色器和片段着色器链接在一起变成一个对象
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.w(TAG, "linkProgram: Could not create new program");
        }
        // 附加上着色器
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Log.i(TAG, "linkProgram: Results of linking program : " + glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            Log.e(TAG, "linkProgram: Linking of program failed");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.i(TAG,
                "validateProgram: Results of validate program:" + validateStatus[0] + "\nlog:" + glGetProgramInfoLog(programId));
        return validateStatus[0] != 0;
    }
}
