package com.byteflow.app.qingqi_study.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.RawRes;

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {

            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("could not open resource:" + resId, e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("resource not found:" + resId, e);
        }
        return body.toString();
    }
}
