package com.example.loginningphone_12.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

class FormatImg {

    companion object{

        fun drawableToBitmap(drawable: Drawable): Bitmap{
            val img = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas: Canvas = Canvas(img)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return img
        }

    }

}