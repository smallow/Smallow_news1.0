package com.smallow.common.utils;

import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by smallow on 2015/5/21.
 */
public class Utils {

    public static byte[] readStream(InputStream inStream) throws Exception {

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);

        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();

    }


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
                is.close();
                os.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void checkVisibility(View v, int visibility) {
        if (v == null) {
            return;
        }
        if (v.getVisibility() == visibility) {
            return;
        }
        v.setVisibility(visibility);
    }
}
