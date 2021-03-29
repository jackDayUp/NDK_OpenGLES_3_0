package com.byteflow.app.qingqi_study.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.logging.SocketHandler;

import androidx.annotation.DrawableRes;

/**
 * 简单来说纹理就是一张图片或者照片，它们可以被加载到OpenGl中
 *
 * 每一个二维的纹理都有自己的坐标，从拐角0，0 到1，1
 * 纹理不一定是正方形，但是要是2的整数倍，这样可以应用更多的场景
 *
 * 纹理的尺寸有最大值
 *
 * 纹理过滤（texture filtering）：当纹理被扩大或者缩小的时候，需要使用纹理过滤明确说明会发生什么
 * 最近邻过滤：nearest-filtering
 */
public class TextureHelper {
    public static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, @DrawableRes int resId) {
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        // OpenGL需要非压缩形式的原始数据
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            Log.w(TAG, "Resource ID " + resId + " could not be decoded.");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // 告诉OpenGL需要一个2D纹理，要绑定的ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        // 缩小的情况下，使用三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        // 放大的情况，使用双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        // 读入bitmap对象，绑定到纹理对象
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        // 完成了纹理对象的绑定，就解除绑定，避免其他方法改变这个纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
}
