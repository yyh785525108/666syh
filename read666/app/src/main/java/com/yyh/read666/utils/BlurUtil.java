package com.yyh.read666.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;


public class BlurUtil {

    public static Bitmap getBlurBackground(Context context,Bitmap originBitmap) {
        Bitmap blurBitmap = BlurUtil.apply(context, originBitmap, 1);
        return blurBitmap;
    }



    private static Bitmap apply(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = Bitmap.createScaledBitmap(sentBitmap, sentBitmap.getWidth(), sentBitmap.getHeight(), false);

        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return bitmap;


    }
}