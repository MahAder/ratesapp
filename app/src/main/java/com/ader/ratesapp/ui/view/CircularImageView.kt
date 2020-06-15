package com.ader.ratesapp.ui.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.widget.ImageView


class CircularImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    protected override fun onDraw(canvas: Canvas) {
        val drawable: Drawable = getDrawable() ?: return
        if (getWidth() == 0 || getHeight() == 0) {
            return
        }
        val b = (drawable as BitmapDrawable).bitmap
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
        val w = width
        val roundBitmap = getCroppedBitmap(bitmap, w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }

    companion object {
        fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
            val sbmp: Bitmap
            sbmp = if (bmp.width != radius || bmp.height != radius) {
                val smallest =
                    Math.min(bmp.width, bmp.height).toFloat()
                val factor = smallest / radius
                Bitmap.createScaledBitmap(
                    bmp,
                    (bmp.width / factor).toInt(),
                    (bmp.height / factor).toInt(), false
                )
            } else {
                bmp
            }
            val output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val color = "#BAB399"
            val paint = Paint()
            val rect = Rect(0, 0, radius, radius)
            paint.setAntiAlias(true)
            paint.setFilterBitmap(true)
            paint.setDither(true)
            canvas.drawARGB(0, 0, 0, 0)
            paint.setColor(Color.parseColor(color))
            canvas.drawCircle(
                radius / 2 + 0.7f, radius / 2 + 0.7f,
                radius / 2 + 0.1f, paint
            )
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            canvas.drawBitmap(sbmp, rect, rect, paint)
            return output
        }
    }
}